package tech.mamontov.blackradish.filesystem.properties

import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.interfaces.Logged

class ThreadFileContentProperty : Logged {
    companion object {
        private val file: ThreadLocal<Result?> = ThreadLocal<Result?>()

        fun set(content: Result) {
            file.set(content)
        }

        fun get(): Result {
            if (file.get() === null) {
                Assertions.fail<Any>("There is no open file.")
            }

            return file.get()!!
        }

        fun raw(): String {
            return this.get().content
        }
    }
}
