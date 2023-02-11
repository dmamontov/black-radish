package tech.mamontov.blackradish.core.interfaces

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.converters.JsonConverter
import tech.mamontov.blackradish.core.enumerated.ConvertedResultOperation
import java.io.File

/**
 * To json converter interface
 *
 * @author Dmitry Mamontov
 */
interface ToJsonConverter : Converter {
    override val operations: List<ConvertedResultOperation>
        get() = listOf(
            ConvertedResultOperation.SIZE,
            ConvertedResultOperation.SUM,
            ConvertedResultOperation.MATCH,
            ConvertedResultOperation.SAVE,
        )

    /**
     * Converter is not detected automatically
     *
     * @param content String
     * @return Boolean
     */
    override fun `is`(content: String): Boolean {
        Assertions.fail<Any?>("Converter does not support automatic detection")

        return true
    }

    /**
     * Converter does not support schema validation
     *
     * @param content String
     * @param schema File
     */
    override fun validate(content: String, schema: File) {
        Assertions.fail<Any?>("Converter does not support schema validation")
    }

    /**
     * Json string to JsonElement
     *
     * @param content String
     * @return JsonElement
     */
    override fun toJson(content: String): JsonElement {
        return JsonConverter().toJson(content)
    }
}
