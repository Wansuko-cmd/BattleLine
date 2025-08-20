plugins {
    alias(buildLogic.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.core)
    implementation(projects.cpu)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)
}
