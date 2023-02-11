package tech.mamontov.blackradish.core.interfaces

import tech.mamontov.blackradish.core.formats.ColorEscapes
import tech.mamontov.blackradish.core.formats.ColorFormat
import tech.mamontov.blackradish.core.formats.MonochromeFormat

/**
 * Format interface
 *
 * @author Dmitry Mamontov
 */
interface Format {
    /**
     * Format text
     *
     * @param text String
     * @return String
     */
    fun text(text: String): String

    companion object {
        /**
         * Get color format
         *
         * @param escapes Array<out ColorEscapes>
         * @return Format
         */
        fun color(vararg escapes: ColorEscapes): Format {
            return ColorFormat(*escapes)
        }

        /**
         * Get monochrome format
         *
         * @return Format
         */
        fun monochrome(): Format {
            return MonochromeFormat()
        }
    }
}
