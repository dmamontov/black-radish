package tech.mamontov.blackradish.core.properties

import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.interfaces.Logged
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Properties

class ThreadDatabaseProperty : Logged {
    companion object {
        private val connection: ThreadLocal<Connection?> = ThreadLocal<Connection?>()
        private val result: ThreadLocal<List<Map<String, Any>>?> = ThreadLocal<List<Map<String, Any>>?>()

        fun connect(jdbc: String, properties: Properties) {
            connection.set(DriverManager.getConnection(jdbc, properties))
        }

        fun disconnect(force: Boolean = false) {
            if (connection.get() !== null) {
                connection.get()!!.close()
                connection.set(null)
            } else if (!force) {
                Assertions.fail<Any?>("Database connection not open")
            }
        }

        fun exists(table: String): Boolean {
            Assertions.assertThat(connection.get())
                .`as`("Database connection not open").isNotNull

            val result: ResultSet = connection.get()!!.metaData.getTables(
                null,
                null,
                null,
                arrayOf("TABLE"),
            )

            val tables = mutableListOf<String>()

            while (result.next()) {
                val name: String = result.getString("TABLE_NAME")
                val schema: String? = result.getString("TABLE_SCHEM")

                if (schema !== null) {
                    tables.add("$schema.$name")
                }

                tables.add(name)
            }

            return table in tables
        }

        fun execute(query: String) {
            Assertions.assertThat(connection.get())
                .`as`("Database connection not open").isNotNull

            this.result.set(
                connection.get()!!.createStatement().executeQuery(query).toList(),
            )
        }

        fun result(): List<Map<String, Any>> {
            Assertions.assertThat(result.get())
                .`as`("Query result is missing").isNotNull

            return result.get()!!
        }
    }
}

private fun ResultSet.toList(): List<Map<String, Any>> {
    val results: MutableList<Map<String, Any>> = mutableListOf()

    while (next()) {
        val row: MutableMap<String, Any> = mutableMapOf()
        for (i in 1..metaData.columnCount) {
            row[metaData.getColumnLabel(i)] = getObject(i)
        }
        results.add(row)
    }

    return results
}
