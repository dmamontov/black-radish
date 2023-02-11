package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Locale

/**
 * Lowercase lookup for replacement
 *
 * @author Dmitry Mamontov
 */
class LowerLookup : Logged, StringLookup {
    /**
     * Value to lowercase
     *
     * @param value String
     * @return String
     */
    override fun lookup(value: String): String {
        return value.lowercase(Locale.getDefault())
    }
}
