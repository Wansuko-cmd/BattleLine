plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.ktlint) apply false
    id("maven-publish")
}

subprojects {
    apply(plugin = "maven-publish")
}

tasks.register<Delete>(name = "clean") {
    delete(rootProject.layout.buildDirectory)
}
