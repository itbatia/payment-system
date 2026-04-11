////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                   Подключение плагинов + декларация версии Java для Spring Boot и Gradle                   //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
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

repositories {
    mavenCentral()
    maven {
        url = uri("http://localhost:8081/repository/maven-public/")
        credentials {
            username = "admin"
            password = "admin-password"
        }
        withGroovyBuilder {
            setProperty("allowInsecureProtocol", true)
        }
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

    // DB stack:
    // JPA + PostgreSQL + Flyway + Hibernate Envers (аудит)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly ("org.flywaydb:flyway-database-postgresql")
//    implementation("org.hibernate.com:hibernate-envers")

    // Observer stack:
    // Metrics + OpenTelemetry (трассировка)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:${project.property("micrometerRegistryPrometheusVersion")}")
//    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:${project.property("opentelemetrySpringBootStarterVersion")}")
//    implementation ("io.github.openfeign:feign-micrometer:${project.property("feignMicrometerVersion")}")

    // Lombok + Mapstruct
    compileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")
    implementation("org.mapstruct:mapstruct:${project.property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${project.property("mapstructVersion")}")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${project.property("lombokMapstructBindingVersion")}")

    // Logback
    implementation("net.logstash.logback:logstash-logback-encoder:${project.property("logstashLogbackEncoderVersion")}")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    testAnnotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // PSP projects
     implementation(project(":common")) // from local
//    implementation("by.itbatia.psp:common:${project.property("commonVersion")}") // from Nexus
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
//////                                          Генерация DTO из OpenAPI                                          //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateCommonDto") {
    generatorName.set("spring")
    inputSpec.set("$rootDir/person-service/openapi/person-service-api.yaml")
    outputDir.set("$rootDir/common")
    modelPackage.set("by.itbatia.psp.common.dto")

    globalProperties.set(
        mapOf(
            "models" to "",                          // ← включить генерацию DTO (""=all)
            "supportingFiles" to "false"             // ← не включить генерацию Utils (-ApiUtil)
        )
    )

    configOptions.set(                               // ← docs - https://openapi-generator.tech/docs/generators/spring/
        mapOf(
            "useJakartaEe" to "true",                // ← использует jakarta.* вместо javax.* (требуется для Spring Boot 4)
            "useSpringBoot4" to "true",              // ← сгенерировать код и предоставить зависимости для использования со Spring Boot 4.x (+ включает Jakarta EE)
            "openApiNullable" to "false",            // ← не генерировать аннотации @Nullable/@NonNull
            "modelTests" to "false",                 // ← отключает генерацию тестов для моделей

            "additionalModelTypeAnnotations" to """
                @lombok.Data
            """.trimIndent(),

            "useBeanValidation" to "true",            // ← Use BeanValidation API annotations (добавляет @Validated на классе и @Valid на параметрах)
            "performBeanValidation" to "true"         // ← Добавляет @NotNull, @Size(min = ..., max = ...), @Pattern, @Email, @Min, @Max и т.д.
        )
    )
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generatePersonServiceApi") {
    generatorName.set("spring")
    inputSpec.set("$rootDir/person-service/openapi/person-service-api.yaml")
    outputDir.set(layout.buildDirectory.dir("generated-sources/openapi").get().asFile.absolutePath)
    apiPackage.set("by.itbatia.psp.personservice.api")
    modelPackage.set("by.itbatia.psp.common.dto")

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

tasks.register("openApiGenerateAll") {
    dependsOn("generateCommonDto", "generatePersonServiceApi")
}

tasks.named("compileJava") {
    dependsOn("openApiGenerateAll")
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                              Очистка ненужных или пустых артефактов генерации                              //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.register("deleteOpenApiMeta") {
    doLast {
        val metaDir = file("$rootDir/common/.openapi-generator")
        if (metaDir.exists()) {
            metaDir.deleteRecursively()
            println("Deleted .openapi-generator metadata folder")
        }
    }
}

tasks.register("deleteGeneratedEmptyDirs") {
    doLast {
        val srcMainJava = file("$rootDir/common/src/main/java")
        val srcMainResources = file("$rootDir/common/src/main/resources")
        val testDir = file("$rootDir/common/src/test")

        // Delete src/main/java/org/:
        listOf(
            "org/openapitools/api",
            "org/openapitools/configuration",
            "org/openapitools",
            "org"
        ).forEach { pkg ->
            val dir = File(srcMainJava, pkg)
            if (dir.exists()) {
                dir.deleteRecursively()
                println("Deleted junk package: $pkg")
            }
        }

        // Delete src/test/:
        if (testDir.exists()) {
            testDir.deleteRecursively()
            println("Deleted empty test/java directory")
        }

        // Delete resources:
        if (srcMainResources.exists() && srcMainResources.list()?.isEmpty() == true) {
            srcMainResources.deleteRecursively()
            println("Deleted empty resources folder")
        }
    }
}

tasks.named("generateCommonDto") {
    finalizedBy("deleteOpenApiMeta")
    finalizedBy("deleteGeneratedEmptyDirs")
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
