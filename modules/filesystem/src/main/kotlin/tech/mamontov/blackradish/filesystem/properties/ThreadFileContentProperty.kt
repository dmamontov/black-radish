package tech.mamontov.blackradish.filesystem.properties

import tech.mamontov.blackradish.core.utils.Logged

class ThreadFileContentProperty : Logged {
    companion object {
        private val file: ThreadLocal<String?> = ThreadLocal<String?>()

        fun set(content: String) {
            file.set(content)
        }

        fun get(): String? {
            return file.get()
        }
    }
}
