package tech.mamontov.blackradish.command.properties

import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.core.interfaces.Logged

class ThreadCommandResultProperty : Logged {
    companion object {
        private val commandResult: ThreadLocal<CommandResult?> = ThreadLocal()

        fun set(result: CommandResult) {
            commandResult.set(result)
        }

        fun get(): CommandResult? {
            return commandResult.get()
        }
    }
}
