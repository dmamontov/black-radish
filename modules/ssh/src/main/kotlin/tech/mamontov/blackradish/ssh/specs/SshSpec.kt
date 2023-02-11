package tech.mamontov.blackradish.ssh.specs

import com.sshtools.client.SshClient
import io.qameta.allure.Allure
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.output.ByteArrayOutputStream
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.commands.LocalCommand
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.storages.CommandStorage
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.storages.ConfigurationStorage
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.ssh.commands.SshCommand
import tech.mamontov.blackradish.ssh.enumerated.AuthType
import tech.mamontov.blackradish.ssh.enumerated.SftpMethod
import tech.mamontov.blackradish.ssh.storages.SshClientStorage
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Ssh spec
 *
 * @author Dmitry Mamontov
 */
open class SshSpec : Logged {
    /**
     * Opening an ssh connection.
     *
     * @param host String
     * @param port Int
     * @param user String
     * @param auth AuthType
     * @param token String
     * @param passphrase String?
     */
    fun connect(host: String, port: Int, user: String, auth: AuthType, token: String, passphrase: String?) {
        if (SshClientStorage.get() !== null) {
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
            ConfigurationStorage.get(ConfigurationStorage.MODULE_SSH_COMMAND_TIMEOUT, 60).toLong(),
        )

        try {
            val ssh: SshClient = when (auth) {
                AuthType.KEY -> {
                    val file = Filesystem.that(token).absolute().asFile
                    if (passphrase !== null) {
                        SshClient(connectionHost, connectionPort, user, timeout, file, passphrase)
                    } else {
                        SshClient(connectionHost, connectionPort, user, timeout, file)
                    }
                }

                else -> SshClient(connectionHost, connectionPort, user, timeout, token.toCharArray())
            }

            SshClientStorage.set(ssh)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Closing the ssh connection.
     */
    open fun disconnect() {
        this.check()

        SshClientStorage.close()
    }

    /**
     * Run ssh command.
     *
     * @param command String
     */
    open fun run(command: String) {
        this.check()

        var sshCommand: SshCommand? = null
        var commandResult: CommandResult? = null

        try {
            sshCommand = SshCommand(command)

            sshCommand.waitFor(CommandStorage.getTimeout())

            if (!sshCommand.isExited()) {
                sshCommand.terminate()

                Assertions.fail<Any>("Timout error")
            }

            commandResult = CommandResult(sshCommand.exitCode(), sshCommand.read())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            if (sshCommand === null || commandResult === null) {
                Assertions.fail<Any>("Failed to get command result.")
            }

            CommandStorage.set(commandResult!!)
            sshCommand!!.attach(commandResult)

            val result = ConvertedResult(commandResult.content)
            ConvertedResultStorage.set(result)
            if (result.json !== null) {
                sshCommand.attach(result.json!!)
            }
        }
    }

    /**
     * Run an ssh command in the background.
     *
     * @param command String
     */
    open fun runInBackground(command: String) {
        this.check()

        try {
            SshCommand(command).toBackground()
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Run the ssh command until the content appears within the specified time.
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    open fun search(timeout: Int, command: String, search: String) {
        this.check()

        val start = System.currentTimeMillis()
        val millis = TimeUnit.SECONDS.toMillis(timeout.toLong() + 1)

        var sshCommand: SshCommand? = null
        var commandResult: CommandResult? = null

        try {
            var content = ""
            sshCommand = SshCommand(command)

            while ((System.currentTimeMillis() - start) <= millis) {
                try {
                    if (sshCommand!!.isExited()) {
                        content += sshCommand.safeRead()

                        sshCommand = SshCommand(command)
                    }

                    val temp = sshCommand.safeRead()
                    if (temp.isEmpty()) {
                        continue
                    }

                    Assertions.assertThat(temp).contains(search)

                    sshCommand.terminate()

                    commandResult = CommandResult(
                        sshCommand.exitCode(),
                        sshCommand.trim(content + temp),
                    )

                    break
                } catch (_: AssertionError) {
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            if (commandResult === null) {
                Assertions.fail<Any>(
                    "Within '$timeout' seconds the result of the command execution does not contain '$search'",
                )
            }

            CommandStorage.set(commandResult!!)
            sshCommand!!.attach(commandResult)

            val result = ConvertedResult(commandResult.content)
            ConvertedResultStorage.set(result)
            if (result.json !== null) {
                sshCommand.attach(result.json!!)
            }
        }
    }

    /**
     * File upload via sftp.
     *
     * @param source String
     * @param target String
     */
    open fun upload(source: String, target: String) {
        this.check()

        this.sftp(
            Filesystem.that(source).absolute().path,
            target,
            SftpMethod.UPLOAD,
            ConfigurationStorage.get(ConfigurationStorage.MODULE_SSH_UPLOAD_TIMEOUT, 15000),
        )
    }

    /**
     * Downloading a file via sftp.
     * @param source String
     * @param target String
     */
    open fun download(source: String, target: String) {
        this.check()

        this.sftp(
            source,
            Filesystem.that(target).file().path,
            SftpMethod.DOWNLOAD,
            ConfigurationStorage.get(ConfigurationStorage.MODULE_SSH_DOWNLOAD_TIMEOUT, 15000),
        )
    }

    /**
     * Downloading or uploading a file via sftp.
     *
     * @param source String
     * @param target String
     * @param method SftpMethod
     * @param timeout Int
     */
    private fun sftp(source: String, target: String, method: SftpMethod, timeout: Int) {
        val timeoutMillis = TimeUnit.SECONDS.toMillis(timeout.toLong())

        try {
            val ssh = SshClientStorage.get()!!
            var file: File? = null

            when (method) {
                SftpMethod.UPLOAD -> {
                    file = File(source)
                    ssh.putFile(file, target, timeoutMillis)
                }
                SftpMethod.DOWNLOAD -> {
                    file = File(target)
                    ssh.getFile(source, file, timeoutMillis)
                }
            }

            Allure.attachment(FilenameUtils.getName(file.path), file.inputStream())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Check that an ssh connection is open.
     */
    private fun check() {
        if (SshClientStorage.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }
    }
}
