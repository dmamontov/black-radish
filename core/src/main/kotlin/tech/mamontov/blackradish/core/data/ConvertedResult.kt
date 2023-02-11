package tech.mamontov.blackradish.core.data

import com.google.gson.JsonElement
import tech.mamontov.blackradish.core.converters.JsonConverter
import tech.mamontov.blackradish.core.converters.XmlConverter
import tech.mamontov.blackradish.core.converters.YamlConverter
import tech.mamontov.blackradish.core.interfaces.Converter

/**
 * Parse result data
 *
 * @author Dmitry Mamontov
 *
 * @property content String
 * @property json JsonElement?
 * @property converter Converter?
 * @constructor
 */
class ConvertedResult(val content: String, converter: Converter? = null) {
    var json: JsonElement? = null
    var converter: Converter? = null

    init {
        this.converter = if (converter !== null) {
            converter
        } else if (JsonConverter().`is`(content)) {
            JsonConverter()
        } else if (XmlConverter().`is`(content)) {
            XmlConverter()
        } else if (YamlConverter().`is`(content)) {
            YamlConverter()
        } else {
            null
        }

        if (this.converter !== null) {
            this.json = this.converter!!.toJson(content)
        }
    }
}
