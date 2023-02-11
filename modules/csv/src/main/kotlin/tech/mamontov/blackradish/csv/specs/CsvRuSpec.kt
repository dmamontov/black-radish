package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Пусть
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Csv spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
class CsvRuSpec : Logged, CsvSpec() {

    /**
     * Устанавливает разделитель.
     *
     * ```
     * Сценарий: Пример установки разделителя
     *   Пусть я устанавливаю для csv разделитель ';'
     * ```
     *
     * @param delimiter String
     */
    @Пусть("^[Я|я] устанавливаю для csv разделитель '(.*?)'$")
    override fun delimiter(delimiter: String) {
        super.delimiter(delimiter)
    }

    /**
     * Устанавливает межстрочный разделитель.
     *
     * ```
     * Сценарий: Пример установки межстрочного разделителя
     *   Пусть я устанавливаю для csv межстрочный разделитель '\r\n'
     * ```
     *
     * @param separator String
     */
    @Пусть("^[Я|я] устанавливаю для csv межстрочный разделитель '(.*?)'$")
    override fun lineSeparator(separator: String) {
        super.lineSeparator(separator)
    }

    /**
     * Создает файл с содержимым.
     *
     * ```
     * Сценарий: Пример создания *.csv файла
     *   Если я создаю csv файл 'artifacts/tmp/example.csv' содержащий:
     *     | #1 | example1 | 101 |
     *     | #2 | example2 | 102 |
     *     | #3 | example3 | 103 |
     *     | #4 | example4 | 104 |
     *     | #5 | example5 | 105 |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @Когда("^[Я|я] создаю csv файл '(.*?)' содержащий:$")
    override fun create(path: String, dataTable: DataTable) {
        super.create(path, dataTable)
    }

    /**
     * Открывает файл.
     *
     * ```
     * Сценарий: Пример чтения из файла
     *   Когда я открываю csv файл 'artifacts/files/example_first.csv'
     * ```
     *
     * @param path String
     */
    @Когда("^[Я|я] открываю csv файл '(.*?)'$")
    fun read(path: String) {
        super.`open`(path, false, null)
    }

    /**
     * Открывает файл, с указанием что в первой строке находятся заголовки.
     *
     * ```
     * Сценарий: Пример чтения из файла с заголовками в первой строке
     *   Когда я открываю csv файл 'artifacts/files/example_second.csv' с заголовками в первой строке
     * ```
     *
     * @param path String
     */
    @Когда("^[Я|я] открываю csv файл '(.*?)' с заголовками в первой строке$")
    override fun openWithoutFirstLine(path: String) {
        super.openWithoutFirstLine(path)
    }

    /**
     * Открывает файл, с указанием заголовков.
     *
     * ```
     * Сценарий: Пример чтения из файла с установленными заголовками
     *   Когда я открываю csv файл 'artifacts/files/example_first.csv' с заголовками:
     *     | localAS | remoteAS | remoteIp | routerId | status | uptime |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @Когда("^[Я|я] открываю csv файл '(.*?)' с заголовками:$")
    override fun openWithHeaders(path: String, dataTable: DataTable) {
        super.openWithHeaders(path, dataTable)
    }

    /**
     * Сравнивает содержимое.
     *
     * ```
     * Сценарий: Пример чтения из файла и сравнения содержимого
     *   Когда я открываю csv файл 'artifacts/files/example_first.csv'
     *   Тогда содержимое csv файла соответствует:
     *     | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
     *     | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
     *     | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |
     * ```
     * ```
     * Сценарий: Пример чтения из файла и сравнения содержимого
     *   Когда я открываю csv файл 'artifacts/files/example_first.csv'
     *   Тогда содержимое csv файла не соответствует:
     *     | #1 | example1 | 101 |
     *     | #2 | example2 | 102 |
     * ```
     *
     * @param not String?
     * @param dataTable DataTable
     */
    @Тогда("^[С|с]одержимое csv файла( не)? соответствует:$")
    fun contentIs(not: String?, dataTable: DataTable) {
        super.contentIs(dataTable, not !== null)
    }
}
