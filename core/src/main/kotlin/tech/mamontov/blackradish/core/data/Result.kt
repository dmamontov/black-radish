package tech.mamontov.blackradish.core.data

import com.google.gson.JsonElement
import tech.mamontov.blackradish.core.parsers.JsonParser
import tech.mamontov.blackradish.core.parsers.Parser
import tech.mamontov.blackradish.core.parsers.XmlParser
import tech.mamontov.blackradish.core.parsers.YamlParser

class Result(val content: String, parser: Parser? = null) {
    var json: JsonElement? = null
    var parser: Parser? = null

    init {
        this.parser = if (parser !== null) {
            parser
        } else if (JsonParser().`is`(content)) {
            JsonParser()
        } else if (XmlParser().`is`(content)) {
            XmlParser()
        } else if (YamlParser().`is`(content)) {
            YamlParser()
        } else {
            null
        }

        if (this.parser !== null) {
            this.json = this.parser!!.parse(content)
        }
    }
}
