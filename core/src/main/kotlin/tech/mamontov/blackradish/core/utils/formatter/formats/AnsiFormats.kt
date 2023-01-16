package tech.mamontov.blackradish.core.utils.formatter.formats

import tech.mamontov.blackradish.core.utils.Logged

class AnsiFormats : Logged, Formats {
    private val formats: Map<String, Format> = object : HashMap<String, Format>() {
        init {
            put("undefined", Format.color(Escapes.YELLOW))
            put("undefined_arg", Format.color(Escapes.YELLOW, Escapes.INTENSITY_BOLD))
            put("unused", Format.color(Escapes.YELLOW))
            put("unused_arg", Format.color(Escapes.YELLOW, Escapes.INTENSITY_BOLD))
            put("pending", Format.color(Escapes.YELLOW))
            put("pending_arg", Format.color(Escapes.YELLOW, Escapes.INTENSITY_BOLD))
            put("executing", Format.color(Escapes.GREY))
            put("executing_arg", Format.color(Escapes.GREY, Escapes.INTENSITY_BOLD))
            put("failed", Format.color(Escapes.RED))
            put("failed_arg", Format.color(Escapes.RED, Escapes.INTENSITY_BOLD))
            put("ambiguous", Format.color(Escapes.RED))
            put("ambiguous_arg", Format.color(Escapes.RED, Escapes.INTENSITY_BOLD))
            put("passed", Format.color(Escapes.GREEN))
            put("passed_arg", Format.color(Escapes.GREEN, Escapes.INTENSITY_BOLD))
            put("outline", Format.color(Escapes.CYAN))
            put("outline_arg", Format.color(Escapes.CYAN, Escapes.INTENSITY_BOLD))
            put("skipped", Format.color(Escapes.GREY))
            put("skipped_arg", Format.color(Escapes.GREY, Escapes.INTENSITY_BOLD))
            put("comment", Format.color(Escapes.GREY))
            put("tag", Format.color(Escapes.CYAN))
            put("output", Format.color(Escapes.BLUE))
        }
    }

    override fun get(key: String): Format {
        return formats[key] ?: throw NullPointerException("No format for key $key")
    }

    override fun up(count: Int): String {
        return Escapes.up(count).toString()
    }
}
