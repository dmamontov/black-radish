package tech.mamontov.blackradish.core.storages

import tech.mamontov.blackradish.core.interfaces.ThreadFuture

/**
 * Thread future storage
 *
 * @author Dmitry Mamontov
 */
class ThreadFutureStorage {
    companion object {
        private val futures: ThreadLocal<MutableMap<String, ThreadFuture>> =
            object : ThreadLocal<MutableMap<String, ThreadFuture>>() {
                override fun initialValue(): MutableMap<String, ThreadFuture> {
                    return mutableMapOf()
                }
            }

        /**
         * Get last future
         *
         * @return ThreadFuture?
         */
        fun last(): ThreadFuture? {
            return if (futures.get().isNotEmpty()) {
                futures.get().values.last()
            } else {
                null
            }
        }

        /**
         * Get future by uuid
         *
         * @param uuid String
         * @return ThreadFuture?
         */
        fun get(uuid: String): ThreadFuture? {
            return if (futures.get().containsKey(uuid)) {
                futures.get()[uuid]
            } else {
                null
            }
        }

        /**
         * Remove future by uuid
         *
         * @param uuid String
         */
        fun remove(uuid: String) {
            if (futures.get().containsKey(uuid)) {
                futures.get().remove(uuid)
            }
        }

        /**
         * Add future with uuid
         *
         * @param uuid String
         * @param future ThreadFuture
         */
        fun add(uuid: String, future: ThreadFuture) {
            futures.get()[uuid] = future
        }

        /**
         * Get all futures
         *
         * @return MutableMap<String, ThreadFuture>
         */
        fun all(): MutableMap<String, ThreadFuture> {
            return futures.get()
        }

        /**
         * Reset futures
         */
        fun reset() {
            futures.set(mutableMapOf())
        }
    }
}
