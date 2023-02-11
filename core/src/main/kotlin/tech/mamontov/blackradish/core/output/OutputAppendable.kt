package tech.mamontov.blackradish.core.output

import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.Closeable
import java.io.Flushable

/**
 * Output appendable
 *
 * @author Dmitry Mamontov
 *
 * @property out Appendable
 * @constructor
 */
open class OutputAppendable(private val out: Appendable) : Logged, Appendable {
    /**
     * Print new line
     *
     * @return OutputAppendable
     */
    fun println(): OutputAppendable {
        return append(System.lineSeparator())
    }

    /**
     * Print string with new line
     *
     * @param csq CharSequence?
     * @return OutputAppendable
     */
    fun println(csq: CharSequence?): OutputAppendable {
        out.append(csq).append(System.lineSeparator())
        flush()

        return this
    }

    /**
     * Append to output
     *
     * @param csq CharSequence
     * @return OutputAppendable
     */
    override fun append(csq: CharSequence): OutputAppendable {
        out.append(csq)
        flush()

        return this
    }

    /**
     * Append to output with start and end
     *
     * @param csq CharSequence
     * @param start Int
     * @param end Int
     * @return OutputAppendable
     */
    override fun append(csq: CharSequence, start: Int, end: Int): OutputAppendable {
        out.append(csq, start, end)
        flush()

        return this
    }

    /**
     * Append char to output
     *
     * @param c Char
     * @return OutputAppendable
     */
    override fun append(c: Char): OutputAppendable {
        out.append(c)
        flush()

        return this
    }

    /**
     * Flush
     */
    private fun flush() {
        if (out is Flushable) {
            (out as Flushable).flush()
        }
    }

    /**
     * Close output
     */
    fun close() {
        flush()

        if (out is Closeable) {
            (out as Closeable).close()
        }
    }
}
