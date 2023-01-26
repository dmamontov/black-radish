package tech.mamontov.blackradish.core.parsers

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonElement
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import com.google.gson.JsonParser as GSonParser

open class JsonParser : Logged, Parser {
    override fun `is`(content: String): Boolean {
        return try {
            this.parse(content)
            true
        } catch (_: Exception) {
            false
        } catch (_: AssertionError) {
            false
        }
    }

    override fun validate(content: String, schema: File) {
        FileAssert.assertThat(schema).isMimeTypeEquals("application/json")

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

    override fun parse(content: String): JsonElement {
        var result: JsonElement? = null
        try {
            result = GSonParser.parseString(content)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        return result!!
    }
}
