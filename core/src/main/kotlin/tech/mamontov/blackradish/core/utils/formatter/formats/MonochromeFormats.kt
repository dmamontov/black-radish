package tech.mamontov.blackradish.core.utils.formatter.formats

import tech.mamontov.blackradish.core.utils.Logged

class MonochromeFormats : Logged, Formats {
    override fun get(key: String): Format {
        return Format.monochrome()
    }

    override fun up(count: Int): String {
        return ""
    }
}
