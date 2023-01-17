package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.property.Configuration
import tech.mamontov.blackradish.core.utils.property.ThreadProperty

open class DefaultLookup : Logged, StringLookup {
    override fun lookup(key: String): String? {
        return ThreadProperty.get(key) ?: Configuration.get(key) ?: System.getProperty(key)
    }
}
