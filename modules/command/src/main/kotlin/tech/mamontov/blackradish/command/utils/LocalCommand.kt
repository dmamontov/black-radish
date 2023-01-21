package tech.mamontov.blackradish.command.utils

import com.sshtools.forker.client.ForkerBuilder
import com.sshtools.forker.client.ForkerProcess
import com.sshtools.forker.client.OSCommand
import org.apache.commons.io.output.ByteArrayOutputStream
import tech.mamontov.blackradish.core.utils.Logged
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

open class LocalCommand : Logged, Command {
    private var process: ForkerProcess? = null
    private var buffer: ByteArrayOutputStream? = null

    constructor() {
    }

    constructor(command: String) {
        val builder = ForkerBuilder(prepare(command))
        builder.redirectErrorStream(true)

        this.process = builder.start()
    }

    constructor(command: String, buffer: ByteArrayOutputStream) : this(command) {
        this.buffer = buffer
    }

    override fun read(): String {
        var content = ""

        if (this.buffer !== null) {
            content = this.trim(this.buffer!!.toString(Charset.defaultCharset()))

            this.buffer!!.close()

            return content
        }

        if (this.process!!.inputStream.available() > 0) {
            content = this.process!!.inputStream.bufferedReader().readText()
        }

        return this.trim(content)
    }

    override fun exitCode(): Int {
        return this.process!!.exitValue()
    }

    open fun waitFor(seconds: Long) {
        this.process!!.waitFor(seconds, TimeUnit.SECONDS)
    }

    override fun safeRead(): String {
        if (this.process!!.inputStream.available() <= 0) {
            return ""
        }

        var content = ""
        val temp = ByteArray(1024)
        while (this.process!!.inputStream.available() > 0) {
            val i: Int = this.process!!.inputStream.read(temp, 0, 1024)
            if (i < 0) {
                break
            }
            content += String(temp, 0, i)
        }

        if (content.isNotEmpty() && this.buffer !== null) {
            this.buffer!!.write(content.toByteArray())
        }

        return content
    }

    override fun destroy() {
        if (!exited()) {
            this.process!!.waitFor(100, TimeUnit.MILLISECONDS)

            this.process!!.destroyForcibly()

            this.process!!.waitFor()
        }
    }

    override fun exited(): Boolean {
        return try {
            this.process!!.exitValue()
            true
        } catch (e: IllegalThreadStateException) {
            false
        }
    }

    fun trim(content: String): String {
        return content.trim(' ', '"', '\r', '\n', '\t')
            .replace("\r\n", System.lineSeparator())
    }

    private fun prepare(command: String): ArrayList<String> {
        listOf("bash", "sh").forEach {
            if (OSCommand.hasCommand(it)) {
                return arrayListOf(it, "-c", command)
            }
        }

        return ArrayList(command.split("\\s".toRegex()))
    }
}