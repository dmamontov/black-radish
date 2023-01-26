package tech.mamontov.blackradish.core.specs.parser

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseRuSpec

@Glue
@Suppress("UNUSED_PARAMETER")
class ParserRuSpec : Logged, ParserSpec() {
    @Тогда("^[Р|р]езультат содержит( не менее)? '(\\d+)' запис(ь|и|ей)?$")
    fun resultCount(min: String?, count: Int, ending: String?) {
        super.resultCount(min !== null, count)
    }

    @Тогда("^[С|с]умма '(.*?)' в результате равна '(.*?)'$")
    override fun resultSum(column: String, sum: String) {
        super.resultSum(column, sum)
    }

    @Тогда("^[В|в] результате:$")
    fun resultMatch(dataTable: DataTable) {
        super.resultMatch(dataTable, BaseRuSpec.toComparisonOperation)
    }

    @Тогда("^[Я|я] проверяю результат по схеме '(.*?)'$")
    override fun resultValidate(path: String) {
        super.resultValidate(path)
    }

    @Тогда("^[Я|я] сохраняю результат( '(.*?)')? в переменной '([A-Za-z0-9_]+)'$")
    override fun resultSave(column: String?, variable: String) {
        super.resultSave(column, variable)
    }
}
