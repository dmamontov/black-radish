package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Locale

class LowerLookup : Logged, StringLookup {
    override fun lookup(key: String): String {
        return key.lowercase(Locale.getDefault())
    }
}
