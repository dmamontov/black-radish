package tech.mamontov.blackradish.core.parsers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.assertj.core.api.Assertions
import org.json.XML
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

class XmlParser : Logged, Parser {
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
        FileAssert.assertThat(schema).isMimeTypeEquals("application/xml")
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

    override fun parse(content: String): JsonElement {
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
