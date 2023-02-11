package tech.mamontov.blackradish.core.interfaces

import java.util.UUID
import java.util.concurrent.Future

/**
 * Thread future interface
 *
 * @author Dmitry Mamontov
 *
 * @property uuid UUID
 * @property future Future<*>
 */
interface ThreadFuture {
    val uuid: UUID

    val future: Future<*>

    /**
     * Terminate future
     */
    fun terminate()
}
