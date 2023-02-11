package tech.mamontov.blackradish.core.storages

import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Properties

/**
 * Property storage
 *
 * @author Dmitry Mamontov
 */
class PropertyStorage : Logged {
    companion object {
        private val properties: ThreadLocal<Properties> = object : ThreadLocal<Properties>() {
            override fun initialValue(): Properties {
                return Properties()
            }
        }

        /**
         * Set property value by key
         *
         * @param key String
         * @param value String?
         */
        fun set(key: String, value: String?) {
            if (value != null) {
                properties.get().setProperty(key, value)
            }
        }

        /**
         * Get property by key
         *
         * @param key String
         * @return String?
         */
        fun get(key: String): String? {
            return properties.get().getProperty(key)
        }

        /**
         * Get property by key with default value
         *
         * @param key String
         * @param default String
         * @return String?
         */
        fun get(key: String, default: String): String? {
            return properties.get().getProperty(key, default)
        }

        /**
         * Reset properties
         */
        fun reset() {
            properties.set(Properties())
        }
    }
}
