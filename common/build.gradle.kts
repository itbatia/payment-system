////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                   Подключение плагинов + декларация версии Java для Spring Boot и Gradle                   //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

plugins {
    `java-library`
    `maven-publish`
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

repositories {
    mavenCentral()
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////                                             Публикация в Nexus                                             //////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

publishing {
    repositories {
        maven {
            url = uri("http://localhost:8081/repository/maven-releases/")
            credentials {
                username = "admin"
                password = "admin-password"
            }
            withGroovyBuilder {
                setProperty("allowInsecureProtocol", true)
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "by.itbatia.psp"
            artifactId = "common"
            version = "1.0.0"
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
