plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)
            }
        }
    }
}
