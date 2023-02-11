package tech.mamontov.blackradish.core.storages

import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Thread executor storage
 *
 * @author Dmitry Mamontov
 */
class ThreadExecutorStorage : Logged {
    companion object {
        private val service: ThreadLocal<ExecutorService?> = ThreadLocal<ExecutorService?>()

        /**
         * Create new ExecutorService
         *
         * @return ExecutorService
         */
        fun create(): ExecutorService {
            if (service.get() === null) {
                service.set(Executors.newSingleThreadExecutor())
            }

            return service.get()!!
        }

        /**
         * Get last ExecutorService
         *
         * @return ExecutorService?
         */
        fun get(): ExecutorService? {
            return service.get()
        }

        /**
         * Reset ExecutorService
         */
        fun reset() {
            service.set(null)
        }
    }
}
