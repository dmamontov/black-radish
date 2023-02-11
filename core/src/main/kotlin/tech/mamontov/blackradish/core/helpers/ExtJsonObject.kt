package tech.mamontov.blackradish.core.helpers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.jayway.jsonpath.JsonPath
import org.apache.commons.lang3.math.NumberUtils
import tech.mamontov.blackradish.core.asserts.NumberAssert

/**
 * Extended json object
 *
 * @author Dmitry Mamontov
 *
 * @see [JsonPath](https://github.com/json-path/JsonPath)
 *
 * @property element JsonElement
 * @property jsonString String
 * @constructor
 */
class ExtJsonObject(private val element: JsonElement) {
    companion object {
        /**
         * Static constructor
         *
         * @param obj Any
         * @return ExtJsonObject
         */
        fun that(obj: Any): ExtJsonObject {
            return ExtJsonObject(AnyObject.that(obj).asJson)
        }

        fun parse(jsonString: String): JsonElement {
            return JsonParser.parseString(jsonString)
        }
    }

    private val jsonString = AnyObject.that(element).asJsonString

    /**
     * Find string by json path
     *
     * @param jsonPath String
     * @return String
     */
    fun findString(jsonPath: String): String {
        return JsonPath.parse(jsonString).read(jsonPath, String::class.java)
    }

    /**
     * Find list by json path
     *
     * @param jsonPath String
     * @return List<Any?>
     */
    fun findList(jsonPath: String): List<Any?> {
        return JsonPath.parse(jsonString).read(jsonPath, List::class.java)
    }

    /**
     * Sum list by json path
     *
     * @param jsonPath String
     * @return Double
     */
    fun sum(jsonPath: String): Double {
        return findList(jsonPath).filterNotNull().sumOf {
            NumberAssert.assertThat(it).isNumber

            NumberUtils.createNumber(it.toString()).toDouble()
        }
    }
}
