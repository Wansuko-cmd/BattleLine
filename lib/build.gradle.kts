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

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("battle-line") {
            groupId = libs.versions.lib.group.id.get()
            artifactId = "battle-line"
            version = libs.versions.lib.version.get()
            from(components["kotlin"])
        }
    }
}
