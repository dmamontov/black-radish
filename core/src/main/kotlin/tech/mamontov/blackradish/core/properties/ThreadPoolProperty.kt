package tech.mamontov.blackradish.core.properties

import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadPoolProperty : Logged {
    companion object {
        private val pool: ThreadLocal<ExecutorService?> = ThreadLocal<ExecutorService?>()

        fun create(): ExecutorService {
            if (pool.get() === null) {
                pool.set(Executors.newSingleThreadExecutor())
            }

            return pool.get()!!
        }

        fun get(): ExecutorService? {
            return pool.get()
        }

        fun reset() {
            pool.set(null)
        }
    }
}
