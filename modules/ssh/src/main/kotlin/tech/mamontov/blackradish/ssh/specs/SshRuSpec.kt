package tech.mamontov.blackradish.ssh.specs

import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.ssh.enumerated.AuthType

@Glue
class SshRuSpec : Logged, SshSpec() {
    @Когда("^[Я|я] открываю ssh соединение к '(.*?)(:([0-9]{2,}))?' от имени '(.*?)' с (паролем|ключом) '(.*?)'( и паролем '(.*?)')?$")
    fun connect(host: String, port: Int?, user: String, auth: String, token: String, passphrase: String?) {
        val toAuthType: Map<String, AuthType> = hashMapOf(
            "паролем" to AuthType.PASSWORD,
            "ключом" to AuthType.KEY,
        )

        toAuthType[auth]?.let {
            super.connect(host, port ?: 22, user, it, token, passphrase)
        }
    }

    @Тогда("^[Я|я] закрываю ssh соединение$")
    override fun disconnect() {
        super.disconnect()
    }

    @Тогда("^[Я|я] запускаю команду '(.*?)' по ssh$")
    override fun run(command: String) {
        super.run(command)
    }

    @Когда("^[Я|я] запускаю ssh команду '(.*?)' в фоне$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод ssh команды '(.*?)' содержит '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Тогда("^[Я|я] загружаю файл '(.*?)' по sftp как '(.*?)'$")
    override fun upload(source: String, target: String) {
        super.upload(source, target)
    }

    @Тогда("^[Я|я] скачиваю файл '(.*?)' по sftp как '(.*?)'$")
    override fun download(source: String, target: String) {
        super.download(source, target)
    }
}
