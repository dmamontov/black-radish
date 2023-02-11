package tech.mamontov.blackradish.core.storages

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.interfaces.Converter
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File

/**
 * Parse result storage
 *
 * @author Dmitry Mamontov
 */
class ConvertedResultStorage : Logged {
    companion object {
        private val convertedResult: ThreadLocal<ConvertedResult?> = ThreadLocal<ConvertedResult?>()

        /**
         * Set last parse result
         *
         * @param content ConvertedResult
         */
        fun set(result: ConvertedResult) {
            convertedResult.set(result)
        }

        /**
         * Get parse result
         *
         * @return ConvertedResult
         */
        fun get(): ConvertedResult {
            Assertions.assertThat(convertedResult.get()).`as`("Not found converted result.").isNotNull

            return convertedResult.get()!!
        }

        /**
         * As raw result
         *
         * @return String
         */
        fun asRaw(): String {
            return this.get().content
        }

        /**
         * As json result
         *
         * @return JsonElement
         */
        fun asJson(): JsonElement {
            val result = this.get()
            Assertions.assertThat(result.json).`as`("Not found converted result").isNotNull

            return result.json!!
        }

        /**
         * Get converter
         *
         * @return Converter
         */
        fun converter(): Converter {
            val result = this.get()
            Assertions.assertThat(result.converter).`as`("Content not supported converting").isNotNull

            return result.converter!!
        }

        /**
         * Validate parse result by schema
         *
         * @param schema File
         */
        fun validate(schema: File) {
            val result = this.get()
            Assertions.assertThat(result.converter).`as`("No parsing parseResult").isNotNull

            result.converter!!.validate(result.content, schema)
        }

        /**
         * Reset parse result
         */
        fun reset() {
            convertedResult.set(null)
        }
    }
}
