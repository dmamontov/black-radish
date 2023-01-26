package tech.mamontov.blackradish.dbf.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class DbfEnSpec : Logged, DbfSpec() {

    @Given("^[I|i] connect to database 'dbf' by path '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    @Given("^[I|i] connect to database 'dbf' by path '(.*?)' with the parameters:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
