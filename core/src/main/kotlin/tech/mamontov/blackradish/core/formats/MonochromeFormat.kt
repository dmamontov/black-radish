package tech.mamontov.blackradish.core.formats

import tech.mamontov.blackradish.core.interfaces.Format
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Monochrome format
 *
 * @author Dmitry Mamontov
 */
class MonochromeFormat : Logged, Format {
    /**
     * Format text
     *
     * @param text String
     * @return String
     */
    override fun text(text: String): String {
        return text
    }
}
