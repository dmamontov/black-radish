package tech.mamontov.blackradish.command.interfaces

interface Command {
    fun read(): String

    fun safeRead(): String

    fun destroy()

    fun exitCode(): Int

    fun exited(): Boolean
}
