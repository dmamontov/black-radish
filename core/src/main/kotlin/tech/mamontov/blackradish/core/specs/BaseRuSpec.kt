package tech.mamontov.blackradish.core.specs

import io.cucumber.java.ru.Если
import io.cucumber.java.ru.Иначе
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Пусть
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.utils.Logged

@Glue
open class BaseRuSpec() : Logged, BaseSpec() {
    @Пусть("^[Я|я] жду '(\\d+)' секунд[ы|у]?$")
    override fun wait(seconds: Int) {
        super.wait(seconds)
    }

    @Пусть("^[Я|я] сохраняю '(.*?)' в переменной '([A-Za-z0-9_]+)'$")
    override fun save(value: String, key: String) {
        super.save(value, key)
    }

    @Если("^'(.*?)' (равно|соответствует|больше|меньше|содержит|не равно) '(.*?)'$")
    fun comparison(first: String, operation: String, second: String) {
        val toComparisonOperation: Map<String, ComparisonOperation> = hashMapOf(
            "равно" to ComparisonOperation.IS,
            "соответствует" to ComparisonOperation.MATCHES,
            "больше" to ComparisonOperation.IS_HIGHER_THAN,
            "меньше" to ComparisonOperation.IS_LOWER_THAN,
            "содержит" to ComparisonOperation.CONTAINS,
            "не равно" to ComparisonOperation.IS_DIFFERENT_FROM,
        )

        toComparisonOperation[operation]?.let {
            super.comparison(first, it, second)
        }
    }

    @Если("^[Е|е]сли '(.*?)':$")
    override fun aspectIf(condition: String) {
    }

    @Иначе("^[И|и]наче [Е|е]сли '(.*?)':$")
    override fun aspectElseIf(condition: String) {
    }

    @Иначе("^[И|и]наче:$")
    override fun aspectElse() {
    }

    @Тогда("^[К|к]онец [Е|е]сли$")
    override fun aspectEndIf() {
    }

    @Пусть("^[Ц|ц]икл из '(.*?)':$")
    override fun aspectLoop(condition: String) {
    }

    @Пусть("^[Ц|ц]икл от '(-?\\d+)' до '(-?\\d+)':$")
    override fun aspectLoop(from: Int, to: Int) {
    }

    @Тогда("^[К|к]онец [Ц|ц]икла$")
    override fun aspectEndLoop() {
    }

    @Когда("^[Я|я] подключаю сценарий '(.*?)'( из функции '(.*?\\.feature)')?$")
    override fun aspectInclude(scenario: String, feature: String?) {
    }
}
