package tech.mamontov.blackradish.core.utils.formatter.formats

import tech.mamontov.blackradish.core.utils.Logged

class ColorFormat(private vararg val escapes: Escapes) : Logged, Format {
    override fun text(text: String): String {
        val builder = StringBuilder()

        escapes.forEach { escape: Escapes -> escape.appendTo(builder) }

        builder.append(text)

        if (escapes.isNotEmpty()) {
            Escapes.RESET.appendTo(builder)
        }

        return builder.toString()
    }
}
