plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
    alias(buildLogic.plugins.maven.publish)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core)

                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)
            }
        }
    }
}
