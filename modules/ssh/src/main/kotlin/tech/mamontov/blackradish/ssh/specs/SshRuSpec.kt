package tech.mamontov.blackradish.ssh.specs

import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.ssh.enumerated.AuthType

@Glue
class SshRuSpec : Logged, SshSpec() {
    @Когда("^[Я|я] открываю ssh соединение к '(.*?)(:([0-9]{2,}))?' от имени '(.*?)' с (паролем|ключом) '(.*?)'$")
    fun connect(host: String, port: Int?, user: String, auth: String, token: String) {
        val toAuthType: Map<String, AuthType> = hashMapOf(
            "паролем" to AuthType.PASSWORD,
            "ключом" to AuthType.KEY,
        )

        toAuthType[auth]?.let {
            super.connect(host, port ?: 22, user, it, token)
        }
    }

    @Тогда("^[Я|я] закрываю ssh соединение$")
    override fun disconnect() {
        super.disconnect()
    }

    @Тогда("^[Я|я] запускаю команду '(.*?)' по ssh$")
    override fun run(command: String): CommandResult? {
        return super.run(command)
    }

    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод ssh команды '(.*?)' содержит '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }
}
