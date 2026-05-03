////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                   Подключение плагинов + декларация версии Java для Spring Boot и Gradle                   //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

plugins {
    java                                    // Компилирует код, создаёт JAR
    id("org.springframework.boot")
    id("io.spring.dependency-management")   // Управление зависимостями
    id("org.openapi.generator")             // Генерация DTO из OpenAPI
    `maven-publish`                         // Публикация в Nexus
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(project.property("javaVersion").toString().toInt()))
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                            Основные метаданные                                             //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

group = "by.itbatia.psp"
version = "1.0.0"
description = "User management microservice"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                         Конфигурации зависимостей                                          //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                                Зависимости                                                 //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

dependencies {

    // Spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Openapi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.property("springdocOpenapiStarterWebmvcUiVersion")}")

    // DB stack: JPA + PostgreSQL + Flyway + Hibernate Envers (аудит)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    implementation("org.hibernate:hibernate-envers:${project.property("hibernateEnversVersion")}")

    // Observer stack: Metrics + OpenTelemetry (трассировка)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")  // Contains OpenTelemetry SDK + OTLP exporter
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:${project.property("micrometerRegistryPrometheusVersion")}")

    // Lombok + Mapstruct
    compileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")
    implementation("org.mapstruct:mapstruct:${project.property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${project.property("mapstructVersion")}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${project.property("lombokMapstructBindingVersion")}")

    // Logback
    implementation("net.logstash.logback:logstash-logback-encoder:${project.property("logstashLogbackEncoderVersion")}")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")   // @DataJpaTest
    implementation("org.springframework.boot:spring-boot-data-jpa-test")           // @AutoConfigureTestDatabase
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")          // @Testcontainers
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test") // @AutoConfigureMockMvc
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")                  // Assertions
    testImplementation("org.testcontainers:postgresql:${project.property("testcontainersPostgresqlVersion")}")

    testCompileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    testAnnotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // PSP projects
    implementation("by.itbatia.psp:common:${project.property("commonVersion")}") // from Nexus. From local: implementation(project(":common"))
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                             Обработка ресурсов                                             //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.register<WriteProperties>("generateApplicationProperties") {
    destinationFile = layout.buildDirectory.file("resources/main/application.properties").get().asFile
    property("info.application.name", project.name)
    property("info.application.version", project.version.toString())
    property("info.application.description", project.description ?: "")
}

tasks.named("processResources") {
    dependsOn("generateApplicationProperties")
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                          Генерация API из OpenAPI                                          //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/person-service/openapi/person-service-openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated-sources/openapi").get().asFile.absolutePath)
    apiPackage.set("by.itbatia.psp.personservice.api")
    modelPackage.set("by.itbatia.psp.common.dto")

    importMappings.set(                             // import DTO из common, которые используются в OpenAPI-спецификации
        mapOf(
            "IndividualCreateRequest" to "by.itbatia.psp.common.dto.IndividualCreateRequest",
            "IndividualUpdateRequest" to "by.itbatia.psp.common.dto.IndividualUpdateRequest",
            "IndividualResponse" to "by.itbatia.psp.common.dto.IndividualResponse",

            "UserCreateRequest" to "by.itbatia.psp.common.dto.UserCreateRequest",
            "UserUpdateRequest" to "by.itbatia.psp.common.dto.UserUpdateRequest",
            "UserResponse" to "by.itbatia.psp.common.dto.UserResponse",

            "AddressCreateRequest" to "by.itbatia.psp.common.dto.AddressCreateRequest",
            "AddressUpdateRequest" to "by.itbatia.psp.common.dto.AddressUpdateRequest",
            "AddressResponse" to "by.itbatia.psp.common.dto.AddressResponse",

            "CountryResponse" to "by.itbatia.psp.common.dto.CountryResponse",
            "ErrorResponse" to "by.itbatia.psp.common.dto.ErrorResponse"
        )
    )

    globalProperties.set(                            // ← docs - https://openapi-generator.tech/docs/generators/spring/
        mapOf(
            "apis" to ""
        )
    )

    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",                // ← использует jakarta.* вместо javax.* (требуется для Spring Boot 4)
            "useSpringBoot4" to "true",              // ← сгенерировать код и предоставить зависимости для использования со Spring Boot 4.x (+ включает Jakarta EE)
            "interfaceOnly" to "true",               // ← только интерфейсы, без реализации (не генерирует: class AuthApiController implements AuthApi)
            "skipDefaultInterface" to "true",        // ← не генерировать default-реализацию интерфейсов (только сигнатура метода, без тела)
            "includeHttpRequestContext" to "false",  // ← не включать HttpServletRequest в качестве доп параметра в генерируемые методы
            "useBeanValidation" to "true"            // ← Use BeanValidation API annotations (отключает @Validated на классе и @Valid на параметрах)
        )
    )
}

// Подключаем сгенерированные интерфейсы API к исходникам
sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated-sources/openapi/src/main/java"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                              Настройки сборки                                              //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("person-service.jar")
    layered {
        enabled = true
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                              Настройки тестов                                              //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.withType<Test> {
    useJUnitPlatform()
}
