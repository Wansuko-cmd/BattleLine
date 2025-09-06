plugins {
    alias(buildLogic.plugins.kotlin.multiplatform)
    alias(buildLogic.plugins.maven.publish)
    alias(libs.plugins.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.datetime)
                implementation(libs.serialization)
            }
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
