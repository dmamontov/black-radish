package tech.mamontov.blackradish.csvdb.specs

import io.cucumber.datatable.DataTable
import io.cucumber.java.ru.Пусть
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Csv database spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
class CsvDbRuSpec : Logged, CsvDbSpec() {
    /**
     * Открывает соединение к csv базе данных по jdbc.
     *
     * ```
     * Сценарий: Пример подключения к базе данных
     *   Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
     * ```
     * ```
     * Сценарий: Пример подключения к базе данных в zip архиве
     *   Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database/example.zip'
     * ```
     *
     * @param path String
     */
    @Пусть("^[Я|я] подключаюсь к базе данных 'csv' по пути '(.*?)'$")
    fun connect(path: String) {
        super.connect(path, null)
    }

    /**
     * Открывает соединение к базе данных по jdbc с предустановленными параметрами.
     *
     * ```
     * Сценарий: Пример подключения к базе данных с параметрами
     *   Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
     *     | charset | UTF-8 |
     * ```
     *
     * @param path String
     * @param dataTable DataTable
     */
    @Пусть("^[Я|я] подключаюсь к базе данных 'csv' по пути '(.*?)' с параметрами:$")
    fun connectWithParameters(path: String, dataTable: DataTable) {
        super.connect(path, dataTable)
    }
}
