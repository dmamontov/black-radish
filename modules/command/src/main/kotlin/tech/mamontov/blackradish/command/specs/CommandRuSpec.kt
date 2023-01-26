package tech.mamontov.blackradish.command.specs

import io.cucumber.docstring.DocString
import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class CommandRuSpec : Logged, CommandSpec() {
    @Когда("^[Я|я] устанавливаю максимальное время выполнения команды '(\\d+)' секунд$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    @Когда("^[Я|я] запускаю команду '(.*?)' локально$")
    override fun run(command: String) {
        super.run(command)
    }

    @Когда("^[Я|я] запускаю локальную команду '(.*?)' в фоне$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    @И("^[С|с]охраняю идентификатор команды в переменную '([A-Za-z0-9_]+)'$")
    override fun saveBackgroundId(variable: String) {
        super.saveBackgroundId(variable)
    }

    @Тогда("^[Я|я] завершаю команду запущенную в фоне( с идентификатором '(.*?)')?$")
    override fun closeBackground(variable: String?) {
        super.closeBackground(variable)
    }

    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод локальной команды '(.*?)' содержит '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Тогда("^[С|с]охраняю результат выполнения команды в переменную '([A-Za-z0-9_]+)'$")
    override fun save(variable: String) {
        super.save(variable)
    }

    @Когда("^[Р|р]азбираю результат выполнения команды используя шаблон '(.*?)'$")
    override fun parseResult(templatePath: String) {
        super.parseResult(templatePath)
    }

    @Тогда("^[К|к]од выхода команды должен быть '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }

    @Тогда("^[Р|р]езультат выполнения команды соответствует:$")
    override fun check(content: DocString) {
        super.check(content)
    }
}
