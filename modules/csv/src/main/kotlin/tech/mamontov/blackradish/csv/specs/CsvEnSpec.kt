package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class CsvEnSpec : Logged, CsvSpec() {

    @Given("^[I|i] set csv delimiter '(.*?)'$")
    override fun delimiter(delimiter: String) {
        super.delimiter(delimiter)
    }

    @Given("^[I|i] set csv line separator '(.*?)'$")
    override fun separator(separator: String) {
        super.separator(separator)
    }

    @When("^[I|i] create a csv file '(.*?)' containing:$")
    override fun create(path: String, content: DataTable) {
        super.create(path, content)
    }

    @When("^[I|i] open csv file '(.*?)'$")
    fun read(path: String) {
        super.read(path, false, null)
    }

    @When("^[I|i] open csv file '(.*?)' with headers in first line$")
    override fun readWithoutFirstLine(path: String) {
        super.readWithoutFirstLine(path)
    }

    @When("^[I|i] open csv file '(.*?)' with headers:$")
    override fun readWithHeaders(path: String, dataTable: DataTable) {
        super.readWithHeaders(path, dataTable)
    }

    @Then("^[C|c]sv file content( does not)? match:$")
    fun contentIs(not: String?, content: DataTable) {
        super.contentIs(content, not !== null)
    }

    @Given("^[I|i] connect to database 'csv' by path '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    @Given("^[I|i] connect to database 'csv' by path '(.*?)' with the parameters:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
