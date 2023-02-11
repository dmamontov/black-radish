package tech.mamontov.blackradish.core.converters

import com.google.gson.JsonElement
import com.sonalake.utah.config.Config
import com.sonalake.utah.config.ConfigLoader
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.interfaces.ToJsonConverter
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.Charset
import com.sonalake.utah.Parser as UtahParser

/**
 * Template converter
 *
 * @author Dmitry Mamontov
 *
 * @see [UtahParser](https://github.com/sonalake/utah-converter)
 *
 * @property uri URI
 * @property template Config
 * @constructor
 */
class TemplateConverter(private val uri: URI) : Logged, ToJsonConverter {
    private val template: Config

    init {
        val file = File(this.uri.path)
        Assertions.assertThat(file).isFile
        FileAssert.assertThat(file).isXml

        this.template = ConfigLoader().loadConfig(uri.toURL())
    }

    /**
     * String to JsonElement by template
     *
     * @param content String
     * @return JsonElement
     */
    override fun toJson(content: String): JsonElement {
        val parsed: MutableList<Map<String, String>> = mutableListOf()

        InputStreamReader(content.byteInputStream(Charset.defaultCharset())).use { reader: InputStreamReader ->
            val parser: UtahParser = UtahParser.parse(this.template, reader)
            var record: Map<String, String>?

            while (parser.next().also { record = it } !== null) {
                parsed.add(record!!)
            }
        }

        return AnyObject.that(parsed).asJson
    }
}
