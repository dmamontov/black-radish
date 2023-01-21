package tech.mamontov.blackradish.ssh.specs

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.ssh.enumerated.AuthType

@Glue
class SshEnSpec : Logged, SshSpec() {
    @When("^[I|i] open ssh connection to '(.*?)(:([0-9]{2,}))?' as '(.*?)' with (password|key) '(.*?)'( and passphrase '(.*?)')?$")
    fun connect(host: String, port: Int?, user: String, auth: String, token: String, passphrase: String?) {
        val toAuthType: Map<String, AuthType> = hashMapOf(
            "password" to AuthType.PASSWORD,
            "key" to AuthType.KEY,
        )

        toAuthType[auth]?.let {
            super.connect(host, port ?: 22, user, it, token, passphrase)
        }
    }

    @Then("^[I|i] close the ssh connection$")
    override fun disconnect() {
        super.disconnect()
    }

    @Then("^[I|i] run command '(.*?)' over ssh$")
    override fun run(command: String) {
        super.run(command)
    }

    @When("^[I|i] run ssh command '(.*?)' in background$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    @Then("^[I|i]n less than '(\\d+)' seconds the output of ssh command '(.*?)' contains '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Then("^[I|i] upload file '(.*?)' by sftp as '(.*?)'$")
    override fun upload(source: String, target: String) {
        super.upload(source, target)
    }

    @Then("^[I|i] download file '(.*?)' by sftp as '(.*?)'$")
    override fun download(source: String, target: String) {
        super.download(source, target)
    }
}
