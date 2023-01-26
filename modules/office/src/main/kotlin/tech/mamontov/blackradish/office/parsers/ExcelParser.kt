package tech.mamontov.blackradish.office.parsers

import com.google.gson.JsonElement
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.parsers.JsonParser
import java.io.File

open class ExcelParser(val records: List<List<Map<String, Any>>>) : Logged, JsonParser() {

    override fun `is`(content: String): Boolean {
        return true
    }

    override fun validate(content: String, schema: File) {
        Assertions.fail<Any>("Validation is not supported")
    }

    override fun parse(content: String): JsonElement {
        return super.parse(JsonHelper.toJson(this.records))
    }
}
