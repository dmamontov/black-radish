package tech.mamontov.blackradish.core.formats

import tech.mamontov.blackradish.core.interfaces.Format
import tech.mamontov.blackradish.core.interfaces.Formats
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Monochrome formats
 *
 * @author Dmitry Mamontov
 */
class MonochromeFormats : Logged, Formats {
    /**
     * Get format by key
     *
     * @param key String
     * @return Format
     */
    override fun get(key: String): Format {
        return Format.monochrome()
    }

    /**
     * Up
     *
     * @param count Int
     * @return String
     */
    override fun up(count: Int): String {
        return ""
    }
}
