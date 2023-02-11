package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Csv spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class CsvEnSpec : Logged, CsvSpec() {

    /**
     * Sets a delimiter.
     *
     * ```
     * Scenario: An example of setting a delimiter
     *   Given i set csv delimiter ';'
     * ```
     *
     * @param delimiter String
     */
    @Given("^[I|i] set csv delimiter '(.*?)'$")
    override fun delimiter(delimiter: String) {
        super.delimiter(delimiter)
    }

    /**
     * Sets the line separator.
     *
     * ```
     * Scenario: An example of setting the interline separator
     *   Given i set csv line separator '\r\n'
     * ```
     *
     * @param separator String
     */
    @Given("^[I|i] set csv line separator '(.*?)'$")
    override fun lineSeparator(separator: String) {
        super.lineSeparator(separator)
    }

    /**
     * Creates a file with content.
     *
     * ```
     * Scenario: An example of creating a *.csv file
     *   When i create a csv file 'artifacts/tmp/example.csv' containing:
     *     | #1 | example1 | 101 |
     *     | #2 | example2 | 102 |
     *     | #3 | example3 | 103 |
     *     | #4 | example4 | 104 |
     *     | #5 | example5 | 105 |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @When("^[I|i] create a csv file '(.*?)' containing:$")
    override fun create(path: String, dataTable: DataTable) {
        super.create(path, dataTable)
    }

    /**
     * Opens a file.
     *
     * ```
     * Scenario: Example of reading from a file
     *   When i open csv file 'artifacts/files/example_first.csv'
     * ```
     *
     * @param path String
     */
    @When("^[I|i] open csv file '(.*?)'$")
    fun read(path: String) {
        super.`open`(path, false, null)
    }

    /**
     * Opens the file, specifying that the first line contains the headers.
     *
     * ```
     * Scenario: Example of reading from a file with headers on the first line
     *   When i open csv file 'artifacts/files/example_second.csv' with headers in first line
     * ```
     *
     * @param path String
     */
    @When("^[I|i] open csv file '(.*?)' with headers in first line$")
    override fun openWithoutFirstLine(path: String) {
        super.openWithoutFirstLine(path)
    }

    /**
     * Opens a file with headers.
     *
     * ```
     * Scenario: Example of reading from a file with headers set
     *   When i open csv file 'artifacts/files/example_first.csv' with headers:
     *     | localAS | remoteAS | remoteIp | routerId | status | uptime |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @When("^[I|i] open csv file '(.*?)' with headers:$")
    override fun openWithHeaders(path: String, dataTable: DataTable) {
        super.openWithHeaders(path, dataTable)
    }

    /**
     * Compares content.
     *
     * ```
     * Scenario: Example of reading from a file and comparing contents
     *   When i open csv file 'artifacts/files/example_first.csv'
     *   Then csv file content match:
     *     | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
     *     | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
     *     | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |
     * ```
     * ```
     * Scenario: Example of reading from a file and comparing contents
     *   When i open csv file 'artifacts/files/example_first.csv'
     *   Then csv file content does not match:
     *     | #1 | example1 | 101 |
     *     | #2 | example2 | 102 |
     * ```
     *
     * @param not String?
     * @param dataTable DataTable
     */
    @Then("^[C|c]sv file content( does not)? match:$")
    fun contentIs(not: String?, dataTable: DataTable) {
        super.contentIs(dataTable, not !== null)
    }
}
