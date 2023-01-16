package tech.mamontov.blackradish.core.utils.formatter.formats

import tech.mamontov.blackradish.core.utils.Logged

class MonochromeFormat : Logged, Format {
    override fun text(text: String): String {
        return text
    }
}
