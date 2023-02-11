package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Database spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class DatabaseEnSpec : Logged, DatabaseSpec() {
    /**
     * Load jdbc driver.
     *
     * ```
     * Scenario: Example load jdbc driver
     *   Given i load jdbc driver 'org.sqlite.JDBC'
     * ```
     *
     * @param driver String
     */
    @Given("^[I|i] load jdbc driver '(.*?)'$")
    override fun load(driver: String) {
        super.load(driver)
    }

    /**
     * Opens a connection to the jdbc database.
     *
     * ```
     * Scenario: An example of a database connection
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     * ```
     * ```
     * Scenario: An example of a database connection
     *   Given i load jdbc driver 'org.sqlite.JDBC'
     *   Then i am connecting to database 'jdbc:sqlite::memory:'
     * ```
     *
     * @param jdbc String
     */
    @Given("^[I|i] am connecting to database '(jdbc\\:.*?)'$")
    fun connect(jdbc: String) {
        super.connect(jdbc, null)
    }

    /**
     * Opens a connection to the jdbc database with predefined parameters.
     *
     * ```
     * Scenario: An example of connecting to a database with parameters
     *   Given i am connecting to database 'jdbc:sqlite::memory:' with parameters:
     *     | encoding | utf8 |
     * ```
     * ```
     * Scenario: An example of connecting to a database with parameters
     *   Given i load jdbc driver 'org.sqlite.JDBC'
     *   Then i am connecting to database 'jdbc:sqlite::memory:' with parameters:
     *     | encoding | utf8 |
     * ```
     *
     * @param jdbc String
     * @param dataTable DataTable?
     */
    @Given("^[I|i] am connecting to database '(jdbc\\:.*?)' with parameters:$")
    override fun connect(jdbc: String, dataTable: DataTable?) {
        super.connect(jdbc, dataTable)
    }

    /**
     * Closes the connection to the database.
     *
     * ```
     * Scenario: An example of closing a connection to a database
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then close database connection
     * ```
     */
    @Then("^[C|c]lose database connection$")
    override fun disconnect() {
        super.disconnect()
    }

    /**
     * Sets a timeout for executing a request.
     *
     * ```
     * Scenario: An example of setting the maximum query execution time
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then i set the maximum query execution time to '2' seconds
     * ```
     *
     * @param seconds Int
     */
    @Then("^[I|i] set the maximum query execution time to '(\\d+)' second[s]?$")
    override fun timeout(seconds: Int) {
        super.timeout(seconds)
    }

    /**
     * Executes a database query.
     *
     * ```
     * Scenario: An example of executing a database query
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
     * ```
     *
     * @param query String
     */
    @Then("^[E|e]xecute query '(.*?)'$")
    override fun execute(query: String) {
        super.execute(query)
    }

    /**
     * Executes a database query.
     *
     * ```
     * Scenario: An example of executing a database query
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query:
     *     """
     *     CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT);
     *     """
     * ```
     *
     * @param query String
     */
    @Then("^[E|e]xecute query:$")
    override fun execute(query: DocString) {
        super.execute(query)
    }

    /**
     * Executes database queries from a file.
     *
     * ```
     * Scenario: An example of executing database queries from a file
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query from 'artifacts/sql/example.sql'
     * ```
     *
     * @param path String
     */
    @Then("^[E|e]xecute query from '(.*?)'$")
    override fun executeFrom(path: String) {
        super.executeFrom(path)
    }

    /**
     * Checks the query result for a match.
     *
     * ```
     * Scenario: An example of checking if the result of a query matches
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query from 'artifacts/sql/example.sql'
     *   And execute query 'SELECT * FROM example'
     *   Then query result matches:
     *     | localAS | remoteAS |
     *     | 65551   | 65551    |
     *     | 65551   | 65552    |
     * ```
     *
     * @param dataTable DataTable
     */
    @And("^[Q|q]uery result matches:$")
    override fun match(dataTable: DataTable) {
        super.match(dataTable)
    }

    /**
     * Checks if the table exists.
     *
     * ```
     * Scenario: An example of checking a table for existence
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
     *   And table 'example' exists
     * ```
     * ```
     * Scenario: An example of checking a table for non-existence
     *   Given i am connecting to database 'jdbc:sqlite::memory:'
     *   Then execute query 'DROP TABLE IF EXISTS example'
     *   And table 'example' does not exists
     * ```
     *
     * @param table String
     * @param not String?
     */
    @When("^[T|t]able '(.*?)'( does not)? exists$")
    fun tableExists(table: String, not: String?) {
        super.exists(table, not !== null)
    }
}
