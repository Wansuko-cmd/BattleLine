plugins {
    alias(buildLogic.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.shared)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)
}
