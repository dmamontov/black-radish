package tech.mamontov.blackradish.core.helpers

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.apache.commons.lang3.math.NumberUtils
import java.util.Locale

/**
 * Any object
 *
 * @author Dmitry Mamontov
 *
 * @property value Any
 * @property asJsonString String
 * @property asJson JsonElement
 * @constructor
 */
class AnyObject(private val value: Any) {
    companion object {
        /**
         * Static constructor
         *
         * @param value Any
         * @return AnyObject
         */
        fun that(value: Any): AnyObject {
            return AnyObject(value)
        }
    }

    val asJsonString: String
        get() = GsonBuilder().setPrettyPrinting().create().toJson(value)

    val asJson: JsonElement
        get() = JsonParser.parseString(asJsonString)

    /**
     * Evaluate string by class name
     *
     * @param className String
     * @return Any?
     */
    fun to(className: String): Any? {
        val value = value.toString()

        return when (className) {
            "Int" -> value.toIntOrNull()
            "Short" -> value.toShortOrNull()
            "Byte" -> value.toByteOrNull()
            "Long" -> value.toLongOrNull()
            "Float" -> value.toFloatOrNull()
            "Double" -> value.toDoubleOrNull()
            "Boolean" -> value.toBooleanStrictOrNull()
            else -> value
        }
    }

    fun parse(): Any? {
        val value = value.toString().lowercase(Locale.getDefault())

        if (value.trim() in listOf("true", "false")) {
            return to("Boolean")
        }

        if (NumberUtils.isCreatable(value)) {
            if (value.indexOfAny(charArrayOf('.', ',')) > -1) {
                return NumberUtils.createNumber(value).toDouble()
            }

            return NumberUtils.createNumber(value).toLong()
        }

        return value
    }
}
