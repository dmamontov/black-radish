package tech.mamontov.blackradish.command.specs

import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged

@Glue
class CommandRuSpec : Logged, CommandSpec() {

    @Когда("^[Я|я] устанавливаю максимальное время выполнения команды '(\\d+)' секунд$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    @Когда("^[Я|я] запускаю команду '(.*?)' локально$")
    override fun run(command: String): CommandResult? {
        return super.run(command)
    }

    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод локальной команды '(.*?)' содержит '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Тогда("^[Я|я] сохраняю результат выполнения команды в переменную '([A-Za-z0-9_]+)'$")
    override fun save(variable: String) {
        super.save(variable)
    }

    @Тогда("^[Я|я] проверяю, что код выхода команды '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }
}
