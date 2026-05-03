////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                   Подключение плагинов + декларация версии Java для Spring Boot и Gradle                   //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

plugins {
    `java-library`                // Создаёт JAR, экспортирует зависимости
    `maven-publish`               // Публикация в Nexus
    id("org.openapi.generator")   // Генерация DTO из OpenAPI
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
description = "common DTO and utils"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                         Конфигурации зависимостей                                          //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                             Публикация в Nexus                                             //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

publishing {
    repositories {
        maven {
            url = uri("http://localhost:8081/repository/maven-releases/")
            credentials {
                username = providers.gradleProperty("nexusUsername").get()
                password = providers.gradleProperty("nexusPassword").get()
            }
            withGroovyBuilder {
                setProperty("allowInsecureProtocol", true)
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                                Зависимости                                                 //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

dependencies {

    // Lombok
    compileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // Uses only to support OpenApi annotations
    implementation("jakarta.annotation:jakarta.annotation-api:${project.property("jakartaAnnotationApiVersion")}")
    implementation("jakarta.validation:jakarta.validation-api:${project.property("jakartaValidationApiVersion")}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${project.property("jacksonAnnotationsVersion")}")
    implementation("io.swagger.core.v3:swagger-annotations:${project.property("swaggerAnnotationsVersion")}")
    implementation("org.springframework:spring-core:${project.property("springVersion")}")
    implementation("com.google.code.findbugs:jsr305:${project.property("jsr305Version")}")
    implementation("org.springframework:spring-context:${project.property("springVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${project.property("springBootVersion")}")
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                          Генерация DTO из OpenAPI                                          //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/common/openapi/common-openapi.yaml")
    outputDir.set(layout.buildDirectory.dir("generated-sources/openapi").get().asFile.absolutePath)
    modelPackage.set("by.itbatia.psp.common.dto")

    globalProperties.set(
        mapOf(
            "models" to "",                          // ← включить генерацию DTO (""=all)
            "apis" to "false",                       // ← НЕ генерировать API-интерфейсы
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

// Подключаем сгенерированные DTO к исходникам
sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated-sources/openapi/src/main/java"))
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                              Очистка ненужных или пустых артефактов генерации                              //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

tasks.register("deleteOpenApiMeta") {
    doLast {
        val metaDir = file("$rootDir/common/build/generated-sources/openapi/.openapi-generator")
        if (metaDir.exists()) {
            metaDir.deleteRecursively()
            println("Deleted .openapi-generator metadata folder")
        }
    }
}

tasks.register("deleteGeneratedEmptyDirs") {
    doLast {
        val srcMainJava = file("$rootDir/common/build/generated-sources/openapi/src/main/java")
        val srcMainResources = file("$rootDir/common/build/generated-sources/openapi/src/main/resources")
        val testDir = file("$rootDir/common/build/generated-sources/openapi/src/test")

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

tasks.named("openApiGenerate") {
    finalizedBy("deleteOpenApiMeta")
    finalizedBy("deleteGeneratedEmptyDirs")
}