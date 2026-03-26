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
description = "Authentication orchestrator"

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
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                                Зависимости                                                 //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

dependencies {

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Openapi
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${project.property("springdocOpenapiStarterWebfluxUiVersion")}")

    // Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:${project.property("micrometerRegistryPrometheusVersion")}")

    // Lombok
    compileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // Logback
    implementation("net.logstash.logback:logstash-logback-encoder:${project.property("logstashLogbackEncoderVersion")}")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("com.github.dasniko:testcontainers-keycloak:${project.property("testcontainersKeycloakVersion")}")

    testCompileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    testAnnotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // Resolve CVE-2025-48924 in 'spring-cloud-starter-openfeign'
    implementation("org.apache.commons:commons-lang3:${project.property("commonsLang3Version")}")
    // Resolve GHSA-72hv-8253-57qq in 'springdoc-openapi-starter-webflux-ui'
    implementation("com.fasterxml.jackson.core:jackson-core:${project.property("jacksonCoreVersion")}")
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

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/individuals-api/openapi/individuals-api.yaml")
    outputDir.set(layout.buildDirectory.dir("generated-sources/openapi").get().asFile.absolutePath)
    modelPackage.set("by.itbatia.psp.individualsapi.dto")
    apiPackage.set("by.itbatia.psp.individualsapi.api")

    globalProperties.set(
        mapOf(
            "models" to "",                         // ← включить генерацию DTO (""=all)
            "apis" to "",                           // ← включить генерацию API (""=all)
            "supportingFiles" to "false"            // ← не включить генерацию Utils (-ApiUtil)
        )
    )

    configOptions.set(                              // ← docs - https://openapi-generator.tech/docs/generators/spring/
        mapOf(
            "useJakartaEe" to "true",               // ← использует jakarta.* вместо javax.* (требуется для Spring Boot 4)
            "useSpringBoot4" to "true",             // ← сгенерировать код и предоставить зависимости для использования со Spring Boot 4.x (+ включает Jakarta EE)
            "openApiNullable" to "false",           // ← не генерировать аннотации @Nullable/@NonNull

            "interfaceOnly" to "true",              // ← только интерфейсы, без реализации (не генерирует: class AuthApiController implements AuthApi)
            "skipDefaultInterface" to "true",       // ← не генерировать default-реализацию интерфейсов (только сигнатура метода, без тела)

            "reactive" to "true",                   // ← использовать Mono/Flux в возвращаемом значении (!!! Но и RequestBody заворачивает тоже)
            "includeHttpRequestContext" to "false", // ← не включать ServerWebExchange в качестве доп параметра в генерируемые методы

            "useBeanValidation" to "false",         // ← Use BeanValidation API annotations (отключает @Validated на классе и @Valid на параметрах)
            "performBeanValidation" to "false",     // ← Use Bean Validation Impl. to perform BeanValidation (опционально)
            "useSpringBuiltInValidation" to "false" // ← Disable @Validated at the class level when using built-in validation. (опционально)
        )
    )
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated-sources/openapi/src/main/java"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn(tasks.named("openApiGenerate"))
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                    Очистка пустых артефактов генерации                                     //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.register("deleteGeneratedEmptyDirs") {
    doLast {
        val genDir = layout.buildDirectory.dir("generated-sources/openapi/src/main/java").get().asFile
        listOf(
            "org/openapitools/api",
            "org/openapitools/configuration",
            "org/openapitools",
            "org"
        ).forEach { directory ->
            val dir = File(genDir, directory)
            if (dir.exists() && dir.list()?.isEmpty() == true) {
                dir.deleteRecursively()
            }
        }
    }
}

tasks.named("openApiGenerate") {
    finalizedBy(tasks.named("deleteGeneratedEmptyDirs"))
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                              Настройки сборки                                              //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("individuals-api.jar")
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
