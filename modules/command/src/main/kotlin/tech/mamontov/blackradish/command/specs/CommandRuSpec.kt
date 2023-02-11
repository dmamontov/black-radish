package tech.mamontov.blackradish.command.specs

import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Command spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
class CommandRuSpec : Logged, CommandSpec() {
    /**
     * Установка максимальное время выполнения команды.
     *
     * ```
     * Сценарий: Пример запуска команды с установленным максимальным временем выполнения
     *   Когда я устанавливаю максимальное время выполнения команды '10' секунд
     *   Тогда я запускаю команду 'echo "example"' локально
     * ```
     *
     * @param seconds Long
     */
    @Когда("^[Я|я] устанавливаю максимальное время выполнения команды '(\\d+)' секунд$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    /**
     * Запуск команды.
     *
     * ```
     * Сценарий: Пример запуска команды
     *   Когда я запускаю команду 'echo "example"' локально
     * ```
     *
     * @param command String
     */
    @Когда("^[Я|я] запускаю команду '(.*?)' локально$")
    override fun run(command: String) {
        super.run(command)
    }

    /**
     * Запуск команды в фоне.
     *
     * ```
     * Сценарий: Пример запуска команды в фоне
     *   Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне
     * ```
     *
     * @param command String
     */
    @Когда("^[Я|я] запускаю локальную команду '(.*?)' в фоне$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    /**
     * Сохранение идентификатора команды запущенной в фоне в переменную.
     *
     * ```
     * Сценарий: Пример сохранения идентификатора в переменную
     *   Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 2 && echo "world"' в фоне
     *   Тогда сохраняю идентификатор команды в переменную 'UUID'
     * ```
     *
     * @param variable String
     */
    @И("^[С|с]охраняю идентификатор команды в переменную '([A-Za-z0-9_]+)'$")
    override fun saveBackgroundUuid(variable: String) {
        super.saveBackgroundUuid(variable)
    }

    /**
     * Завершает команду запущенную в фоне.
     *
     * ```
     * Сценарий: Пример завершения команды запущенной в фоне
     *   Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 2 && echo "world"' в фоне
     *   Тогда сохраняю идентификатор команды в переменную 'UUID'
     *   Затем я завершаю команду запущенную в фоне с идентификатором '${UUID}'
     * ```
     * ```
     * Сценарий: Пример завершения команды запущенной в фоне
     *   Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 2 && echo "world"' в фоне
     *   Тогда я завершаю команду запущенную в фоне
     * ```
     *
     * @param variable String?
     */
    @Тогда("^[Я|я] завершаю команду запущенную в фоне( с идентификатором '(.*?)')?$")
    override fun terminateBackground(variable: String?) {
        super.terminateBackground(variable)
    }

    /**
     * Запуск команды до появления содержимого в течении указанного времени.
     *
     * ```
     * Сценарий: Пример запуска команды до появления содержимого в течении указанного времени
     *   Затем менее чем через '10' секунд вывод локальной команды 'sleep 1 && printf "hello\nworld" && sleep 10' содержит 'world'
     * ```
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод локальной команды '(.*?)' содержит '(.*?)'$")
    override fun search(timeout: Int, command: String, search: String) {
        super.search(timeout, command, search)
    }

    /**
     * Проверка кода завершения.
     *
     * ```
     * Сценарий: Пример проверки кода выхода команды
     *   Когда я запускаю команду 'echo "first"' локально
     *   Тогда код выхода команды должен быть '0'
     * ```
     *
     * @param code Long
     */
    @Тогда("^[К|к]од выхода команды должен быть '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }
}
