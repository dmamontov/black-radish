package tech.mamontov.blackradish.command.specs

import io.cucumber.docstring.DocString
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged

@Glue
class CommandEnSpec : Logged, CommandSpec() {
    @When("^[I|i] set the maximum command execution time to '(\\d+)' seconds$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    @When("^[I|i] run command '(.*?)' locally$")
    override fun run(command: String) {
        return super.run(command)
    }

    @When("^[I|i] run local command '(.*?)' in background$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    @And("^[S|s]ave the command ID in the variable '([A-Za-z0-9_]+)'$")
    override fun saveBackgroundId(variable: String) {
        super.saveBackgroundId(variable)
    }

    @Then("^[I|i] terminate a command running in background( with id '(.*?)')?$")
    override fun closeBackground(variable: String?) {
        super.closeBackground(variable)
    }

    @Then("^[I|i]n less than '(\\d+)' seconds the output of the local command '(.*?)' contains '(.*?)'$")
    override fun tail(timeout: Int, command: String, search: String) {
        super.tail(timeout, command, search)
    }

    @Then("^[S|s]ave the result of the command execution in the variable '([A-Za-z0-9_]+)'$")
    override fun save(variable: String) {
        super.save(variable)
    }

    @Then("^[C|c]ommand exit code must be '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }

    @Then("^[C|c]ommand result is:$")
    override fun check(content: DocString) {
        super.check(content)
    }
}
