package tech.mamontov.blackradish.core.helpers

class EvaluateHelper {
    companion object {
        fun eval(value: String, className: String): Any? {
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
    }
}
