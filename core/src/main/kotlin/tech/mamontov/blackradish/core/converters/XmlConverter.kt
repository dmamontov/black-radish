package tech.mamontov.blackradish.core.converters

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.assertj.core.api.Assertions
import org.json.XML
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.enumerated.ConvertedResultOperation
import tech.mamontov.blackradish.core.interfaces.Converter
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

/**
 * Xml converter
 *
 * @author Dmitry Mamontov
 *
 * @property operations List<ConvertedResultOperation>
 */
class XmlConverter : Logged, Converter {
    override val operations: List<ConvertedResultOperation> = listOf(
        ConvertedResultOperation.SUM,
        ConvertedResultOperation.MATCH,
        ConvertedResultOperation.VALIDATE,
        ConvertedResultOperation.SAVE,
    )

    /**
     * String content is xml
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
     * Validate xml string by xsd schema
     *
     * @param content String
     * @param schema File
     */
    override fun validate(content: String, schema: File) {
        FileAssert.assertThat(schema).isXml
        try {
            SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(schema)
                .newValidator()
                .validate(StreamSource(StringReader(content)))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Xml string to JsonElement
     *
     * @param content String
     * @return JsonElement
     */
    override fun toJson(content: String): JsonElement {
        var result: JsonElement? = null
        try {
            val parsed = XML.toJSONObject(content).toString(4)

            Assertions.assertThat(parsed).`as`("Invalid XML").isNotEqualTo("{}")
            result = JsonParser.parseString(parsed)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        return result!!
    }
}
