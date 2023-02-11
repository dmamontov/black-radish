package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Locale

/**
 * Uppercase lookup for replacement
 *
 * @author Dmitry Mamontov
 */
class UpperLookup : Logged, StringLookup {
    /**
     * Value to uppercase
     *
     * @param value String
     * @return String
     */
    override fun lookup(value: String): String {
        return value.uppercase(Locale.getDefault())
    }
}
