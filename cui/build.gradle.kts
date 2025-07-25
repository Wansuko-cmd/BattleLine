plugins {
    alias(buildLogic.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.domain)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)
}
