plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "by.itbatia"
version = "1.0.0"
description = "Authentication orchestrator"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Lombok
    compileOnly("org.projectlombok:lombok:${project.property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${project.property("lombokVersion")}")

    // Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:${project.property("micrometerVersion")}")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
