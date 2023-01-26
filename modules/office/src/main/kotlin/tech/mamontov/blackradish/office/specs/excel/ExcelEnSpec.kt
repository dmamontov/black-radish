package tech.mamontov.blackradish.office.specs.excel

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class ExcelEnSpec : Logged, ExcelSpec() {
    @When("^[I|i] create an empty (xls[x]?) file '(.*?\\.xls[x]?)'$")
    override fun createEmpty(format: String, path: String) {
        super.createEmpty(format, path)
    }

    @Then("^[I|i]n xls[x]? add a new sheet '(.*?)' containing:$")
    override fun listAdd(name: String, dataTable: DataTable) {
        super.listAdd(name, dataTable)
    }

    @Then("^[I|i]n xls[x]? sheet '(.*?)' merge cells from '([A-Z]+\\d+)' to '([A-Z]+\\d+)'$")
    override fun cellsMerge(name: String, from: String, to: String) {
        super.cellsMerge(name, from, to)
    }

    @Then("^[I|i]n xls[x]? sheet '(.*?)' write( the formula)? '(.*?)' in the cell '([A-Z]+\\d+)'$")
    fun cellWrite(name: String, formula: String?, value: String, cell: String) {
        super.cellWrite(name, value, cell, formula !== null)
    }

    @When("^[I|i] open (xls[x]?) file '(.*?\\.xls[x]?)'$")
    override fun read(format: String, path: String) {
        super.read(format, path)
    }
}
