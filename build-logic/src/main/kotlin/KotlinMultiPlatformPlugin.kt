import ext.alias
import ext.getPlugin
import ext.libs
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiPlatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.getPlugin("kotlin.multiplatform"))
                alias(libs.getPlugin("ktlint"))
            }

            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            kotlin {
                applyDefaultHierarchyTemplate()
                jvm()

                // Androidビルド用
//                androidTarget()

                // iOSビルド用
//                iosX64()
//                iosArm64()
//                iosSimulatorArm64()
            }
        }
    }
}

private fun Project.kotlin(configure: Action<KotlinMultiplatformExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)
