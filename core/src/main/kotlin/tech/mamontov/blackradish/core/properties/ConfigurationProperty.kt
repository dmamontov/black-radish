package tech.mamontov.blackradish.core.properties

import org.apache.commons.configuration2.CombinedConfiguration
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.ex.ConversionException
import org.apache.commons.configuration2.tree.OverrideCombiner
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.utils.Logged

class ConfigurationProperty : Logged {
    companion object {
        const val ASPECT_INCLUDE_DEPTH = "aspect.include.depth"
        const val DEBUG_SHOW_TRACE = "debug.show.trace"
        const val DEBUG_SHOW_STACKTRACE = "debug.show.stacktrace"
        const val MODULE_COMMAND_TIMEOUT = "module.command.timeout"
        const val MODULE_SSH_CONNECTION_TIMEOUT = "module.ssh.connection.timeout"

        private const val ERROR_MESSAGE = "settings.properties: %s"
        private val SETTINGS = listOf(
            ASPECT_INCLUDE_DEPTH,
            DEBUG_SHOW_TRACE,
            DEBUG_SHOW_STACKTRACE,
            MODULE_COMMAND_TIMEOUT,
        )

        private val configuration: CombinedConfiguration = CombinedConfiguration(OverrideCombiner())

        fun add(configuration: FileBasedConfiguration) {
            Companion.configuration.addConfiguration(configuration)
        }

        fun get(key: String, default: String? = null): String? {
            return configuration.getString(key, default)
        }

        fun get(key: String, default: Int): Int {
            return configuration.getInt(key, default)
        }

        fun get(key: String, default: Boolean): Boolean {
            return configuration.getBoolean(key, default)
        }

        fun validate() {
            SETTINGS.forEach { setting: String ->
                try {
                    when (setting) {
                        ASPECT_INCLUDE_DEPTH -> Assertions.assertThat(
                            get(setting, 10),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(1)

                        DEBUG_SHOW_TRACE -> get(setting, false)

                        DEBUG_SHOW_STACKTRACE -> get(setting, false)

                        MODULE_COMMAND_TIMEOUT -> Assertions.assertThat(
                            get(setting, 60),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(0)

                        MODULE_SSH_CONNECTION_TIMEOUT -> Assertions.assertThat(
                            get(setting, 60),
                        ).`as`(ERROR_MESSAGE, setting).isGreaterThan(-1)
                    }
                } catch (e: ConversionException) {
                    Assertions.fail<Any>(e.message)
                }
            }
        }
    }
}
