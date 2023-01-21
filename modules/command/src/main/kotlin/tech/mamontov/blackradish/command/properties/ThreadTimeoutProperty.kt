package tech.mamontov.blackradish.command.properties

import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.core.utils.Logged

class ThreadTimeoutProperty : Logged {
    companion object {
        private val defaultTimeout: Long = ConfigurationProperty.get(ConfigurationProperty.MODULE_COMMAND_TIMEOUT, 60).toLong()
        private val timeout: ThreadLocal<Long> = object : ThreadLocal<Long>() {
            override fun initialValue(): Long {
                return defaultTimeout
            }
        }

        fun set(seconds: Long) {
            timeout.set(seconds)
        }

        fun reset() {
            timeout.set(defaultTimeout)
        }

        fun get(): Long {
            return timeout.get()
        }
    }
}
