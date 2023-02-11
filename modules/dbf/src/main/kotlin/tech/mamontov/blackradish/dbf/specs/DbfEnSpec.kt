package tech.mamontov.blackradish.dbf.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Dbf database spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class DbfEnSpec : Logged, DbfSpec() {

    /**
     * Opens connection to dbf database via jdbc.
     *
     * ```
     * Scenario: Database connection example
     *   When i connect to database 'dbf' by path 'artifacts/database'
     * ```
     * ```
     * Scenario: An example of connecting to a database in a zip archive
     *   When i connect to database 'dbf' by path 'artifacts/database/example.zip'
     * ```
     *
     * @param path String
     */
    @Given("^[I|i] connect to database 'dbf' by path '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    /**
     * Opens a connection to the jdbc database with predefined parameters.
     *
     * ```
     * Scenario: An example of connecting to a database with parameters
     *   When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
     *     | charset | UTF-8 |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @Given("^[I|i] connect to database 'dbf' by path '(.*?)' with the parameters:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
