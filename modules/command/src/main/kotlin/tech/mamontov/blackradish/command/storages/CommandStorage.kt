package tech.mamontov.blackradish.command.storages

import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.storages.ConfigurationStorage

/**
 * Command storage
 *
 * @author Dmitry Mamontov
 */
class CommandStorage : Logged {
    companion object {
        private val defaultTimeout: Long =
            ConfigurationStorage.get(ConfigurationStorage.MODULE_COMMAND_TIMEOUT, 60).toLong()

        private val timeout: ThreadLocal<Long> = object : ThreadLocal<Long>() {
            override fun initialValue(): Long {
                return defaultTimeout
            }
        }

        private val result: ThreadLocal<CommandResult?> = ThreadLocal()

        /**
         * Set command result.
         *
         * @param commandResult CommandResult
         */
        fun set(commandResult: CommandResult) {
            result.set(commandResult)
        }

        /**
         * Set command timeout.
         *
         * @param seconds Long
         */
        fun setTimeout(seconds: Long) {
            timeout.set(seconds)
        }

        /**
         * Get command result.
         *
         * @return CommandResult?
         */
        fun get(): CommandResult? {
            return result.get()
        }

        /**
         * Get command timeout.
         *
         * @return Long
         */
        fun getTimeout(): Long {
            return timeout.get()
        }

        /**
         * Reset storage.
         */
        fun reset() {
            timeout.set(defaultTimeout)
            result.set(null)
        }
    }
}
