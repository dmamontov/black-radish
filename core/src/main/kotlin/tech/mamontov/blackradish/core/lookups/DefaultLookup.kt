package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.core.properties.ThreadProperty

open class DefaultLookup : Logged, StringLookup {
    override fun lookup(key: String): String? {
        return ThreadProperty.get(key) ?: ConfigurationProperty.get(key) ?: System.getProperty(key)
    }
}
