package tech.mamontov.blackradish.core.plugins

import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunStarted
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import tech.mamontov.blackradish.core.aspects.ReplacementAspect
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import java.io.File
import java.net.URISyntaxException

@Suppress("UNUSED_PARAMETER")
class ConfigurationLoader : Logged, ConcurrentEventListener {
    companion object {
        fun load() {
            try {
                val files: ArrayList<String> = arrayListOf("common", "settings")
                try {
                    files.add(ReplacementAspect.replace("\${env}"))
                } catch (_: AssertionError) {
                }

                this.load(files, ConfigurationProperty::add)
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e.message, e)
            }

            ConfigurationProperty.validate()
        }

        fun load(files: ArrayList<String>, add: (FileBasedConfiguration) -> Unit) {
            val params = Parameters()

            files.forEach { file: String ->
                try {
                    val commonConfig = FileBasedConfigurationBuilder<FileBasedConfiguration>(
                        PropertiesConfiguration::class.java,
                    ).configure(
                        params.properties().setFile(this.getFile(file)),
                    )
                    add(commonConfig.configuration)
                } catch (_: IllegalArgumentException) {
                }
            }
        }

        private fun getFile(name: String): File {
            val url = ReplacementAspect::class.java.classLoader.getResource("configuration/$name.properties")

            if (url != null) {
                return File(url.toURI())
            }

            throw IllegalArgumentException(
                "The configuration file $name.properties was not found",
            )
        }
    }

    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(
            TestRunStarted::class.java,
        ) { load() }
    }
}
