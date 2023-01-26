package tech.mamontov.blackradish.core.utils.formatter.formats

import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.utils.formatter.OutputAppendable

class Escapes private constructor(private val value: String) : Logged {
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

        private fun color(code: Int): Escapes {
            return Escapes(code.toString() + "m")
        }

        fun up(count: Int): Escapes {
            return Escapes(count.toString() + "A")
        }
    }

    fun appendTo(appendable: OutputAppendable) {
        appendable.append(ESC).append(BRACKET).append(value)
    }

    fun appendTo(builder: StringBuilder) {
        builder.append(ESC).append(BRACKET).append(value)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        appendTo(builder)

        return builder.toString()
    }
}
