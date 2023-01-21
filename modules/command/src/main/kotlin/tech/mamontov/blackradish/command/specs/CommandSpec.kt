package tech.mamontov.blackradish.command.specs

import com.google.gson.Gson
import io.cucumber.docstring.DocString
import io.qameta.allure.Allure
import org.apache.commons.io.output.ByteArrayOutputStream
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.properties.ThreadCommandResultProperty
import tech.mamontov.blackradish.command.properties.ThreadTimeoutProperty
import tech.mamontov.blackradish.command.utils.Command
import tech.mamontov.blackradish.command.utils.CommandThreadFuture
import tech.mamontov.blackradish.command.utils.LocalCommand
import tech.mamontov.blackradish.core.properties.ThreadFutureProperty
import tech.mamontov.blackradish.core.properties.ThreadPoolProperty
import tech.mamontov.blackradish.core.specs.BaseSpec
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.ThreadFuture
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

abstract class CommandSpec : Logged, BaseSpec() {
    open fun timeout(seconds: Long) {
        ThreadTimeoutProperty.set(seconds)
    }

    open fun run(command: String) {
        var commandResult: CommandResult? = null

        try {
            val local = LocalCommand(command)

            local.waitFor(ThreadTimeoutProperty.get())

            if (!local.exited()) {
                local.destroy()

                Assertions.fail<Any>("Timout error")
            }

            commandResult = CommandResult(local.exitCode(), local.read())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        if (commandResult !== null) {
            ThreadCommandResultProperty.set(commandResult)
            this.attach(commandResult)
        }

        ThreadTimeoutProperty.reset()
    }

    open fun runInBackground(command: String) {
        val local = try {
            LocalCommand(command, ByteArrayOutputStream())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } as LocalCommand

        this.inBackground(local)
    }

    open fun saveBackgroundId(variable: String) {
        val future: ThreadFuture? = ThreadFutureProperty.last()

        Assertions.assertThat(future).`as`("Background command not running").isNotNull

        super.save(future!!.uuid.toString(), variable)
    }

    open fun closeBackground(variable: String?) {
        val future: CommandThreadFuture? = if (variable === null) {
            ThreadFutureProperty.last()
        } else {
            ThreadFutureProperty.get(variable)
        } as CommandThreadFuture?

        Assertions.assertThat(future).`as`("Background command not running").isNotNull

        var commandResult: CommandResult? = null

        try {
            future!!.destroy()

            commandResult = CommandResult(
                future.command.exitCode(),
                future.command.read()
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        ThreadFutureProperty.remove(future!!.uuid.toString())

        if (commandResult !== null) {
            ThreadCommandResultProperty.set(commandResult)
            this.attach(commandResult)
        }
    }

    open fun tail(timeout: Int, command: String, search: String) {
        val start = System.currentTimeMillis()
        val millis = TimeUnit.SECONDS.toMillis(timeout.toLong() + 1)

        var commandResult: CommandResult? = null

        try {
            var content = ""

            var local = try {
                LocalCommand(command)
            } catch (e: Exception) {
                Assertions.fail<Any>(e.message, e)
            } as LocalCommand

            loop@ while ((System.currentTimeMillis() - start) <= millis) {
                try {
                    if (local.exited()) {
                        local = LocalCommand(command)
                    }

                    val temp = local.safeRead()
                    if (temp.isEmpty()) {
                        continue
                    }

                    content += temp

                    Assertions.assertThat(content).contains(search)

                    local.destroy()

                    commandResult = CommandResult(local.exitCode(), local.trim(content))

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

    open fun save(variable: String) {
        super.save(this.getResult().content, variable)
    }

    open fun exitCode(code: Long) {
        Assertions.assertThat(this.getResult().code).isEqualTo(code)
    }

    open fun check(content: DocString) {
        Assertions.assertThat(this.getResult().content).isEqualTo(content.content)
    }

    protected fun inBackground(command: Command) {
        val pool: ExecutorService = ThreadPoolProperty.create()

        val future = pool.submit {
            while (!command.exited()) {
                command.safeRead()
            }
        }

        val uuid = UUID.randomUUID()
        ThreadFutureProperty.add(uuid.toString(), CommandThreadFuture(uuid, future, command))
    }

    protected fun attach(result: CommandResult) {
        Allure.addAttachment("pipe.json", "application/json", Gson().toJson(result).toString())
    }

    private fun getResult(): CommandResult {
        val commandResult = ThreadCommandResultProperty.get()

        if (commandResult === null) {
            Assertions.fail<Any>("The result of the command execution was not found")
        }

        return commandResult!!
    }
}
