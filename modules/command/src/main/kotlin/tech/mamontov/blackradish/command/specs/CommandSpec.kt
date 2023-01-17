package tech.mamontov.blackradish.command.specs

import com.google.gson.Gson
import io.qameta.allure.Attachment
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.properties.ThreadTimeoutProperty
import tech.mamontov.blackradish.command.properties.ThreadCommandResultProperty
import tech.mamontov.blackradish.core.specs.BaseSpec
import tech.mamontov.blackradish.core.utils.Logged
import java.util.concurrent.TimeUnit

abstract class CommandSpec : Logged, BaseSpec() {
    open fun timeout(seconds: Long) {
        ThreadTimeoutProperty.set(seconds)
    }

    open fun run(command: String): CommandResult? {
        var commandResult: CommandResult? = null

        try {
            val process = ProcessBuilder(*command.split("\\s".toRegex()).toTypedArray())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            process.waitFor(ThreadTimeoutProperty.get(), TimeUnit.SECONDS)

            commandResult = CommandResult(
                process.exitValue(),
                process.inputStream.bufferedReader().readText(),
                process.pid(),
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message)
        }

        if (commandResult !== null) {
            ThreadCommandResultProperty.set(commandResult)
            this.attach(commandResult)
        }

        ThreadTimeoutProperty.reset()

        return commandResult
    }

    open fun tail(timeout: Int, command: String, search: String) {
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

    open fun save(variable: String) {
        super.save(this.getResult().content.trim(' ', '"', '\r', '\n'), variable)
    }

    open fun exitCode(code: Long) {
        Assertions.assertThat(this.getResult().code).isEqualTo(code)
    }

    @Attachment(value = "pipe", type = "application/json")
    protected fun attach(result: CommandResult): ByteArray {
        return Gson().toJson(result).toByteArray()
    }

    private fun getResult(): CommandResult {
        val commandResult = ThreadCommandResultProperty.get()

        if (commandResult === null) {
            Assertions.fail<Any>("The result of the command execution was not found")
        }

        return commandResult!!
    }
}
