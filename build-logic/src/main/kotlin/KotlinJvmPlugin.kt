import ext.alias
import ext.getPlugin
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinJvmPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.getPlugin("kotlin.jvm"))
                alias(libs.getPlugin("ktlint"))
            }
        }
    }
}
