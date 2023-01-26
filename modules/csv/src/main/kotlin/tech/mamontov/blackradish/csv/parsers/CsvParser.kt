package tech.mamontov.blackradish.csv.parsers

import com.google.gson.JsonElement
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.parsers.JsonParser
import java.io.File

open class CsvParser(val list: List<List<Any>>, private val records: List<Any>) : Logged, JsonParser() {

    override fun `is`(content: String): Boolean {
        return true
    }

    override fun validate(content: String, schema: File) {
        super.validate(JsonHelper.toJson(this.records), schema)
    }

    override fun parse(content: String): JsonElement {
        return super.parse(JsonHelper.toJson(this.records))
    }
}
