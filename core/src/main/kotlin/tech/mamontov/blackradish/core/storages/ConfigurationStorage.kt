package tech.mamontov.blackradish.core.storages

import org.apache.commons.configuration2.CombinedConfiguration
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.ex.ConversionException
import org.apache.commons.configuration2.tree.OverrideCombiner
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Configuration storage
 *
 * @author Dmitry Mamontov
 */
class ConfigurationStorage : Logged {
    companion object {
        const val THREAD_POOL_SHUTDOWN_TIMEOUT = "thread.pool.shutdown.timeout"

        const val ASPECT_INCLUDE_DEPTH = "aspect.include.depth"

        const val DEBUG_SHOW_TRACE = "debug.show.trace"
        const val DEBUG_SHOW_STACKTRACE = "debug.show.stacktrace"

        const val MODULE_COMMAND_TIMEOUT = "module.command.timeout"

        const val MODULE_SSH_COMMAND_TIMEOUT = "module.ssh.command.timeout"
        const val MODULE_SSH_UPLOAD_TIMEOUT = "module.ssh.upload.timeout"
        const val MODULE_SSH_DOWNLOAD_TIMEOUT = "module.ssh.download.timeout"

        private const val ERROR_MESSAGE = "settings.properties: %s"
        private val SETTINGS = listOf(
            THREAD_POOL_SHUTDOWN_TIMEOUT,

            ASPECT_INCLUDE_DEPTH,

            DEBUG_SHOW_TRACE,
            DEBUG_SHOW_STACKTRACE,

            MODULE_COMMAND_TIMEOUT,

            MODULE_SSH_COMMAND_TIMEOUT,
            MODULE_SSH_UPLOAD_TIMEOUT,
            MODULE_SSH_DOWNLOAD_TIMEOUT,
        )

        private val configuration: CombinedConfiguration = CombinedConfiguration(OverrideCombiner())

        /**
         * Add configuration
         *
         * @param configuration FileBasedConfiguration
         */
        fun add(configuration: FileBasedConfiguration) {
            Companion.configuration.addConfiguration(configuration)
        }

        /**
         * Get string configuration by key
         *
         * @param key String
         * @param default String?
         * @return String?
         */
        fun get(key: String, default: String? = null): String? {
            return configuration.getString(key, default)
        }

        /**
         * Get int configuration by key
         *
         * @param key String
         * @param default Int
         * @return Int
         */
        fun get(key: String, default: Int): Int {
            return configuration.getInt(key, default)
        }

        /**
         * Get boolean configuration by key
         *
         * @param key String
         * @param default Boolean
         * @return Boolean
         */
        fun get(key: String, default: Boolean): Boolean {
            return configuration.getBoolean(key, default)
        }

        /**
         * Validate settings
         */
        fun validateSettings() {
            SETTINGS.forEach { setting: String ->
                try {
                    when (setting) {
                        THREAD_POOL_SHUTDOWN_TIMEOUT -> Assertions.assertThat(
                            get(setting, 10),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(1)

                        ASPECT_INCLUDE_DEPTH -> Assertions.assertThat(
                            get(setting, 10),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(1)

                        DEBUG_SHOW_TRACE -> get(setting, false)

                        DEBUG_SHOW_STACKTRACE -> get(setting, false)

                        MODULE_COMMAND_TIMEOUT -> Assertions.assertThat(
                            get(setting, 60),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(0)

                        MODULE_SSH_COMMAND_TIMEOUT -> Assertions.assertThat(
                            get(setting, 180),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(-1)

                        MODULE_SSH_UPLOAD_TIMEOUT -> Assertions.assertThat(
                            get(setting, 180),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(1)

                        MODULE_SSH_DOWNLOAD_TIMEOUT -> Assertions.assertThat(
                            get(setting, 180),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(1)
                    }
                } catch (e: ConversionException) {
                    Assertions.fail<Any>(e.message, e)
                }
            }
        }
    }
}
