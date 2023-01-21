package tech.mamontov.blackradish.command.utils

import com.sshtools.forker.client.ForkerProcess
import org.apache.commons.io.output.ByteArrayOutputStream
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.ThreadFuture
import java.util.UUID
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class CommandThreadFuture(
    override val uuid: UUID,
    override val future: Future<*>,
    val command: Command
) : Logged, ThreadFuture {
    override fun destroy() {
        command.destroy()

        if (!future.isDone && !future.isCancelled) {
            future.cancel(true)
        }
    }
}