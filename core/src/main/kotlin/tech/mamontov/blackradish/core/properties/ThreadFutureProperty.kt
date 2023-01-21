package tech.mamontov.blackradish.core.properties

import tech.mamontov.blackradish.core.utils.ThreadFuture

class ThreadFutureProperty {
    companion object {
        private val futures: ThreadLocal<MutableMap<String, ThreadFuture>> =
            object : ThreadLocal<MutableMap<String, ThreadFuture>>() {
                override fun initialValue(): MutableMap<String, ThreadFuture> {
                    return mutableMapOf()
                }
            }

        fun last(): ThreadFuture? {
            return if (futures.get().isNotEmpty()) {
                futures.get().values.last()
            } else {
                null
            }
        }

        fun get(uuid: String): ThreadFuture? {
            return if (futures.get().containsKey(uuid)) {
                futures.get()[uuid]
            } else {
                null
            }
        }

        fun remove(uuid: String) {
            if (futures.get().containsKey(uuid)) {
                futures.get().remove(uuid)
            }
        }

        fun add(uuid: String, future: ThreadFuture) {
            futures.get()[uuid] = future
        }

        fun all(): MutableMap<String, ThreadFuture> {
            return futures.get()
        }

        fun reset() {
            futures.set(mutableMapOf())
        }
    }
}