package tech.mamontov.blackradish.core.specs.parser

import com.jayway.jsonpath.matchers.JsonPathMatchers
import io.cucumber.datatable.DataTable
import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import tech.mamontov.blackradish.core.asserts.NumberAssert
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.properties.ThreadContentProperty
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import java.io.File

abstract class ParserSpec : Logged, BaseSpec() {
    open fun resultCount(min: Boolean, count: Int) {
        val data = ThreadContentProperty.parsed()

        Assertions.assertThat(data.isJsonArray).`as`("Must be an array").isTrue

        if (min) {
            Assertions.assertThat(data.asJsonArray.size()).isGreaterThanOrEqualTo(count)
        } else {
            Assertions.assertThat(data.asJsonArray.size()).isEqualTo(count)
        }
    }

    open fun resultSum(column: String, sum: String) {
        val data = ThreadContentProperty.parsed()

        MatcherAssert.assertThat(JsonHelper.toJson(data), JsonPathMatchers.hasJsonPath(column))
        NumberAssert.assertThat(JsonHelper.sum(data, column)).isEqualTo(sum)
    }

    open fun resultMatch(dataTable: DataTable, toComparisonOperation: Map<String, ComparisonOperation>) {
        val data = ThreadContentProperty.parsed()
        dataTable.asLists().forEach { row: List<String> ->
            Assertions.assertThat(row)
                .`as`("Should be 3 columns.")
                .hasSize(3)

            MatcherAssert.assertThat(JsonHelper.toJson(data), JsonPathMatchers.hasJsonPath(row[0]))
            Assertions.assertThat(toComparisonOperation).containsKey(row[1])

            this.comparison(JsonHelper.string(data, row[0]), toComparisonOperation[row[1]]!!, row[2])
        }
    }

    open fun resultValidate(path: String) {
        ThreadContentProperty.validate(File(this.uri(path)!!.path))
    }

    open fun resultSave(column: String? = null, variable: String) {
        if (column === null) {
            this.save(ThreadContentProperty.raw(), variable)

            return
        }

        val data = ThreadContentProperty.parsed()
        MatcherAssert.assertThat(JsonHelper.toJson(data), JsonPathMatchers.hasJsonPath(column))

        this.save(JsonHelper.string(data, column), variable)
    }
}
