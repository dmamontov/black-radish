package tech.mamontov.blackradish.core.properties

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File

class ThreadContentProperty : Logged {
    companion object {
        private val file: ThreadLocal<Result?> = ThreadLocal<Result?>()

        fun set(content: Result) {
            file.set(content)
        }

        fun get(): Result {
            Assertions.assertThat(file.get()).`as`("No parsed result.").isNotNull

            return file.get()!!
        }

        fun raw(): String {
            return this.get().content
        }

        fun parsed(): JsonElement {
            val content = this.get()
            Assertions.assertThat(content.json).`as`("No parsing result").isNotNull

            return content.json!!
        }

        fun validate(schema: File) {
            val content = this.get()
            Assertions.assertThat(content.parser).`as`("No parsing result").isNotNull

            content.parser!!.validate(content.content, schema)
        }
    }
}
