package tech.mamontov.blackradish.office.specs.excel

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class ExcelRuSpec : Logged, ExcelSpec() {
    @Когда("^[Я|я] создаю пустой (xls[x]?) файл '(.*?\\.xls[x]?)'$")
    override fun createEmpty(format: String, path: String) {
        super.createEmpty(format, path)
    }

    @Тогда("^[В|в] xls[x]? добавляю новый лист '(.*?)' содержащий:$")
    override fun listAdd(name: String, dataTable: DataTable) {
        super.listAdd(name, dataTable)
    }

    @Тогда("^[В|в] xls[x]? листе '(.*?)' объединяю ячейки от '([A-Z]+\\d+)' до '([A-Z]+\\d+)'$")
    override fun cellsMerge(name: String, from: String, to: String) {
        super.cellsMerge(name, from, to)
    }

    @Тогда("^[В|в] xls[x]? листе '(.*?)' записываю( формулу)? '(.*?)' в ячейку '([A-Z]+\\d+)'$")
    fun cellWrite(name: String, formula: String?, value: String, cell: String) {
        super.cellWrite(name, value, cell, formula !== null)
    }

    @Когда("^[Я|я] открываю (xls[x]?) файл '(.*?\\.xls[x]?)'$")
    override fun read(format: String, path: String) {
        super.read(format, path)
    }
}
