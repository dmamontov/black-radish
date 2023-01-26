package tech.mamontov.blackradish.core.helpers

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jayway.jsonpath.JsonPath
import org.apache.commons.lang3.math.NumberUtils
import tech.mamontov.blackradish.core.asserts.NumberAssert

class JsonHelper {
    companion object {
        fun toJson(string: Any): String {
            return Gson().toJson(string)
        }

        fun list(json: JsonElement, expression: String): List<Any?> {
            return JsonPath.parse(toJson(json)).read(expression, List::class.java)
        }

        fun string(json: JsonElement, expression: String): String {
            return JsonPath.parse(toJson(json)).read(expression, String::class.java)
        }

        fun sum(json: JsonElement, expression: String): Double {
            return list(json, expression).filterNotNull().sumOf {
                NumberAssert.assertThat(it).isNumber()

                NumberUtils.createNumber(it.toString()).toDouble()
            }
        }
    }
}
