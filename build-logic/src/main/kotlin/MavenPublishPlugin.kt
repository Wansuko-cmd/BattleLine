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

class MavenPublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("maven-publish")
            }

            publishing {
                publications {
                    create<MavenPublication>(libs.getVersion("lib.name")) {
                        groupId = libs.getVersion("lib.group.id")
                        artifactId = project.name
                        version = libs.getVersion("lib.version")
                        from(components["kotlin"])
                    }
                }
            }
        }
    }
}

private fun Project.publishing(configure: Action<PublishingExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishing", configure)
