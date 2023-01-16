package tech.mamontov.blackradish.core.utils.formatter.formats

interface Formats {
    fun get(key: String): Format

    fun up(count: Int): String

    companion object {
        fun monochrome(): Formats {
            return MonochromeFormats()
        }

        fun ansi(): Formats {
            return AnsiFormats()
        }
    }
}
