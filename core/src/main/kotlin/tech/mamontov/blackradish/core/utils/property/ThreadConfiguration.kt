package tech.mamontov.blackradish.core.utils.property

import org.apache.commons.configuration2.CombinedConfiguration
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.tree.OverrideCombiner
import tech.mamontov.blackradish.core.utils.Logged

class ThreadConfiguration : Logged {
    companion object {
        const val ASPECT_INCLUDE_DEPTH = "aspect.include.depth"
        const val DEBUG_SHOW_TRACE = "debug.show.trace"
        const val DEBUG_SHOW_STACKTRACE = "debug.show.stacktrace"

        private val configuration: ThreadLocal<CombinedConfiguration> = object : ThreadLocal<CombinedConfiguration>() {
            override fun initialValue(): CombinedConfiguration {
                return CombinedConfiguration(OverrideCombiner())
            }
        }

        fun add(configuration: FileBasedConfiguration) {
            Companion.configuration.get().addConfiguration(configuration)
        }

        fun get(key: String, default: String? = null): String? {
            return configuration.get().getString(key, default)
        }

        fun get(key: String, default: Int): Int {
            return configuration.get().getInt(key, default)
        }

        fun get(key: String, default: Boolean): Boolean {
            return configuration.get().getBoolean(key, default)
        }
    }
}
