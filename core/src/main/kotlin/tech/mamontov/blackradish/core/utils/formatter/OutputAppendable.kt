package tech.mamontov.blackradish.core.utils.formatter

import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.Closeable
import java.io.Flushable

class OutputAppendable(private val out: Appendable) : Logged, Appendable {
    companion object {
        private const val NL = "\n"
        private const val DIVIDING_LINE = "-------------------------------------------------------"
    }

    fun println(): OutputAppendable {
        return append(NL)
    }

    fun println(csq: CharSequence?): OutputAppendable {
        out.append(csq).append(NL)
        flush()

        return this
    }

    fun dividing(): OutputAppendable {
        out.append(DIVIDING_LINE).append(NL)
        flush()

        return this
    }

    override fun append(csq: CharSequence): OutputAppendable {
        out.append(csq)
        flush()

        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): OutputAppendable {
        out.append(csq, start, end)
        flush()

        return this
    }

    override fun append(c: Char): OutputAppendable {
        out.append(c)
        flush()

        return this
    }

    private fun flush() {
        if (out is Flushable) {
            (out as Flushable).flush()
        }
    }

    fun close() {
        flush()

        if (out is Closeable) {
            (out as Closeable).close()
        }
    }
}
