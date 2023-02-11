package tech.mamontov.blackradish.ssh.specs

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.ssh.enumerated.AuthType

/**
 * Ssh spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class SshEnSpec : Logged, SshSpec() {
    /**
     * Opening an ssh connection.
     *
     * ```
     * Scenario: Example ssh connection with password
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
     * ```
     * ```
     * Scenario: An example of connecting via ssh with a key
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
     * ```
     * ```
     * Scenario: An example of connecting via ssh with a key and passphrase
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PASS_PEM}' and passphrase '${SSH_PASSPHRASE}'
     * ```
     *
     * @param host String
     * @param port Int?
     * @param user String
     * @param auth String
     * @param token String
     * @param passphrase String?
     */
    @When("^[I|i] open ssh connection to '(.*?)(:([0-9]{2,}))?' as '(.*?)' with (password|key) '(.*?)'( and passphrase '(.*?)')?$")
    fun connect(host: String, port: Int?, user: String, auth: String, token: String, passphrase: String?) {
        val toAuthType: Map<String, AuthType> = hashMapOf(
            "password" to AuthType.PASSWORD,
            "key" to AuthType.KEY,
        )

        super.connect(host, port ?: 22, user, toAuthType[auth]!!, token, passphrase)
    }

    /**
     * Closing the ssh connection.
     *
     * ```
     * Scenario: An example of closing a connection
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
     *   Then i close the ssh connection
     * ```
     */
    @Then("^[I|i] close the ssh connection$")
    override fun disconnect() {
        super.disconnect()
    }

    /**
     * Run ssh command.
     *
     * ```
     * Scenario: An example of running a command
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
     *   When i run command 'echo "example"' over ssh
     * ```
     *
     * @param command String
     */
    @Then("^[I|i] run command '(.*?)' over ssh$")
    override fun run(command: String) {
        super.run(command)
    }

    /**
     * Run an ssh command in the background.
     *
     * ```
     * Scenario: An example of running a command in the background
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
     *   When i run ssh command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background
     * ```
     *
     * @param command String
     */
    @When("^[I|i] run ssh command '(.*?)' in background$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    /**
     * Run the ssh command until the content appears within the specified time.
     *
     * ```
     * Scenario: An example of running a command until the content appears within the specified time
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
     *   Then in less than '10' seconds the output of ssh command 'sleep 1 && printf "hello\nworld" && sleep 10' contains 'world'
     * ```
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    @Then("^[I|i]n less than '(\\d+)' seconds the output of ssh command '(.*?)' contains '(.*?)'$")
    override fun search(timeout: Int, command: String, search: String) {
        super.search(timeout, command, search)
    }

    /**
     * File upload via sftp.
     *
     * ```
     * Scenario: An example of uploading a file via sftp
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
     *   Then i upload file 'artifacts/sftp/upload.txt' by sftp as '/app/upload.txt'
     * ```
     *
     * @param source String
     * @param target String
     */
    @Then("^[I|i] upload file '(.*?)' by sftp as '(.*?)'$")
    override fun upload(source: String, target: String) {
        super.upload(source, target)
    }

    /**
     * Downloading a file via sftp.
     *
     * ```
     * Scenario: An example of downloading a file via sftp
     *   Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
     *   Then i run command 'echo "DOWNLOAD" > /app/example.txt' over ssh
     *   Then i download file '/app/example.txt' by sftp as 'artifacts/sftp/download.txt'
     * ```
     *
     * @param source String
     * @param target String
     */
    @Then("^[I|i] download file '(.*?)' by sftp as '(.*?)'$")
    override fun download(source: String, target: String) {
        super.download(source, target)
    }
}
