package tech.mamontov.blackradish.core.interfaces

import java.util.UUID
import java.util.concurrent.Future

interface ThreadFuture {
    val uuid: UUID

    val future: Future<*>

    fun destroy()
}
