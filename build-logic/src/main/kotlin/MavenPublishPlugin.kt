import ext.alias
import ext.getPlugin
import ext.getVersion
import ext.libs
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName

class MavenPublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("maven-publish")
            }

            publishing {
                publications {
                    getByName<MavenPublication>("kotlinMultiplatform") {
                        groupId = libs.getVersion("lib.group.id")
                        artifactId = project.name
                        version = libs.getVersion("lib.version")
                    }
                }
            }
        }
    }
}

private fun Project.publishing(configure: Action<PublishingExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishing", configure)
