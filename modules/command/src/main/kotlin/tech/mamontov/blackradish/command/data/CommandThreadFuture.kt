package tech.mamontov.blackradish.command.data

import tech.mamontov.blackradish.command.interfaces.Command
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ThreadFuture
import java.util.UUID
import java.util.concurrent.Future

class CommandThreadFuture(
    override val uuid: UUID,
    override val future: Future<*>,
    val command: Command,
) : Logged, ThreadFuture {
    override fun destroy() {
        command.destroy()

        if (!future.isDone && !future.isCancelled) {
            future.cancel(true)
        }
    }
}
