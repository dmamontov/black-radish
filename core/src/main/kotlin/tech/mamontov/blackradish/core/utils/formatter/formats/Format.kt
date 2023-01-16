package tech.mamontov.blackradish.core.utils.formatter.formats

interface Format {
    fun text(text: String): String

    companion object {
        fun color(vararg escapes: Escapes): Format {
            return ColorFormat(*escapes)
        }

        fun monochrome(): Format {
            return MonochromeFormat()
        }
    }
}
