pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    val springBootVersion = providers.gradleProperty("springBootVersion").get()
    val springDependencyManagementVersion = providers.gradleProperty("springDependencyManagementVersion").get()
    val openapiGeneratorVersion = providers.gradleProperty("openapiGeneratorVersion").get()

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("org.openapi.generator") version openapiGeneratorVersion
    }
}

dependencyResolutionManagement {
    repositories {
        maven("http://localhost:8081/repository/maven-public/") {
            name = "Nexus"
            credentials {
                username = providers.gradleProperty("nexusUsername").get()
                password = providers.gradleProperty("nexusPassword").get()
            }
            withGroovyBuilder {
                setProperty("allowInsecureProtocol", true)
            }
            mavenContent {
                releasesOnly()
            }
        }
    }
}

rootProject.name = "payment-system"
include(":individuals-api")
include(":person-service")
include(":common")
