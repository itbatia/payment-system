plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.openapi.generator")
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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Resolve CVE-2025-48924 in 'spring-cloud-starter-openfeign'
    implementation("org.apache.commons:commons-lang3:${project.property("commonsLang3Version")}")
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
    modelPackage.set("by.itbatia.individualsapi.dto")

    globalProperties.set(
        mapOf(
            "models" to "",
            "apis" to "false",
            "supportingFiles" to "false"
        )
    )

    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",
            "openApiNullable" to "false"
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
