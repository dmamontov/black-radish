package tech.mamontov.blackradish.ssh.specs

import com.sshtools.client.SshClient
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.properties.ThreadCommandResultProperty
import tech.mamontov.blackradish.command.properties.ThreadTimeoutProperty
import tech.mamontov.blackradish.command.specs.CommandSpec
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.UriHelper
import tech.mamontov.blackradish.ssh.enumerated.AuthType
import tech.mamontov.blackradish.ssh.enumerated.SftpMethod
import tech.mamontov.blackradish.ssh.properties.ThreadSshProperty
import tech.mamontov.blackradish.ssh.utils.SshCommand
import java.io.File
import java.util.concurrent.TimeUnit


abstract class SshSpec : Logged, CommandSpec() {
    fun connect(host: String, port: Int, user: String, auth: AuthType, token: String, passphrase: String?) {
        if (ThreadSshProperty.get() !== null) {
            Assertions.fail<Any>("SSH connection already open")
        }

        var connectionHost = host
        var connectionPort = port

        if (host.contains(":")) {
            val parts = host.split(":")
            if (parts.size == 2) {
                connectionHost = parts[0]
                connectionPort = parts[1].toInt()
            }
        }

        val timeout = TimeUnit.SECONDS.toMillis(
            ConfigurationProperty.get(ConfigurationProperty.MODULE_SSH_COMMAND_TIMEOUT, 60).toLong()
        )

        try {
            val ssh: SshClient = when (auth) {
                AuthType.KEY -> {
                    val file = File(UriHelper.uri(token).path)
                    if (passphrase !== null) {
                        SshClient(connectionHost, connectionPort, user, timeout, file, passphrase)
                    } else {
                        SshClient(connectionHost, connectionPort, user, timeout, file)
                    }
                }

                else -> SshClient(connectionHost, connectionPort, user, timeout, token.toCharArray())
            }

            ThreadSshProperty.set(ssh)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun disconnect() {
        this.check()

        ThreadSshProperty.close()
    }

    override fun run(command: String) {
        this.check()

        var commandResult: CommandResult? = null

        try {
            var ssh = SshCommand(command)

            ssh.waitFor(ThreadTimeoutProperty.get())

            if (!ssh.exited()) {
                ssh.destroy()

                Assertions.fail<Any>("Timout error")
            }

            commandResult = CommandResult(ssh.exitCode(), ssh.read())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        if (commandResult !== null) {
            ThreadCommandResultProperty.set(commandResult)
            this.attach(commandResult)
        }
    }

    override fun runInBackground(command: String) {
        this.check()

        val ssh = try {
            SshCommand(command)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } as SshCommand

        this.inBackground(ssh)
    }

    override fun tail(timeout: Int, command: String, search: String) {
        this.check()

        val start = System.currentTimeMillis()
        val millis = TimeUnit.SECONDS.toMillis(timeout.toLong() + 1)

        var commandResult: CommandResult? = null

        try {
            var content = ""
            var sshCommand = SshCommand(command)

            loop@ while ((System.currentTimeMillis() - start) <= millis) {
                try {
                    if (sshCommand.exited()) {
                        content += sshCommand.safeRead()

                        sshCommand = SshCommand(command)
                    }

                    val temp = sshCommand.safeRead()
                    if (temp.isEmpty()) {
                        continue
                    }

                    Assertions.assertThat(temp).contains(search)

                    sshCommand.destroy()

                    commandResult = CommandResult(
                        sshCommand.exitCode(),
                        sshCommand.trim(content + temp)
                    )

                    break@loop
                } catch (_: AssertionError) {
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        if (commandResult === null) {
            Assertions.fail<Any>("Within '$timeout' seconds the result of the command execution does not contain '$search'")
        }

        ThreadCommandResultProperty.set(commandResult!!)
        this.attach(commandResult)
    }

    open fun upload(source: String, target: String) {
        this.check()

        this.sftp(
            UriHelper.uri(source).path,
            target,
            SftpMethod.UPLOAD,
            ConfigurationProperty.get(ConfigurationProperty.MODULE_SSH_UPLOAD_TIMEOUT, 15000),
        )
    }

    open fun download(source: String, target: String) {
        this.check()

        this.sftp(
            source,
            UriHelper.uri(target, true).path,
            SftpMethod.DOWNLOAD,
            ConfigurationProperty.get(ConfigurationProperty.MODULE_SSH_DOWNLOAD_TIMEOUT, 15000),
        )
    }

    private fun sftp(source: String, target: String, method: SftpMethod, timeout: Int) {
        val timeoutMillis = TimeUnit.SECONDS.toMillis(timeout.toLong())

        try {
            val ssh = ThreadSshProperty.get()!!

            when (method) {
                SftpMethod.UPLOAD -> ssh.putFile(File(source), target, timeoutMillis)
                SftpMethod.DOWNLOAD -> ssh.getFile(source, File(target), timeoutMillis)
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    private fun check() {
        if (ThreadSshProperty.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }
    }
}
