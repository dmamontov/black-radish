package tech.mamontov.blackradish.command.data

import tech.mamontov.blackradish.command.interfaces.Command
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ThreadFuture
import java.util.UUID
import java.util.concurrent.Future

/**
 * Command thread future.
 *
 * @author Dmitry Mamontov
 *
 * @property uuid UUID
 * @property future Future<*>
 * @property command Command
 * @constructor
 */
class CommandThreadFuture(
    override val uuid: UUID,
    override val future: Future<*>,
    val command: Command,
) : Logged, ThreadFuture {
    /**
     * Terminate future
     */
    override fun terminate() {
        command.terminate()

        if (!future.isDone && !future.isCancelled) {
            future.cancel(true)
        }
    }
}
