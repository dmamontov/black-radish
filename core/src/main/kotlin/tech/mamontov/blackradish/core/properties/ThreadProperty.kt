package tech.mamontov.blackradish.core.properties

import tech.mamontov.blackradish.core.utils.Logged
import java.util.Properties

class ThreadProperty : Logged {
    companion object {
        private val properties: ThreadLocal<Properties> = object : ThreadLocal<Properties>() {
            override fun initialValue(): Properties {
                return Properties()
            }
        }

        fun set(key: String, value: String?) {
            if (value != null) {
                properties.get().setProperty(key, value)
            }
        }

        fun get(key: String): String? {
            return properties.get().getProperty(key)
        }

        fun get(key: String, default: String): String? {
            return properties.get().getProperty(key, default)
        }
    }
}
