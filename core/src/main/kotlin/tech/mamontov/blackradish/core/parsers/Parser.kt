package tech.mamontov.blackradish.core.parsers

import com.google.gson.JsonElement
import java.io.File

interface Parser {
    fun `is`(content: String): Boolean

    fun validate(content: String, schema: File)

    fun parse(content: String): JsonElement
}
