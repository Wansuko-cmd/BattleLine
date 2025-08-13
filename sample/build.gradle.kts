plugins {
    alias(buildLogic.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.lib)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)
}
