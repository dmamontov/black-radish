package tech.mamontov.blackradish.core.parsers

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import org.yaml.snakeyaml.Yaml
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import com.google.gson.JsonParser as GSonParser

class YamlParser : Logged, JsonParser() {
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
        try {
            super.validate(JsonHelper.toJson(this.parse(content)), schema)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    override fun parse(content: String): JsonElement {
        var loaded: Any? = null
        try {
            loaded = Yaml().load<Any>(content)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(loaded).isNotInstanceOf(String::class.java)

        return GSonParser.parseString(JsonHelper.toJson(loaded!!))
    }
}
