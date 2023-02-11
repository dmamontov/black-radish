package tech.mamontov.blackradish.core.storages

import io.cucumber.datatable.DataTable
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.runners.ScriptRunner
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Properties

/**
 * Database storage
 *
 * @author Dmitry Mamontov
 */
class DatabaseConnectionStorage : Logged {
    companion object {
        private val connection: ThreadLocal<Connection?> = ThreadLocal<Connection?>()
        private val timeout: ThreadLocal<Int> = object : ThreadLocal<Int>() {
            override fun initialValue(): Int {
                return 0
            }
        }

        /**
         * Connect to DB with jdbc
         *
         * @param jdbc String
         * @param properties Properties
         */
        fun connect(jdbc: String, dataTable: DataTable?) {
            connection.set(DriverManager.getConnection(jdbc, this.properties(jdbc, dataTable)))
        }

        /**
         * Disconnect on DB
         *
         * @param force Boolean
         */
        fun disconnect(force: Boolean = false) {
            if (connection.get() !== null) {
                connection.get()!!.close()
                connection.set(null)
            } else if (!force) {
                Assertions.fail<Any?>("Database connection not open")
            }

            this.timeout.set(0)
        }

        /**
         * Set query timeout
         *
         * @param timeout Long
         */
        fun setTimeout(timeout: Int) {
            this.timeout.set(timeout)
        }

        /**
         * Check if table exist
         *
         * @param table String
         * @return Boolean
         */
        fun isTableExist(table: String): Boolean {
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

        /**
         * Execute query
         *
         * @param query String
         */
        fun execute(query: String): List<Map<String, Any>> {
            Assertions.assertThat(connection.get())
                .`as`("Database connection not open").isNotNull

            val runner = ScriptRunner(connection.get()!!, this.timeout.get())
            runner.run(query.reader())

            return runner.lastResult
        }

        /**
         * Prepare properties
         *
         * @param jdbc String
         * @param dataTable DataTable?
         * @return Properties
         */
        private fun properties(jdbc: String, dataTable: DataTable? = null): Properties {
            if (dataTable === null) {
                return Properties()
            }

            val properties = Properties()

            val available = DriverManager.getDriver(jdbc).getPropertyInfo(jdbc, null).map {
                it.name
            }.toList()

            dataTable.asLists().forEach { row: List<String> ->
                Assertions.assertThat(row)
                    .`as`("Should be 2 columns.")
                    .hasSize(2)

                if (available.isNotEmpty()) {
                    Assertions.assertThat(row[0]).isIn(available)
                }

                properties[row[0]] = row[1]
            }

            return properties
        }
    }
}
