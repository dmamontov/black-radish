package tech.mamontov.blackradish.core.parsers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.sonalake.utah.config.Config
import com.sonalake.utah.config.ConfigLoader
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.Charset
import com.sonalake.utah.Parser as UtahParser

class TemplateParser(private val uri: URI) : Logged, Parser {
    private val template: Config

    init {
        val file = File(this.uri.path)
        Assertions.assertThat(file).isFile
        FileAssert.assertThat(file).isMimeTypeEquals("application/xml")

        this.template = ConfigLoader().loadConfig(uri.toURL())
    }

    override fun `is`(content: String): Boolean {
        return true
    }

    override fun validate(content: String, schema: File) {
    }

    override fun parse(content: String): JsonElement {
        val parsed: MutableList<Map<String, String>> = mutableListOf()

        InputStreamReader(content.byteInputStream(Charset.defaultCharset())).use { reader: InputStreamReader ->
            val parser: UtahParser = UtahParser.parse(this.template, reader)
            var record: Map<String, String>?

            while (parser.next().also { record = it } !== null) {
                parsed.add(record!!)
            }
        }

        return JsonParser.parseString(JsonHelper.toJson(parsed))
    }
}
