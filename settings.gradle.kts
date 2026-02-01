pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val openapiGeneratorVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("org.openapi.generator") version openapiGeneratorVersion
    }
}

rootProject.name = "payment-system"
include(":individuals-api")
