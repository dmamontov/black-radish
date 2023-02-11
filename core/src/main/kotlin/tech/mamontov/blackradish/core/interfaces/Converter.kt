package tech.mamontov.blackradish.core.interfaces

import com.google.gson.JsonElement
import tech.mamontov.blackradish.core.enumerated.ConvertedResultOperation
import java.io.File

/**
 * Converter interface
 *
 * @author Dmitry Mamontov
 *
 * @property operations List<ConvertedResultOperation>
 */
interface Converter {
    val operations: List<ConvertedResultOperation>
        get() = listOf(
            ConvertedResultOperation.SIZE,
            ConvertedResultOperation.SUM,
            ConvertedResultOperation.MATCH,
            ConvertedResultOperation.VALIDATE,
            ConvertedResultOperation.SAVE,
        )

    /**
     * Content is
     *
     * @param content String
     * @return Boolean
     */
    fun `is`(content: String): Boolean

    /**
     * Validate content by schema
     *
     * @param content String
     * @param schema File
     */
    fun validate(content: String, schema: File)

    /**
     * Parse content to JsonElement
     *
     * @param content String
     * @return JsonElement
     */
    fun toJson(content: String): JsonElement
}
