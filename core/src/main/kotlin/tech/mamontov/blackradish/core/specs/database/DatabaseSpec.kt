package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.qameta.allure.Allure
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.converters.DatabaseConverter
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.runners.ScriptRunner
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.core.storages.DatabaseConnectionStorage
import java.nio.charset.Charset
import java.sql.DriverManager

/**
 * Database spec
 *
 * @author Dmitry Mamontov
 */
open class DatabaseSpec : Logged {

    /**
     * Load driver.
     *
     * @param driver String
     */
    open fun load(driver: String) {
        val drivers = DriverManager.getDrivers().toList().map {
            it::class.java.canonicalName
        }

        if (drivers.contains(driver)) {
            return
        }

        try {
            Class.forName(driver)
        } catch (e: ClassNotFoundException) {
            Assertions.fail<Any?>("Driver $driver not found", e)
        }
    }

    /**
     * Connecting to the database via jdbc.
     *
     * @param jdbc String
     * @param dataTable DataTable?
     */
    open fun connect(jdbc: String, dataTable: DataTable? = null) {
        try {
            DatabaseConnectionStorage.connect(
                jdbc,
                dataTable,
            )
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    /**
     * Disconnect from the database.
     */
    open fun disconnect() {
        try {
            DatabaseConnectionStorage.disconnect()
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    /**
     * Set execute query timeout.
     *
     * @param seconds Int
     */
    open fun timeout(seconds: Int) {
        DatabaseConnectionStorage.setTimeout(seconds)
    }

    /**
     * Execute query
     *
     * @param query String
     */
    open fun execute(query: String) {
        var queryWithDelimiter = query
        if (!queryWithDelimiter.endsWith(ScriptRunner.DEFAULT_DELIMITER)) {
            queryWithDelimiter += ScriptRunner.DEFAULT_DELIMITER
        }

        Allure.addAttachment("query.sql", queryWithDelimiter)

        try {
            val result = DatabaseConnectionStorage.execute(queryWithDelimiter)

            val json = AnyObject.that(result).asJsonString
            ConvertedResultStorage.set(ConvertedResult(json, DatabaseConverter(result)))

            Allure.addAttachment("result.json", json)
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }

    open fun execute(query: DocString) {
        this.execute(query.content)
    }

    /**
     * Execute query from file
     *
     * @param path String
     */
    open fun executeFrom(path: String) {
        val file = Filesystem.that(path).safe().asFile

        Assertions.assertThat(file).isFile
        FileAssert.assertThat(file).isSql
        Assertions.assertThat(file).isNotEmpty

        this.execute(
            FileUtils.readFileToString(file, Charset.defaultCharset().displayName())
                .replace("\r\n", System.lineSeparator()),
        )
    }

    /**
     * Checks the query result for a match.
     *
     * @param dataTable DataTable
     */
    open fun match(dataTable: DataTable) {
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter)
            .`as`("The last result is not the result of a database query.")
            .isInstanceOf(DatabaseConverter::class.java)

        val result = (converter as DatabaseConverter).result

        Assertions.assertThat(result).`as`("Query result is empty").isNotEmpty

        val list = listOf(result[0].keys.toList()) + result.map { row: Map<String, Any> ->
            row.values.map { it.toString() }.toList()
        }

        Assertions.assertThat(list).isEqualTo(dataTable.asLists())
    }

    /**
     * Checking for the existence of a table.
     *
     * @param table String
     * @param not Boolean
     */
    open fun exists(table: String, not: Boolean) {
        val result = DatabaseConnectionStorage.isTableExist(table)

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
}
