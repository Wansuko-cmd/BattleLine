plugins {
    alias(buildLogic.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.core)
    implementation(projects.cpu)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.datetime)

    implementation("com.github.Wansuko-cmd.Perceptron:lib:0.0.1-alpha05")
}
