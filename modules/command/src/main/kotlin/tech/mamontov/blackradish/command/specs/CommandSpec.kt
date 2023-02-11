package tech.mamontov.blackradish.command.specs

import org.apache.commons.io.output.ByteArrayOutputStream
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.command.commands.LocalCommand
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.data.CommandThreadFuture
import tech.mamontov.blackradish.command.storages.CommandStorage
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ThreadFuture
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.core.storages.ThreadFutureStorage
import java.util.concurrent.TimeUnit

/**
 * Command spec
 *
 * @author Dmitry Mamontov
 *
 * @property baseSpec BaseSpec
 */
open class CommandSpec : Logged {
    private val baseSpec = BaseSpec()

    /**
     * Set the maximum command execution time.
     *
     * @param seconds Long
     */
    open fun timeout(seconds: Long) {
        CommandStorage.setTimeout(seconds)
    }

    /**
     * Run command.
     *
     * @param command String
     */
    open fun run(command: String) {
        var localCommand: LocalCommand? = null
        var commandResult: CommandResult? = null

        try {
            localCommand = LocalCommand(command)

            localCommand.waitFor(CommandStorage.getTimeout())

            if (!localCommand.isExited()) {
                localCommand.terminate()

                Assertions.fail<Any>("Timout error")
            }

            commandResult = CommandResult(localCommand.exitCode(), localCommand.read())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            if (localCommand === null || commandResult === null) {
                Assertions.fail<Any>("Failed to get command result.")
            }

            CommandStorage.set(commandResult!!)
            localCommand!!.attach(commandResult)

            val result = ConvertedResult(commandResult.content)
            ConvertedResultStorage.set(result)
            if (result.json !== null) {
                localCommand.attach(result.json!!)
            }
        }
    }

    /**
     * Run the command until the content appears within the specified time.
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    open fun search(timeout: Int, command: String, search: String) {
        val start = System.currentTimeMillis()
        val millis = TimeUnit.SECONDS.toMillis(timeout.toLong() + 1)

        var localCommand: LocalCommand? = null
        var commandResult: CommandResult? = null

        try {
            var content = ""

            localCommand = try {
                LocalCommand(command)
            } catch (e: Exception) {
                Assertions.fail<Any>(e.message, e)
            } as LocalCommand

            while ((System.currentTimeMillis() - start) <= millis) {
                try {
                    if (localCommand!!.isExited()) {
                        localCommand = LocalCommand(command)
                    }

                    val temp = localCommand.safeRead()
                    if (temp.isEmpty()) {
                        continue
                    }

                    content += temp

                    Assertions.assertThat(content).contains(search)

                    localCommand.terminate()

                    commandResult = CommandResult(localCommand.exitCode(), localCommand.trim(content))

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
            localCommand!!.attach(commandResult)

            val result = ConvertedResult(commandResult.content)
            ConvertedResultStorage.set(result)
            if (result.json !== null) {
                localCommand.attach(result.json!!)
            }
        }
    }

    /**
     * Run a command in the background.
     *
     * @param command String
     */
    open fun runInBackground(command: String) {
        try {
            LocalCommand(command, ByteArrayOutputStream()).toBackground()
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Saving the ID of a command running in the background to a variable.
     *
     * @param variable String
     */
    open fun saveBackgroundUuid(variable: String) {
        val future: ThreadFuture? = ThreadFutureStorage.last()

        Assertions.assertThat(future).`as`("Background command not running").isNotNull

        baseSpec.save(future!!.uuid.toString(), variable)
    }

    /**
     * Terminates a command that is running in the background.
     *
     * @param variable String?
     */
    open fun terminateBackground(variable: String?) {
        val future: CommandThreadFuture? = if (variable === null) {
            ThreadFutureStorage.last()
        } else {
            ThreadFutureStorage.get(variable)
        } as CommandThreadFuture?

        Assertions.assertThat(future).`as`("Background command not running").isNotNull

        var commandResult: CommandResult? = null

        try {
            future!!.terminate()

            commandResult = CommandResult(
                future.command.exitCode(),
                future.command.read(),
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            ThreadFutureStorage.remove(future!!.uuid.toString())

            if (commandResult !== null) {
                CommandStorage.set(commandResult)
                future.command.attach(commandResult)

                val result = ConvertedResult(commandResult.content)
                ConvertedResultStorage.set(result)
                if (result.json !== null) {
                    future.command.attach(result.json!!)
                }
            }
        }
    }

    /**
     * Exit code check.
     *
     * @param code Long
     */
    open fun exitCode(code: Long) {
        val commandResult = CommandStorage.get()

        if (commandResult === null) {
            Assertions.fail<Any>("The result of the command execution was not found")
        }

        Assertions.assertThat(commandResult!!.code).isEqualTo(code)
    }
}
