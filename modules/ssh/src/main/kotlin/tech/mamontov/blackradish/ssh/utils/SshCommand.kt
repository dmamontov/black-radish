package tech.mamontov.blackradish.ssh.utils

import com.sshtools.client.SessionChannelNG
import com.sshtools.client.tasks.AbstractCommandTask
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.utils.LocalCommand
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.ssh.properties.ThreadSshProperty
import java.util.concurrent.TimeUnit

class SshCommand(command: String) : Logged, LocalCommand() {
    private val task: AbstractCommandTask
    private val buffer: StringBuffer

    init {
        if (ThreadSshProperty.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }

        val buffer = StringBuffer()

        val ssh = ThreadSshProperty.get()!!
        val task = object : AbstractCommandTask(ssh.connection, command) {
            override fun beforeExecuteCommand(session: SessionChannelNG) {
                session.addEventListener(OutputListener(buffer))
            }

            override fun onOpenSession(session: SessionChannelNG) {
                while (session.inputStream.read() > -1);
            }
        }
        ssh.addTask(task)

        this.buffer = buffer
        this.task = task
    }

    override fun read(): String {
        return this.trim(this.safeRead())
    }

    override fun safeRead(): String {
        return buffer.toString()
    }

    override fun waitFor(seconds: Long) {
        this.task.waitFor(TimeUnit.SECONDS.toMillis(seconds))
    }

    override fun exitCode(): Int {
        if (this.task.exitCode == -2147483648) {
            return 143
        }

        return this.task.exitCode
    }

    override fun destroy() {
        if (!exited()) {
            this.task.waitFor(100)

            this.task.close()

            this.task.waitForever()
        }
    }

    override fun exited(): Boolean {
        return this.task.isDone
    }
}