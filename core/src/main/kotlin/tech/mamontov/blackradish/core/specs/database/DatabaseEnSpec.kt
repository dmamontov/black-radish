package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class DatabaseEnSpec : Logged, DatabaseSpec() {
    @Then("^[C|c]lose database connection$")
    override fun disconnect() {
        super.disconnect()
    }

    @Then("^[E|e]xecute database query '(.*?)'$")
    override fun execute(query: String) {
        super.execute(query)
    }

    @Then("^[E|e]xecute database query from '(.*?)'$")
    override fun executeFrom(path: String) {
        super.executeFrom(path)
    }

    @And("^[R|r]esult of the database query is:$")
    override fun resultIs(dataTable: DataTable) {
        super.resultIs(dataTable)
    }

    @When("^[T|t]able '(.*?)'( does not)? exists in database$")
    fun tableExists(table: String, not: String?) {
        super.tableExists(table, not !== null)
    }
}
