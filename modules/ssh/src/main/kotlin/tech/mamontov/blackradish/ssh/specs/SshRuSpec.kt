package tech.mamontov.blackradish.ssh.specs

import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.ssh.enumerated.AuthType

/**
 * Ssh spec (RU)
 *
 * @author Dmitry Mamontov
 */
@Glue
class SshRuSpec : Logged, SshSpec() {
    /**
     * Открытие ssh соединения.
     *
     * ```
     * Сценарий: Пример подключение по ssh c паролем
     *   Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     * ```
     * ```
     * Сценарий: Пример подключение по ssh c ключом
     *   Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с ключом '${SSH_DEFAULT_PEM}'
     * ```
     * ```
     * Сценарий: Пример подключение по ssh c ключом и паролем для ключа
     *   Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с ключом '${SSH_PASS_PEM}' и паролем '${SSH_PASSPHRASE}'
     * ```
     *
     * @param host String
     * @param port Int?
     * @param user String
     * @param auth String
     * @param token String
     * @param passphrase String?
     */
    @Когда("^[Я|я] открываю ssh соединение к '(.*?)(:([0-9]{2,}))?' от имени '(.*?)' с (паролем|ключом) '(.*?)'( и паролем '(.*?)')?$")
    fun connect(host: String, port: Int?, user: String, auth: String, token: String, passphrase: String?) {
        val toAuthType: Map<String, AuthType> = hashMapOf(
            "паролем" to AuthType.PASSWORD,
            "ключом" to AuthType.KEY,
        )

        super.connect(host, port ?: 22, user, toAuthType[auth]!!, token, passphrase)
    }

    /**
     * Закрытие ssh соединения.
     *
     * ```
     * Сценарий: Пример закрытия соединения
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Тогда я закрываю ssh соединение
     * ```
     */
    @Тогда("^[Я|я] закрываю ssh соединение$")
    override fun disconnect() {
        super.disconnect()
    }

    /**
     * Запуск ssh команды.
     *
     * ```
     * Сценарий: Пример запуска команды
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Тогда я запускаю команду 'echo "example"' по ssh
     * ```
     *
     * @param command String
     */
    @Тогда("^[Я|я] запускаю команду '(.*?)' по ssh$")
    override fun run(command: String) {
        super.run(command)
    }

    /**
     * Запуск ssh команды в фоне.
     *
     * ```
     * Сценарий: Пример запуска команды в фоне
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Тогда я запускаю ssh команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне
     * ```
     *
     * @param command String
     */
    @Когда("^[Я|я] запускаю ssh команду '(.*?)' в фоне$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    /**
     * Запуск ssh команды до появления содержимого в течении указанного времени.
     *
     * ```
     * Сценарий: Пример запуска команды до появления содержимого в течении указанного времени
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Тогда менее чем через '10' секунд вывод ssh команды 'sleep 1 && printf "hello\nworld" && sleep 10' содержит 'world'
     * ```
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    @Затем("^[М|м]енее чем через '(\\d+)' секунд вывод ssh команды '(.*?)' содержит '(.*?)'$")
    override fun search(timeout: Int, command: String, search: String) {
        super.search(timeout, command, search)
    }

    /**
     * Загрузка файла через sftp.
     *
     * ```
     * Сценарий: Пример загрузки файла через sftp
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Тогда я загружаю файл 'artifacts/sftp/upload.txt' по sftp как '/app/upload.txt'
     * ```
     *
     * @param source String
     * @param target String
     */
    @Тогда("^[Я|я] загружаю файл '(.*?)' по sftp как '(.*?)'$")
    override fun upload(source: String, target: String) {
        super.upload(source, target)
    }

    /**
     * Скачивание файла через sftp.
     *
     * ```
     * Сценарий: Пример скачивания файла через sftp
     *   Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
     *   Затем я запускаю команду 'echo "DOWNLOAD" > /app/download.txt' по ssh
     *   Затем я скачиваю файл '/app/download.txt' по sftp как 'artifacts/sftp/download.txt'
     * ```
     *
     * @param source String
     * @param target String
     */
    @Тогда("^[Я|я] скачиваю файл '(.*?)' по sftp как '(.*?)'$")
    override fun download(source: String, target: String) {
        super.download(source, target)
    }
}
