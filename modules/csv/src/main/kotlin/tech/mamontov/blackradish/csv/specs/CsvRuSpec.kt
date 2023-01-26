package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Пусть
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class CsvRuSpec : Logged, CsvSpec() {

    @Пусть("^[Я|я] устанавливаю для csv разделитель '(.*?)'$")
    override fun delimiter(delimiter: String) {
        super.delimiter(delimiter)
    }

    @Пусть("^[Я|я] устанавливаю для csv межстрочный разделитель '(.*?)'$")
    override fun separator(separator: String) {
        super.separator(separator)
    }

    @Когда("^[Я|я] создаю csv файл '(.*?)' содержащий:$")
    override fun create(path: String, content: DataTable) {
        super.create(path, content)
    }

    @Когда("^[Я|я] открываю csv файл '(.*?)'$")
    fun read(path: String) {
        super.read(path, false, null)
    }

    @Когда("^[Я|я] открываю csv файл '(.*?)' с заголовками в первой строке$")
    override fun readWithoutFirstLine(path: String) {
        super.readWithoutFirstLine(path)
    }

    @Когда("^[Я|я] открываю csv файл '(.*?)' с заголовками:$")
    override fun readWithHeaders(path: String, dataTable: DataTable) {
        super.readWithHeaders(path, dataTable)
    }

    @Тогда("^[С|с]одержимое csv файла( не)? соответствует:$")
    fun contentIs(not: String?, content: DataTable) {
        super.contentIs(content, not !== null)
    }

    @Пусть("^[Я|я] подключаюсь к базе данных 'csv' по пути '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    @Пусть("^[Я|я] подключаюсь к базе данных 'csv' по пути '(.*?)' с параметрами:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
