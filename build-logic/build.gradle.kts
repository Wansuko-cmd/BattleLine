plugins {
    `kotlin-dsl`
    alias(libs.plugins.ktlint)
}

dependencies {
    implementation(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("kotlinJvm") {
            id = "com.wsr.jvm"
            implementationClass = "KotlinJvmPlugin"
        }
        register("kotlinMultiPlatform") {
            id = "com.wsr.multiplatform"
            implementationClass = "KotlinMultiPlatformPlugin"
        }
        register("mavenPublish") {
            id = "com.wsr.maven.publish"
            implementationClass = "MavenPublishPlugin"
        }
    }
}
