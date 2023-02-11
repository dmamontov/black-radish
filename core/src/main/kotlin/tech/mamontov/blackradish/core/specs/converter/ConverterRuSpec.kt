package tech.mamontov.blackradish.core.specs.converter

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseRuSpec

/**
 * Converter spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class ConverterRuSpec : Logged, ConverterSpec() {
    /**
     * Преобразует результат по шаблону.
     *
     * ```
     * Сценарий: Пример преобразования по шаблону
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     * ```
     *
     * @param templatePath String
     */
    @Когда("^[Я|я] преобразовываю результат по шаблону '(.*?)'$")
    override fun template(templatePath: String) {
        super.template(templatePath)
    }

    /**
     * Проверяет, содержит ли преобразованный результат не менее `size` записей.
     *
     * ```
     * Сценарий: Пример проверки количество записей в json на равенство
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда результат содержит '2' записи
     * ```
     * ```
     * Сценарий: Пример проверки количество записей в json на минимальное количество
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда результат содержит не менее '2' записей
     * ```
     * ```
     * Сценарий: Пример проверки количество записей в yaml на равенство
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда результат содержит '2' записи
     * ```
     * ```
     * Сценарий: Пример проверки количество записей в yaml на минимальное количество
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда результат содержит не менее '2' записей
     * ```
     * ```
     * Сценарий: Пример проверки количество записей преобразованных по шаблону на равенство
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем результат содержит '2' записи
     * ```
     * ```
     * Сценарий: Пример проверки количество записей преобразованных по шаблону на минимальное количество
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем результат содержит не менее '2' записей
     * ```
     *
     * @param atLeast String?
     * @param size Int
     * @param suffix String
     */
    @Тогда("^[Р|р]езультат содержит( не менее)? '(\\d+)' запис(ь|и|ей)$")
    fun size(atLeast: String?, size: Int, suffix: String) {
        super.size(atLeast !== null, size)
    }

    /**
     * Проверяет, равна ли сумма элементов по пути json в результате преобразования.
     *
     * ```
     * Сценарий: Пример суммирования значений в json по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда сумма '$..remoteAS' в результате равна '131103.0'
     * ```
     * ```
     * Сценарий: Пример суммирования значений в yaml по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда сумма '$..remoteAS' в результате равна '131103.0'
     * ```
     * ```
     * Сценарий: Пример суммирования значений в xml по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда сумма '$.root.element.*.remoteAS' в результате равна '131103.0'
     * ```
     * ```
     * Сценарий: Пример суммирования значений в преобразованных данных по шаблону и json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем сумма '$..remoteAS' в результате равна '131103.0'
     * ```
     *
     * @param jsonPath String
     * @param number String
     */
    @Тогда("^[С|с]умма '(.*?)' в результате равна '(.*?)'$")
    override fun sum(jsonPath: String, number: String) {
        super.sum(jsonPath, number)
    }

    /**
     * Проверяет результат преобразования на соответствие условиям.
     *
     * ```
     * Сценарий: Пример проверки результата преобразования json по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда в результате:
     *     | $.[0].localAS  | равно         | 65551 |
     *     | $.[0].remoteAS | больше        | 65550 |
     *     | $.[1].localAS  | соответствует | ^\d+$ |
     *     | $.[1].remoteAS | содержит      | 65    |
     * ```
     * ```
     * Сценарий: Пример проверки результата преобразования yaml по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда в результате:
     *     | $.[0].localAS  | равно         | 65551 |
     *     | $.[0].remoteAS | больше        | 65550 |
     *     | $.[1].localAS  | соответствует | ^\d+$ |
     *     | $.[1].remoteAS | содержит      | 65    |
     * ```
     * ```
     * Сценарий: Пример проверки результата преобразования xml по json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда в результате:
     *     | $.root.element.[0].localAS  | равно         | 65551 |
     *     | $.root.element.[0].remoteAS | больше        | 65550 |
     *     | $.root.element.[1].localAS  | соответствует | ^\d+$ |
     *     | $.root.element.[1].remoteAS | содержит      | 65    |
     * ```
     * ```
     * Сценарий: Пример проверки результата преобразования данных по шаблону и json path
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем в результате:
     *     | $.[0].localAS  | равно         | 65551 |
     *     | $.[0].remoteAS | больше        | 65550 |
     *     | $.[1].localAS  | соответствует | ^\d+$ |
     *     | $.[1].remoteAS | содержит      | 65    |
     * ```
     *
     * @param dataTable DataTable
     */
    @Тогда("^[В|в] результате:$")
    fun match(dataTable: DataTable) {
        super.match(dataTable, BaseRuSpec.toComparisonOperation)
    }

    /**
     * Сравнение оригинала результата.
     *
     * ```
     * Сценарий: Примеры проверки на равенство
     *   ...
     *   Тогда результат равен 'example'
     * ```
     * ```
     * Сценарий: Примеры проверки на неравенство
     *   ...
     *   Тогда результат не равен 'ex'
     * ```
     * ```
     * Сценарий: Примеры проверки по регулярному выражению
     *   ...
     *   Тогда результат соответствует '^[A-Za-z]+'
     * ```
     * ```
     * Сценарий: Примеры проверки вхождения подстроки
     *   ...
     *   Тогда результат содержит 'ex'
     * ```
     * ```
     * Сценарий: Примеры сравнения чисел
     *   ...
     *   Тогда результат меньше '200.2'
     *   И результат больше '100'
     * ```
     *
     * @param operation String
     * @param value String
     */
    @Тогда("^[Р|р]езультат (равен|соответствует|больше|меньше|содержит|не равен) '(.*?)'$")
    fun comparison(operation: String, value: String) {
        super.comparison(value, BaseRuSpec.toComparisonOperation[operation]!!)
    }

    /**
     * Сравнение оригинала результата.
     *
     * ```
     * Сценарий: Примеры проверки на равенство
     *   ...
     *   Тогда результат равен:
     *     """
     *     example
     *     """
     * ```
     * ```
     * Сценарий: Примеры проверки на неравенство
     *   ...
     *   Тогда результат не равен:
     *     """
     *     ex
     *     """
     * ```
     * ```
     * Сценарий: Примеры проверки по регулярному выражению
     *   ...
     *   Тогда результат соответствует:
     *     """
     *     ^[A-Za-z]+
     *     """
     * ```
     * ```
     * Сценарий: Примеры проверки вхождения подстроки
     *   ...
     *   Тогда результат содержит:
     *      """
     *      ex
     *      """
     * ```
     * ```
     * Сценарий: Примеры сравнения чисел
     *   ...
     *   Тогда результат меньше:
     *     """
     *     200.2
     *     """
     *   И результат больше:
     *     """
     *     100'
     *     """
     * ```
     *
     * @param operation String
     * @param value DocString
     */
    @Тогда("^[Р|р]езультат (равен|соответствует|больше|меньше|содержит|не равен):$")
    fun comparison(operation: String, value: DocString) {
        super.comparison(value.content.trim(), BaseRuSpec.toComparisonOperation[operation]!!)
    }

    /**
     * Проверяет содержимое по схеме.
     *
     * ```
     * Сценарий: Пример валидации json по json схеме
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда я проверяю содержимое по схеме 'artifacts/files/schema-json.json'
     * ```
     * ```
     * Сценарий: Пример валидации yaml по json схеме
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда я проверяю содержимое по схеме 'artifacts/files/schema-yaml.json'
     * ```
     * ```
     * Сценарий: Пример валидации xml по xsd схеме
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда я проверяю содержимое по схеме 'artifacts/files/schema-xml.xsd'
     * ```
     *
     * @param schemaPath String
     */
    @Тогда("^[Я|я] проверяю содержимое по схеме '(.*?)'$")
    override fun validate(schemaPath: String) {
        super.validate(schemaPath)
    }

    /**
     * Сохраняет оригинальное содержимое в переменную.
     *
     * ```
     * Сценарий: Пример сохранения оригинала json в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда я сохраняю оригинал в переменной 'ORIGINAL'
     *   Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.json}'
     * ```
     * ```
     * Сценарий: Пример сохранения оригинала yaml в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда я сохраняю оригинал в переменной 'ORIGINAL'
     *   Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'
     * ```
     * ```
     * Сценарий: Пример сохранения оригинала xml в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда я сохраняю оригинал в переменной 'ORIGINAL'
     *   Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'
     * ```
     * ```
     * Сценарий: Пример сохранения оригинала преобразованных данных по шаблону в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем я сохраняю оригинал в переменной 'ORIGINAL'
     *   И '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'
     * ```
     *
     * @param key String
     */
    @Тогда("^[Я|я] сохраняю оригинал в переменной '([A-Za-z0-9_]+)'$")
    fun save(key: String) {
        super.save(null, key, true)
    }

    /**
     * Сохраняет результат преобразования по пути json или целиком в переменную.
     *
     * ```
     * Сценарий: Пример сохранения результата преобразования json в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда я сохраняю результат в переменной 'RESULT'
     *   Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразования json по json path в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
     *   Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
     *   Затем '${LOCAL_AS}' равно '65551'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразования yaml в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда я сохраняю результат в переменной 'RESULT'
     *   Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразования yaml по json path в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
     *   Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
     *   Затем '${LOCAL_AS}' равно '65551'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразования xml в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда я сохраняю результат в переменной 'RESULT'
     *   Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразования xml по json path в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
     *   Тогда я сохраняю результат '$.root.element.[0].localAS' в переменной 'LOCAL_AS'
     *   Затем '${LOCAL_AS}' равно '65551'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразованных данных по шаблону в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем я сохраняю результат в переменной 'RESULT'
     *   И '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'
     * ```
     * ```
     * Сценарий: Пример сохранения результата преобразованных данных по шаблону и json path в переменную
     *   Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
     *   Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
     *   Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
     *   И '${LOCAL_AS}' равно '65551'
     * ```
     *
     * @param jsonPath String?
     * @param key String
     */
    @Тогда("^[Я|я] сохраняю результат( '(.*?)')? в переменной '([A-Za-z0-9_]+)'$")
    fun save(jsonPath: String?, key: String) {
        super.save(jsonPath, key, false)
    }
}
