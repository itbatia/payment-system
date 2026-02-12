pluginManagement {
    val springBootVersion = providers.gradleProperty("springBootVersion").get()
    val springDependencyManagementVersion = providers.gradleProperty("springDependencyManagementVersion").get()
    val openapiGeneratorVersion = providers.gradleProperty("openapiGeneratorVersion").get()

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("org.openapi.generator") version openapiGeneratorVersion
    }
}

rootProject.name = "payment-system"
include(":individuals-api")
