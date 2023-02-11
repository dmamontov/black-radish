package tech.mamontov.blackradish.core.specs.base

import io.cucumber.java.ru.Если
import io.cucumber.java.ru.Иначе
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Пусть
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Base spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
open class BaseRuSpec() : Logged, BaseSpec() {

    companion object {
        val toComparisonOperation: Map<String, ComparisonOperation> = hashMapOf(
            "равно" to ComparisonOperation.IS,
            "равен" to ComparisonOperation.IS,
            "соответствует" to ComparisonOperation.MATCHES,
            "больше" to ComparisonOperation.IS_HIGHER_THAN,
            "меньше" to ComparisonOperation.IS_LOWER_THAN,
            "содержит" to ComparisonOperation.CONTAINS,
            "не равно" to ComparisonOperation.IS_DIFFERENT_FROM,
            "не равен" to ComparisonOperation.IS_DIFFERENT_FROM,
        )
    }

    /**
     * Ожидание n секунд.
     *
     * ```
     * Сценарий: Пример ожидания
     *   Допустим я жду '1' секунду
     * ```
     *
     * @param seconds Int
     */
    @Пусть("^[Я|я] жду '(\\d+)' секунд[ы|у]?$")
    override fun wait(seconds: Int) {
        super.wait(seconds)
    }

    /**
     * Сохранение значения в переменную.
     *
     * ```
     * Сценарий: Пример сохранения значения в переменную
     *   Если я сохраняю 'example' в переменной 'ONE'
     * ```
     *
     * @param value String
     * @param key String
     */
    @Пусть("^[Я|я] сохраняю '(.*?)' в переменной '([A-Za-z0-9_]+)'$")
    override fun save(value: String, key: String) {
        super.save(value, key)
    }

    /**
     * Сравнение двух значений.
     * Числа сравниваются как double.
     *
     * ```
     * Сценарий: Примеры проверки на равенство
     *   Если '100' равно '100'
     *   И 'example' равно 'example'
     * ```
     * ```
     * Сценарий: Примеры проверки на неравенство
     *   Если '100' не равно '200'
     *   И 'example' не равно 'ex'
     * ```
     * ```
     * Сценарий: Примеры проверки по регулярному выражению
     *   Если '200' соответствует '^\d+'
     *   И 'example' соответствует '^[A-Za-z]+'
     * ```
     * ```
     * Сценарий: Примеры проверки вхождения подстроки
     *   Если '100' содержит '10'
     *   И 'example' содержит 'ex'
     * ```
     * ```
     * Сценарий: Примеры сравнения чисел
     *   Если '100.1' больше '100'
     *   И '200' меньше '200.2'
     *   И '0xAF' меньше '176'
     * ```
     *
     * @param first String
     * @param operation String
     * @param second String
     */
    @Если("^'(.*?)' (равно|соответствует|больше|меньше|содержит|не равно) '(.*?)'$")
    fun comparison(first: String, operation: String, second: String) {
        super.comparison(first, toComparisonOperation[operation]!!, second)
    }

    /**
     * Условный оператор - Если.
     * Условие пишется на языке kotlin.
     *
     * ```
     * Сценарий: Пример использования - Если, Конец Если
     *   Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
     *   * Если '${NUMBER} == 1.0':
     *     Тогда '${NUMBER}' равно '1.0'
     *   * Конец Если
     * ```
     *
     * @param condition String
     */
    @Если("^[Е|е]сли '(.*?)':$")
    override fun aspectIf(condition: String) {
    }

    /**
     * Условный оператор - Иначе Если.
     * Условие пишется на языке kotlin.
     *
     * ```
     * Сценарий: Пример использования - Если, Иначе Если, Конец Если
     *   Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
     *   * Если '${NUMBER} == 1.0':
     *     Тогда '${NUMBER}' равно '1.0'
     *   * Иначе Если '${NUMBER} == 2.0':
     *     Тогда '${NUMBER}' равно '2.0'
     *   * Конец Если
     * ```
     *
     * @param condition String
     */
    @Иначе("^[И|и]наче [Е|е]сли '(.*?)':$")
    override fun aspectElseIf(condition: String) {
    }

