package tech.mamontov.blackradish.command.specs

import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Command spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class CommandEnSpec : Logged, CommandSpec() {
    /**
     * Set the maximum command execution time.
     *
     * ```
     * Scenario: An example of running a command with a set maximum execution time
     *   When i set the maximum command execution time to '10' seconds
     *   Then i run command 'echo "example"' locally
     * ```
     *
     * @param seconds Long
     */
    @When("^[I|i] set the maximum command execution time to '(\\d+)' seconds$")
    override fun timeout(seconds: Long) {
        super.timeout(seconds)
    }

    /**
     * Run command.
     *
     * ```
     * Scenario: An example of running a command
     *   When i run command 'echo "example"' locally
     * ```
     *
     * @param command String
     */
    @When("^[I|i] run command '(.*?)' locally$")
    override fun run(command: String) {
        return super.run(command)
    }

    /**
     * Run a command in the background.
     *
     * ```
     * Scenario: An example of running a command in the background
     *   When i run local command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background
     * ```
     *
     * @param command String
     */
    @When("^[I|i] run local command '(.*?)' in background$")
    override fun runInBackground(command: String) {
        super.runInBackground(command)
    }

    /**
     * Saving the ID of a command running in the background to a variable.
     *
     * ```
     * Scenario: An example of saving an identifier to a variable
     *   When i run local command 'sleep 1 && echo "hello" && sleep 2 && echo "world"' in background
     *   Then save the command ID in the variable 'UUID'
     * ```
     *
     * @param variable String
     */
    @And("^[S|s]ave the command ID in the variable '([A-Za-z0-9_]+)'$")
    override fun saveBackgroundUuid(variable: String) {
        super.saveBackgroundUuid(variable)
    }

    /**
     * Terminates a command that is running in the background.
     *
     * ```
     * Scenario: An example of completing a command running in the background
     *   When i run local command 'sleep 1 && echo "hello" && sleep 2 && echo "world"' in background
     *   Then save the command ID in the variable 'UUID'
     *   Then i terminate a command running in background with id '${UUID}'
     * ```
     * ```
     * Scenario: An example of completing a command running in the background
     *   When i run local command 'sleep 1 && echo "hello" && sleep 2 && echo "world"' in background
     *   Then i terminate a command running in background
     * ```
     *
     * @param variable String?
     */
    @Then("^[I|i] terminate a command running in background( with id '(.*?)')?$")
    override fun terminateBackground(variable: String?) {
        super.terminateBackground(variable)
    }

    /**
     * Run the command until the content appears within the specified time.
     *
     * ```
     * Scenario: An example of running a command until the content appears within the specified time
     *   Then in less than '10' seconds the output of the local command 'sleep 1 && printf "hello\nworld" && sleep 10' contains 'world'
     * ```
     *
     * @param timeout Int
     * @param command String
     * @param search String
     */
    @Then("^[I|i]n less than '(\\d+)' seconds the output of the local command '(.*?)' contains '(.*?)'$")
    override fun search(timeout: Int, command: String, search: String) {
        super.search(timeout, command, search)
    }

    /**
     * Exit code check.
     *
     * ```
     * Scenario: An example of checking the exit code of a command
     *   When i run command 'echo "first"' locally
     *   Then command exit code must be '0'
     * ```
     *
     * @param code Long
     */
    @Then("^[C|c]ommand exit code must be '(-?\\d+)'$")
    override fun exitCode(code: Long) {
        super.exitCode(code)
    }
}
