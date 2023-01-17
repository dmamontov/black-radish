package tech.mamontov.blackradish.command.data

data class CommandResult(
    val code: Int,
    val content: String,
    val pid: Long? = null,
)
