package tech.mamontov.blackradish.core.plugins

import io.cucumber.plugin.ConcurrentEventListener
import io.cucumber.plugin.event.EventPublisher
import io.cucumber.plugin.event.TestRunStarted
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import tech.mamontov.blackradish.core.aspects.ReplacementAspect
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.storages.ConfigurationStorage
import tech.mamontov.blackradish.core.storages.PropertyStorage
import java.io.File
import java.net.URISyntaxException

/**
 * Configuration loader plugin
 *
 * @author Dmitry Mamontov
 *
 */
@Suppress("UNUSED_PARAMETER")
class Loader : Logged, ConcurrentEventListener {
    companion object {
        private const val CONFIG_LOADED = "_cl"

        /**
         * Static loader
         */
        fun load() {
            if (isLoaded()) {
                return
            }

            try {
                val files: ArrayList<String> = arrayListOf("common", "settings")
                try {
                    files.add(ReplacementAspect.replace("\${env}"))
                } catch (_: AssertionError) {
                }

                this.loadFiles(files, ConfigurationStorage::add)
            } catch (e: URISyntaxException) {
                throw IllegalArgumentException(e.message, e)
            }

            ConfigurationStorage.validateSettings()

            PropertyStorage.set(CONFIG_LOADED, "")
        }

        /**
         * Configuration is loaded
         *
         * @return Boolean
         */
        fun isLoaded(): Boolean {
            return PropertyStorage.get(CONFIG_LOADED) !== null
        }

        /**
         * Load configuration files
         *
         * @param files ArrayList<String>
         * @param add Function1<FileBasedConfiguration, Unit>
         */
        fun loadFiles(files: ArrayList<String>, add: (FileBasedConfiguration) -> Unit) {
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

        /**
         * Get configuration file by name
         *
         * @param name String
         * @return File
         */
        private fun getFile(name: String): File {
            return Filesystem.that("configuration/$name.properties").absolute().asFile
        }
    }

    /**
     * Subscribe to events
     *
     * @param publisher EventPublisher
     */
    override fun setEventPublisher(publisher: EventPublisher) {
        publisher.registerHandlerFor(
            TestRunStarted::class.java,
        ) { load() }
    }
}
