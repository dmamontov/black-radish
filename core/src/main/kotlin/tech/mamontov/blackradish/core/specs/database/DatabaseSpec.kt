package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.qameta.allure.Allure
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.properties.ThreadContentProperty
import tech.mamontov.blackradish.core.properties.ThreadDatabaseProperty
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import java.io.File
import java.nio.charset.Charset
import java.util.Properties

abstract class DatabaseSpec : Logged, BaseSpec() {

    open fun connect(jdbc: String, dataTable: DataTable? = null, availableProperties: List<String> = listOf()) {
        try {
            ThreadDatabaseProperty.connect(jdbc, this.properties(dataTable, availableProperties))
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    open fun disconnect() {
        try {
            ThreadDatabaseProperty.disconnect()
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    open fun execute(query: String) {
        Allure.addAttachment("query.sql", query)

        try {
            ThreadDatabaseProperty.execute(query)

            val result = JsonHelper.toJson(ThreadDatabaseProperty.result())
            ThreadContentProperty.set(Result(result))

            Allure.addAttachment("result.json", result)
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    open fun executeFrom(path: String) {
        val file = File(this.uri(path)!!.path)

        Assertions.assertThat(file).isFile
        FileAssert.assertThat(file).isMimeTypeEquals("text/x-sql")
        Assertions.assertThat(file).isNotEmpty

        try {
            this.execute(
                FileUtils.readFileToString(file, Charset.defaultCharset().displayName())
                    .replace("\r\n", System.lineSeparator()),
            )
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    open fun resultIs(dataTable: DataTable) {
        val result = ThreadDatabaseProperty.result()

        Assertions.assertThat(result).`as`("Query result is empty").isNotEmpty

        val list = listOf(result[0].keys.toList()) + result.map { row: Map<String, Any> ->
            row.values.map { it.toString() }.toList()
        }

        Assertions.assertThat(list).isEqualTo(dataTable.asLists())
    }

    open fun tableExists(table: String, not: Boolean) {
        val result = ThreadDatabaseProperty.exists(table)

        if (not) {
            Assertions.assertThat(result)
                .`as`("Table '$table' exists")
                .isFalse
        } else {
            Assertions.assertThat(result)
                .`as`("Table '$table' does not exists")
                .isTrue
        }
    }

    private fun properties(dataTable: DataTable? = null, availableProperties: List<String>): Properties {
        val properties = Properties()

        if (dataTable === null) {
            return properties
        }

        dataTable.asLists().forEach { row: List<String> ->
            Assertions.assertThat(row)
                .`as`("Should be 2 columns.")
                .hasSize(2)

            if (availableProperties.isNotEmpty()) {
                Assertions.assertThat(row[0]).isIn(availableProperties)
            }

            properties[row[0]] = row[1]
        }

        return properties
    }
}
