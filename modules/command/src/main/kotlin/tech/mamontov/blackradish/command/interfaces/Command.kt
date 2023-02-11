package tech.mamontov.blackradish.command.interfaces

import com.google.gson.Gson
import com.google.gson.JsonElement
import io.qameta.allure.Allure
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.command.data.CommandThreadFuture
import tech.mamontov.blackradish.core.storages.ThreadExecutorStorage
import tech.mamontov.blackradish.core.storages.ThreadFutureStorage
import java.util.UUID
import java.util.concurrent.ExecutorService

/**
 * Command interface
 *
 * @author Dmitry Mamontov
 */
interface Command {
    /**
     * Read command result.
     *
     * @return String
     */
    fun read(): String

    /**
     * Non-blocking read command result.
     *
     * @return String
     */
    fun safeRead(): String

    /**
     * Terminate running command.
     */
    fun terminate()

    /**
     * Get exit code.
     *
     * @return Int
     */
    fun exitCode(): Int

    /**
     * Is exited command.
     *
     * @return Boolean
     */
    fun isExited(): Boolean

    /**
     * Attach command result.
     *
     * @param result CommandResult
     */
    fun attach(result: CommandResult) {
        Allure.addAttachment("command.json", "application/json", Gson().toJson(result).toString())
    }

    /**
     * Attach command result as json.
     *
     * @param json JsonElement
     */
    fun attach(json: JsonElement) {
        Allure.addAttachment("parsed.json", "application/json", json.toString())
    }

    /**
     * Fork command process.
     */
    fun toBackground() {
        val pool: ExecutorService = ThreadExecutorStorage.create()

        val future = pool.submit {
            while (!this.isExited()) {
                this.safeRead()
            }
        }

        val uuid = UUID.randomUUID()
        ThreadFutureStorage.add(uuid.toString(), CommandThreadFuture(uuid, future, this))
    }
}
