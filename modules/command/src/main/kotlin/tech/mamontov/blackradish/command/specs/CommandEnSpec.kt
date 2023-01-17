package tech.mamontov.blackradish.command.specs

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.command.data.CommandResult
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged

@Glue
class CommandEnSpec : Logged, CommandSpec() {
    @When("^[I|i] set the maximum command execution time to '(\\d+)' seconds$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    @When("^[I|i] run command '(.*?)' locally$")
    override fun run(command: String): CommandResult? {
        return super.run(command)
    }

    @Then("^[I|i]n less than '(\\d+)' seconds the output of the local command '(.*?)' contains '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Then("^[I|i] save the result of the command execution in the variable '([A-Za-z0-9_]+)'$")
    override fun save(variable: String) {
        super.save(variable)
    }

    @Then("^[I|i] check that the exit code of the command is '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }
}
