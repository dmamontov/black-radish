package tech.mamontov.blackradish.core.utils.property

import org.apache.commons.configuration2.CombinedConfiguration
import org.apache.commons.configuration2.FileBasedConfiguration
import org.apache.commons.configuration2.tree.OverrideCombiner
import tech.mamontov.blackradish.core.utils.Logged

class Configuration : Logged {
    companion object {
        const val ASPECT_INCLUDE_DEPTH = "aspect.include.depth"
        const val DEBUG_SHOW_TRACE = "debug.show.trace"
        const val DEBUG_SHOW_STACKTRACE = "debug.show.stacktrace"

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
    }
}
