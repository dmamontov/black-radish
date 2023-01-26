package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class DatabaseRuSpec : Logged, DatabaseSpec() {
    @Тогда("^[З|з]акрываю соединение с базой данных$")
    override fun disconnect() {
        super.disconnect()
    }

    @Тогда("^[В|в]ыполняю запрос к базе данных '(.*?)'$")
    override fun execute(query: String) {
        super.execute(query)
    }

    @Тогда("^[В|в]ыполняю запрос к базе данных из '(.*?)'$")
    override fun executeFrom(path: String) {
        super.executeFrom(path)
    }

    @И("^[Р|р]езультат запроса к базе данных соответствует:$")
    override fun resultIs(dataTable: DataTable) {
        super.resultIs(dataTable)
    }

    @Когда("^[Т|т]аблица '(.*?)'( не)? существует в базе данных$")
    fun tableExists(table: String, not: String?) {
        super.tableExists(table, not !== null)
    }
}
