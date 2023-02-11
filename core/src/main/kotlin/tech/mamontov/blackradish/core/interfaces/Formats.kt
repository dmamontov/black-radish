package tech.mamontov.blackradish.core.interfaces

import tech.mamontov.blackradish.core.formats.AnsiFormats
import tech.mamontov.blackradish.core.formats.MonochromeFormats

/**
 * Formats interface
 *
 * @author Dmitry Mamontov
 */
interface Formats {
    /**
     * Get format by key
     *
     * @param key String
     * @return Format
     */
    fun get(key: String): Format

    /**
     * Up
     *
     * @param count Int
     * @return String
     */
    fun up(count: Int): String

    companion object {
        /**
         * Get monochrome formats
         *
         * @return Formats
         */
        fun monochrome(): Formats {
            return MonochromeFormats()
        }

        /**
         * Get ansi formats
         *
         * @return Formats
         */
        fun ansi(): Formats {
            return AnsiFormats()
        }
    }
}
