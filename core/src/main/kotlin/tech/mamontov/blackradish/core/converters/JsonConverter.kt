package tech.mamontov.blackradish.core.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonElement
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.helpers.ExtJsonObject
import tech.mamontov.blackradish.core.interfaces.Converter
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File

/**
 * Json converter
 *
 * @author Dmitry Mamontov
 */
open class JsonConverter : Logged, Converter {
    /**
     * String content is json
     *
     * @param content String
     * @return Boolean
     */
    override fun `is`(content: String): Boolean {
        return try {
            this.toJson(content)
            true
        } catch (_: Exception) {
            false
        } catch (_: AssertionError) {
            false
        }
    }

    /**
     * Validate json string by json schema
     *
     * @param content String
     * @param schema File
     */
    override fun validate(content: String, schema: File) {
        FileAssert.assertThat(schema).isJson

        var errors: Set<ValidationMessage> = setOf()

        try {
            errors = JsonSchemaFactory
                .getInstance(SpecVersion.VersionFlag.V4)
                .getSchema(schema.toURI())
                .validate(ObjectMapper().readTree(content))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(errors).isEmpty()
    }

    /**
     * Json string to JsonElement
     *
     * @param content String
     * @return JsonElement
     */
    override fun toJson(content: String): JsonElement {
        var result: JsonElement? = null
        try {
            result = ExtJsonObject.parse(content)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        return result!!
    }
}
