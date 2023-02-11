package tech.mamontov.blackradish.core.converters

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import org.yaml.snakeyaml.Yaml
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File

/**
 * Yaml converter
 *
 * @author Dmitry Mamontov
 */
class YamlConverter : Logged, JsonConverter() {
    /**
     * String content is yaml
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
     * Validate yaml string by json schema
     *
     * @param content String
     * @param schema File
     */
    override fun validate(content: String, schema: File) {
        try {
            super.validate(AnyObject.that(this.toJson(content)).asJsonString, schema)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Yaml string to JsonElement
     *
     * @param content String
     * @return JsonElement
     */
    override fun toJson(content: String): JsonElement {
        var loaded: Any? = null
        try {
            loaded = Yaml().load<Any>(content)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(loaded).isNotInstanceOf(String::class.java)

        return AnyObject.that(loaded!!).asJson
    }
}
