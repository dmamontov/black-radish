package tech.mamontov.blackradish.dbf.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Пусть
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class DbfRuSpec : Logged, DbfSpec() {

    @Пусть("^[Я|я] подключаюсь к базе данных 'dbf' по пути '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    @Пусть("^[Я|я] подключаюсь к базе данных 'dbf' по пути '(.*?)' с параметрами:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
