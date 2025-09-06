@file:Suppress("UnstableApiUsage")

pluginManagement {
    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("buildLogic") {
            from(files("./gradle/build-logic.versions.toml"))
        }
    }
}

rootProject.name = "BattleLine"

include(":core")
include(":cpu")
include(":sample")