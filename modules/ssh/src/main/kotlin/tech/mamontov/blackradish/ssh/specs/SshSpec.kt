package tech.mamontov.blackradish.ssh.specs

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.specs.CommandSpec
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.properties.ThreadCommandResultProperty
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.ssh.enumerated.AuthType
import tech.mamontov.blackradish.ssh.utils.SshUserInfo
import tech.mamontov.blackradish.ssh.properties.ThreadSessionProperty
import java.io.File
import java.net.URL

abstract class SshSpec : Logged, CommandSpec() {
    fun connect(host: String, port: Int, user: String, auth: AuthType, token: String) {
        if (ThreadSessionProperty.get() !== null) {
            Assertions.fail<Any>("SSH connection already open")
        }

        val ssh = JSch()

        if (auth === AuthType.KEY) {
            val url: URL? = this::class.java.classLoader.getResource(token)
            if (url === null) {
                Assertions.fail<Any>("Key file: $token does not exist")
            }

            try {
                ssh.addIdentity(File(url!!.toURI()).path)
            } catch (e: Exception) {
                Assertions.fail<Any>(e.message)
            }
        }

        val sshSession: Session = ssh.getSession(user, host, port)
        sshSession.userInfo = SshUserInfo()
        sshSession.setConfig("PreferredAuthentications", "publickey,password");
        sshSession.setConfig("StrictHostKeyChecking", "no")

        if (auth === AuthType.PASSWORD) {
            sshSession.setPassword(token)
        }

        try {
            sshSession.connect(
                ConfigurationProperty.get(ConfigurationProperty.MODULE_SSH_CONNECTION_TIMEOUT, 60)
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message)
        }

        ThreadSessionProperty.set(sshSession)
    }

    open fun disconnect() {
        if (ThreadSessionProperty.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }

        ThreadSessionProperty.close()
    }

    override fun run(command: String): CommandResult? {
        if (ThreadSessionProperty.get() === null) {
            Assertions.fail<Any>("SSH connection not open")
        }

        var commandResult: CommandResult? = null
        try {
            val channel: Channel = ThreadSessionProperty.get()!!.openChannel("exec")
            (channel as ChannelExec).setCommand(
                "set -o pipefail && $command | grep . --color=never; exit \$PIPESTATUS"
            )

            channel.setInputStream(null)
            channel.setErrStream(System.err)
            channel.setPty(true)

            val stream = channel.getInputStream()

            channel.connect()

            var message = ""
            val temp = ByteArray(1024)
            while (!channel.isClosed() || stream.available() > 0) {
                val i: Int = stream.read(temp, 0, 1024)
                if (i < 0) {
                    break
                }
                message += String(temp, 0, i)

                if (!channel.isClosed() && stream.available() <= 0) {
                    this.wait(1)
                }
            }

            commandResult = CommandResult(
                channel.getExitStatus(),
                message
            )

            channel.disconnect()
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message)
        }

        if (commandResult !== null) {
            ThreadCommandResultProperty.set(commandResult)
            this.attach(commandResult)
        }

        return commandResult
    }

    override fun tail(timeout: Int, command: String, search: String) {
        var commandResult: CommandResult? = null

        loop@ for (number in 1..timeout) {
            try {
                commandResult = this.run(command)

                Assertions.assertThat(commandResult!!.content).contains(search)

                break@loop
            } catch (_: AssertionError) {
                super.wait(1)
            }
        }

        if (commandResult === null) {
            Assertions.fail<Any>("Within '$timeout' seconds the result of the command execution does not contain '$search'")
        }

        ThreadCommandResultProperty.set(commandResult!!)
        this.attach(commandResult)
    }
}
