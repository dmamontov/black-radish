package tech.mamontov.blackradish.core.formats

import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Color escapes
 *
 * @author Dmitry Mamontov
 *
 * @property value String
 * @constructor
 */
class ColorEscapes private constructor(private val value: String) : Logged {
    companion object {
        val RESET = color(0)
        val BLACK = color(30)
        val RED = color(31)
        val GREEN = color(32)
        val YELLOW = color(33)
        val BLUE = color(34)
        val MAGENTA = color(35)
        val CYAN = color(36)
        val WHITE = color(37)
        val DEFAULT = color(9)
        val GREY = color(90)
        val INTENSITY_BOLD = color(1)
        val UNDERLINE = color(4)

        private const val ESC = 27.toChar()
        private const val BRACKET = '['

        /**
         * Get color by code
         *
         * @param code Int
         * @return ColorEscapes
         */
        private fun color(code: Int): ColorEscapes {
            return ColorEscapes(code.toString() + "m")
        }

        /**
         * Up color
         *
         * @param count Int
         * @return ColorEscapes
         */
        fun up(count: Int): ColorEscapes {
            return ColorEscapes(count.toString() + "A")
        }
    }

    /**
     * Append value to string
     *
     * @param builder StringBuilder
     */
    fun appendTo(builder: StringBuilder) {
        builder.append(ESC).append(BRACKET).append(value)
    }

    /**
     * To string
     *
     * @return String
     */
    override fun toString(): String {
        val builder = StringBuilder()
        appendTo(builder)

        return builder.toString()
    }
}
