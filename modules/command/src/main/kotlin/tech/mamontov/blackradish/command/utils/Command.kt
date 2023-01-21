package tech.mamontov.blackradish.command.utils

interface Command {
    fun read(): String

    fun safeRead(): String

    fun destroy()

    fun exitCode(): Int

    fun exited(): Boolean
}