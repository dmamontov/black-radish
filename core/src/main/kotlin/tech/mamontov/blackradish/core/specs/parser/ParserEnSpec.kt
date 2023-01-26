package tech.mamontov.blackradish.core.specs.parser

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseEnSpec

@Glue
class ParserEnSpec : Logged, ParserSpec() {
    @Then("^[R|r]esult contains( at least)? '(\\d+)' record[s]?$")
    fun resultCount(min: String?, count: Int) {
        super.resultCount(min !== null, count)
    }

    @Then("^[S|s]um of '(.*?)' results in '(.*?)'$")
    override fun resultSum(column: String, sum: String) {
        super.resultSum(column, sum)
    }

    @Then("^[A|a]s a result:$")
    fun resultMatch(dataTable: DataTable) {
        super.resultMatch(dataTable, BaseEnSpec.toComparisonOperation)
    }

    @Then("^[I|i] check the result according to the scheme '(.*?)'$")
    override fun resultValidate(path: String) {
        super.resultValidate(path)
    }

    @Then("^[I|i] save the result( '(.*?)')? in a variable '([A-Za-z0-9_]+)'$")
    override fun resultSave(column: String?, variable: String) {
        super.resultSave(column, variable)
    }
}
