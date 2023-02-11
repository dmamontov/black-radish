package tech.mamontov.blackradish.command.data

/**
 * Command result data
 *
 * @author Dmitry Mamontov
 *
 * @property code Int
 * @property content String
 * @constructor
 */
data class CommandResult(
    val code: Int,
    val content: String,
)
