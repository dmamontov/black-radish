package tech.mamontov.blackradish.core.formats

import tech.mamontov.blackradish.core.interfaces.Format
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Color format
 *
 * @author Dmitry Mamontov
 *
 * @property escapes Array<out ColorEscapes>
 * @constructor
 */
class ColorFormat(private vararg val escapes: ColorEscapes) : Logged, Format {
    /**
     * Format text
     *
     * @param text String
     * @return String
     */
    override fun text(text: String): String {
        val builder = StringBuilder()

        escapes.forEach { escape: ColorEscapes -> escape.appendTo(builder) }

        builder.append(text)

        if (escapes.isNotEmpty()) {
            ColorEscapes.RESET.appendTo(builder)
        }

        return builder.toString()
    }
}
