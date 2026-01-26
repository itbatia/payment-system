import org.gradle.jvm.toolchain.JavaLanguageVersion

subprojects {
    apply(plugin = "java")
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(project.property("javaVersion").toString().toInt()))
        }
    }
}

plugins {
    base
}
