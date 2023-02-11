package tech.mamontov.blackradish.ssh.commands

import com.sshtools.client.SessionChannelNG
import com.sshtools.client.tasks.AbstractCommandTask
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.commands.LocalCommand
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.ssh.listeners.OutputListener
import tech.mamontov.blackradish.ssh.storages.SshClientStorage
import java.util.concurrent.TimeUnit

/**
 * Ssh command
 *
 * @property task AbstractCommandTask
 * @property buffer StringBuffer
 * @constructor
 */
class SshCommand(command: String) : Logged, LocalCommand() {
    private val task: AbstractCommandTask
    private val buffer: StringBuffer

    init {
        if (SshClientStorage.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }

        val buffer = StringBuffer()

        val client = SshClientStorage.get()!!
        val task = object : AbstractCommandTask(client.connection, command) {
            override fun beforeExecuteCommand(session: SessionChannelNG) {
                session.addEventListener(OutputListener(buffer))
            }

            override fun onOpenSession(session: SessionChannelNG) {
                while (session.inputStream.read() > -1);
            }
        }
        client.addTask(task)

        this.buffer = buffer
        this.task = task
    }

    /**
     * Read command result.
     *
     * @return String
     */
    override fun read(): String {
        return this.trim(this.safeRead())
    }

    /**
     * Non-blocking read command result.
     *
     * @return String
     */
    override fun safeRead(): String {
        return buffer.toString()
    }

    /**
     * Wait command.
     *
     * @param seconds Long
     */
    override fun waitFor(seconds: Long) {
        this.task.waitFor(TimeUnit.SECONDS.toMillis(seconds))
    }

    /**
     * Get exit code.
     *
     * @return Int
     */
    override fun exitCode(): Int {
        if (this.task.exitCode == -2147483648) {
            return 143
        }

        return this.task.exitCode
    }

    /**
     * Terminate running command.
     */
    override fun terminate() {
        if (!isExited()) {
            this.task.waitFor(100)

            this.task.close()

            this.task.waitForever()
        }
    }

    /**
     * Is exited command.
     *
     * @return Boolean
     */
    override fun isExited(): Boolean {
        return this.task.isDone
    }
}