    /**
     * Условный оператор - Иначе.
     *
     * ```
     * Сценарий: Пример использования - Если, Иначе, Конец Если
     *   Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
     *   * Если '${NUMBER} == 1.0':
     *     Тогда '${NUMBER}' равно '1.0'
     *   * Иначе:
     *     Тогда '${NUMBER}' больше '1.0'
     *   * Конец Если
     * ```
     */
    @Иначе("^[И|и]наче:$")
    override fun aspectElse() {
    }

    /**
     * Условный оператор - Конец Если.
     *
     * ```
     * Сценарий: Пример использования - Если, Конец Если
     *   Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
     *   * Если '${NUMBER} == 1.0':
     *     Тогда '${NUMBER}' равно '1.0'
     *   * Конец Если
     * ```
     */
    @Тогда("^[К|к]онец [Е|е]сли$")
    override fun aspectEndIf() {
    }

    /**
     * Цикл - совместный.
     *
     * ```
     * Сценарий: Пример циклической генерации из списка
     *   * Цикл из 'first,second,three':
     *     * я сохраняю '${loop.value}' в переменной 'VAR'
     *   * Конец Цикла
     * ```
     * ```
     * Сценарий: Пример циклической генерации из списка, включая переменную
     *   Когда я сохраняю '1' в переменной 'NUMBER'
     *   * Цикл из '${NUMBER},first,second':
     *     * я сохраняю '${loop.value}' в переменной 'VAR'
     *   * Конец Цикла
     * ```
     *
     * @param condition String
     */
    @Пусть("^[Ц|ц]икл из '(.*?)':$")
    override fun aspectLoop(condition: String) {
    }

    /**
     * Цикл - со счётчиком.
     *
     * ```
     * Сценарий: Пример циклической генерации шагов циклом со счётчиком
     *   * Цикл от '1' до '3':
     *     * я сохраняю '${loop.value}' в переменной 'NUMBER'
     *   * Конец Цикла
     * ```
     * ```
     * Сценарий: Пример циклической генерации шагов циклом со счётчиком в обратном порядке
     *   * Цикл от '3' до '1':
     *     * я сохраняю '${loop.value}' в переменной 'NUMBER'
     *   * Конец Цикла
     * ```
     *
     * @param from Int
     * @param to Int
     */
    @Пусть("^[Ц|ц]икл от '(-?\\d+)' до '(-?\\d+)':$")
    override fun aspectLoop(from: Int, to: Int) {
    }

    /**
     * Конец цикла
     *
     * ```
     * Сценарий: Пример циклической генерации шагов циклом со счётчиком
     *   * Цикл от '1' до '3':
     *     * я сохраняю '${loop.value}' в переменной 'NUMBER'
     *   * Конец Цикла
     * ```
     */
    @Тогда("^[К|к]онец [Ц|ц]икла$")
    override fun aspectEndLoop() {
    }

    /**
     * Переиспользование сценария.
     *
     * ```
     * Сценарий: Пример создания переменной
     *   Если я сохраняю '2' в переменной 'NUMBER'
     *
     * Сценарий: Пример подключения сценария из текущей функции
     *   Если я сохраняю '1' в переменной 'NUMBER'
     *   Тогда '${NUMBER}' равно '1'
     *   Если я подключаю сценарий 'Пример создания переменной'
     *   Тогда '${NUMBER}' равно '2'
     * ```
     * ```
     * Функция: Подключаемые сценарии
     *   Сценарий: Пример создания переменной
     *     Если я сохраняю '3' в переменной 'NUMBER'
     *
     * Функция: Основная функция
     *   Сценарий: Пример подключения сценария из другой функции
     *     Если я сохраняю '1' в переменной 'NUMBER'
     *     Тогда '${NUMBER}' равно '1'
     *     Если я подключаю сценарий 'Пример создания переменной' из функции 'classpath:include.feature'
     *     Тогда '${NUMBER}' равно '3'
     * ```
     *
     * @param scenario String
     * @param feature String?
     */
    @Когда("^[Я|я] подключаю сценарий '(.*?)'( из функции '(.*?\\.feature)')?$")
    override fun aspectInclude(scenario: String, feature: String?) {
    }
}
