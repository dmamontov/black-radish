package tech.mamontov.blackradish.command.commands

import com.sshtools.forker.client.ForkerBuilder
import com.sshtools.forker.client.ForkerProcess
import com.sshtools.forker.client.OSCommand
import org.apache.commons.io.output.ByteArrayOutputStream
import tech.mamontov.blackradish.command.interfaces.Command
import tech.mamontov.blackradish.core.interfaces.Logged
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Local command
 *
 * @author Dmitry Mamontov
 *
 * @property process ForkerProcess?
 * @property buffer ByteArrayOutputStream?
 */
open class LocalCommand : Logged, Command {
    private var process: ForkerProcess? = null
    private var buffer: ByteArrayOutputStream? = null

    /**
     * Empty constructor.
     *
     * @constructor
     */
    constructor() {
    }

    /**
     * Constructor with string command.
     *
     * @param command String
     * @constructor
     */
    constructor(command: String) {
        val builder = ForkerBuilder(prepare(command))
        builder.redirectErrorStream(true)

        this.process = builder.start()
    }

    /**
     * Constructor with string command and output buffer.
     *
     * @param command String
     * @param buffer ByteArrayOutputStream
     * @constructor
     */
    constructor(command: String, buffer: ByteArrayOutputStream) : this(command) {
        this.buffer = buffer
    }

    /**
     * Read command result.
     *
     * @return String
     */
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

    /**
     * Get exit code.
     *
     * @return Int
     */
    override fun exitCode(): Int {
        return this.process!!.exitValue()
    }

    /**
     * Wait command.
     *
     * @param seconds Long
     */
    open fun waitFor(seconds: Long) {
        this.process!!.waitFor(seconds, TimeUnit.SECONDS)
    }

    /**
     * Non-blocking read command result.
     *
     * @return String
     */
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

    /**
     * Terminate running command.
     */
    override fun terminate() {
        if (!isExited()) {
            this.process!!.waitFor(100, TimeUnit.MILLISECONDS)

            this.process!!.destroyForcibly()

            this.process!!.waitFor()
        }
    }

    /**
     * Is exited command.
     *
     * @return Boolean
     */
    override fun isExited(): Boolean {
        return try {
            this.process!!.exitValue()
            true
        } catch (e: IllegalThreadStateException) {
            false
        }
    }

    /**
     * Trim command result.
     *
     * @param content String
     * @return String
     */
    fun trim(content: String): String {
        return content.trim(' ', '"', '\r', '\n', '\t')
            .replace("\r\n", System.lineSeparator())
    }

    /**
     * Prepare string command.
     *
     * @param command String
     * @return ArrayList<String>
     */
    private fun prepare(command: String): ArrayList<String> {
        listOf("bash", "sh").forEach {
            if (OSCommand.hasCommand(it)) {
                return arrayListOf(it, "-c", command)
            }
        }

        return ArrayList(command.split("\\s".toRegex()))
    }
}
