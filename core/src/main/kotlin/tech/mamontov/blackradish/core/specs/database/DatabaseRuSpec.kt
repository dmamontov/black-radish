package tech.mamontov.blackradish.core.specs.database

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Database spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
class DatabaseRuSpec : Logged, DatabaseSpec() {
    /**
     * Подключает jdbc драйвер.
     *
     * ```
     * Сценарий: Пример подключения jdbc драйвера
     *   Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
     * ```
     *
     * @param driver String
     */
    @Когда("^[Я|я] подключаю jdbc драйвер '(.*?)'$")
    override fun load(driver: String) {
        super.load(driver)
    }

    /**
     * Открывает соединение к базе данных по jdbc.
     *
     * ```
     * Сценарий: Пример подключения к базе данных
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     * ```
     * ```
     * Сценарий: Пример подключения к базе данных
     *   Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
     *   Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     * ```
     *
     * @param jdbc String
     */
    @Когда("^[Я|я] подключаюсь к базе данных '(jdbc\\:.*?)'$")
    fun connect(jdbc: String) {
        super.connect(jdbc, null)
    }

    /**
     * Открывает соединение к базе данных по jdbc с предустановленными параметрами.
     *
     * ```
     * Сценарий: Пример подключения к базе данных c параметрами
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:' с параметрами:
     *     | encoding | utf8 |
     * ```
     * ```
     * Сценарий: Пример подключения к базе данных c параметрами
     *   Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
     *   Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:' с параметрами:
     *     | encoding | utf8 |
     * ```
     *
     * @param jdbc String
     * @param dataTable DataTable?
     */
    @Когда("^[Я|я] подключаюсь к базе данных '(jdbc\\:.*?)' с параметрами:$")
    override fun connect(jdbc: String, dataTable: DataTable?) {
        super.connect(jdbc, dataTable)
    }

    /**
     * Закрывает соединение с базой данных.
     *
     * ```
     * Сценарий: Пример закрытия соединения к базе данных
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда закрываю соединение с базой данных
     * ```
     */
    @Тогда("^[З|з]акрываю соединение с базой данных$")
    override fun disconnect() {
        super.disconnect()
    }

    /**
     * Устанавливает таймаут для выполнения запроса.
     *
     * ```
     * Сценарий: Пример установки максимального времени выполнения запроса
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда устанавливаю максимальное время выполнения запроса '2' секунды
     * ```
     *
     * @param seconds Int
     */
    @Тогда("^[У|у]станавливаю максимальное время выполнения запроса '(\\d+)' секунд[ы|у]?$")
    override fun timeout(seconds: Int) {
        super.timeout(seconds)
    }

    /**
     * Выполняет запрос к базе данных.
     *
     * ```
     * Сценарий: Пример выполнения запроса к базе данных
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
     * ```
     *
     * @param query String
     */
    @Тогда("^[В|в]ыполняю запрос '(.*?)'$")
    override fun execute(query: String) {
        super.execute(query)
    }

    /**
     * Выполняет запрос к базе данных.
     *
     * ```
     * Сценарий: Пример выполнения запроса к базе данных
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос:
     *     """
     *     CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT);
     *     """
     * ```
     *
     * @param query String
     */
    @Тогда("^[В|в]ыполняю запрос:$")
    override fun execute(query: DocString) {
        super.execute(query)
    }

    /**
     * Выполняет запросы к базе данных из файла.
     *
     * ```
     * Сценарий: Пример выполнения запросов к базе данных из файла
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос из 'artifacts/sql/example.sql'
     * ```
     *
     * @param path String
     */
    @Тогда("^[В|в]ыполняю запрос из '(.*?)'$")
    override fun executeFrom(path: String) {
        super.executeFrom(path)
    }

    /**
     * Проверяет результат запроса на соответствие.
     *
     * ```
     * Сценарий: Пример проверки соответствия результата запроса
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос из 'artifacts/sql/example.sql'
     *   И выполняю запрос 'SELECT * FROM example'
     *   И результат запроса соответствует:
     *     | localAS | remoteAS |
     *     | 65551   | 65551    |
     *     | 65551   | 65552    |
     * ```
     * ```
     * Сценарий: Пример проверки соответствия результата запроса на запись
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос из 'artifacts/sql/example.sql'
     *   И выполняю запрос 'INSERT INTO example (localAS, remoteAS) VALUES (65552, 65553), (65553, 65554)'
     *   И результат запроса соответствует:
     *    | updateCount |
     *    | 2           |
     * ```
     *
     * @param dataTable DataTable
     */
    @И("^[Р|р]езультат запроса соответствует:$")
    override fun match(dataTable: DataTable) {
        super.match(dataTable)
    }

    /**
     * Проверяет таблицу на существование
     *
     * ```
     * Сценарий: Пример проверки таблицы на существование
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
     *   И таблица 'example' существует
     * ```
     * ```
     * Сценарий: Пример проверки таблицы на не существование
     *   Когда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
     *   Тогда выполняю запрос 'DROP TABLE IF EXISTS example'
     *   И таблица 'example' не существует
     * ```
     *
     * @param table String
     * @param not String?
     */
    @Когда("^[Т|т]аблица '(.*?)'( не)? существует$")
    fun exists(table: String, not: String?) {
        super.exists(table, not !== null)
    }
}
