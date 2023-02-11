package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.plugins.Loader
import tech.mamontov.blackradish.core.storages.ConfigurationStorage
import tech.mamontov.blackradish.core.storages.PropertyStorage

/**
 * Default lookup for replacement
 *
 * @author Dmitry Mamontov
 */
open class DefaultLookup : Logged, StringLookup {
    /**
     * Get value by key
     *
     * @param key String
     * @return String?
     */
    override fun lookup(key: String): String? {
        if (!Loader.isLoaded() && key != "env") {
            Loader.load()
        }

        return PropertyStorage.get(key) ?: ConfigurationStorage.get(key) ?: System.getProperty(key)
            ?: System.getenv(key)
    }
}
