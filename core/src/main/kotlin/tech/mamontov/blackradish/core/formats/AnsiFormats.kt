package tech.mamontov.blackradish.core.formats

import tech.mamontov.blackradish.core.interfaces.Format
import tech.mamontov.blackradish.core.interfaces.Formats
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Ansi formats
 *
 * @author Dmitry Mamontov
 *
 * @property formats Map<String, Format>
 */
class AnsiFormats : Logged, Formats {
    private val formats: Map<String, Format> = object : HashMap<String, Format>() {
        init {
            put("undefined", Format.color(ColorEscapes.YELLOW))
            put("undefined_arg", Format.color(ColorEscapes.YELLOW, ColorEscapes.INTENSITY_BOLD))
            put("unused", Format.color(ColorEscapes.YELLOW))
            put("unused_arg", Format.color(ColorEscapes.YELLOW, ColorEscapes.INTENSITY_BOLD))
            put("pending", Format.color(ColorEscapes.YELLOW))
            put("pending_arg", Format.color(ColorEscapes.YELLOW, ColorEscapes.INTENSITY_BOLD))
            put("executing", Format.color(ColorEscapes.GREY))
            put("executing_arg", Format.color(ColorEscapes.GREY, ColorEscapes.INTENSITY_BOLD))
            put("failed", Format.color(ColorEscapes.RED))
            put("failed_arg", Format.color(ColorEscapes.RED, ColorEscapes.INTENSITY_BOLD))
            put("ambiguous", Format.color(ColorEscapes.RED))
            put("ambiguous_arg", Format.color(ColorEscapes.RED, ColorEscapes.INTENSITY_BOLD))
            put("passed", Format.color(ColorEscapes.GREEN))
            put("passed_arg", Format.color(ColorEscapes.GREEN, ColorEscapes.INTENSITY_BOLD))
            put("outline", Format.color(ColorEscapes.CYAN))
            put("outline_arg", Format.color(ColorEscapes.CYAN, ColorEscapes.INTENSITY_BOLD))
            put("skipped", Format.color(ColorEscapes.GREY))
            put("skipped_arg", Format.color(ColorEscapes.GREY, ColorEscapes.INTENSITY_BOLD))
            put("comment", Format.color(ColorEscapes.GREY))
            put("tag", Format.color(ColorEscapes.CYAN))
            put("output", Format.color(ColorEscapes.BLUE))
        }
    }

    /**
     * Get format by key
     *
     * @param key String
     * @return Format
     */
    override fun get(key: String): Format {
        return formats[key] ?: throw NullPointerException("No format for key $key")
    }

    /**
     * Up
     *
     * @param count Int
     * @return String
     */
    override fun up(count: Int): String {
        return ColorEscapes.up(count).toString()
    }
}
