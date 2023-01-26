package tech.mamontov.blackradish.filesystem.properties

import tech.mamontov.blackradish.core.interfaces.Logged

class ThreadFilesProperty : Logged {
    companion object {
        private val files: ThreadLocal<List<String>> = object : ThreadLocal<List<String>>() {
            override fun initialValue(): List<String> {
                return listOf()
            }
        }

        fun set(fileList: List<String>) {
            files.set(fileList)
        }

        fun get(): List<String> {
            return files.get()
        }
    }
}
