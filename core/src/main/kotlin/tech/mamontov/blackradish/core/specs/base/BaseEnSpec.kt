package tech.mamontov.blackradish.core.specs.base

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Base spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class BaseEnSpec() : Logged, BaseSpec() {
    companion object {
        val toComparisonOperation: Map<String, ComparisonOperation> = hashMapOf(
            "is" to ComparisonOperation.IS,
            "matches" to ComparisonOperation.MATCHES,
            "is higher than" to ComparisonOperation.IS_HIGHER_THAN,
            "is lower than" to ComparisonOperation.IS_LOWER_THAN,
            "contains" to ComparisonOperation.CONTAINS,
            "is different from" to ComparisonOperation.IS_DIFFERENT_FROM,
        )
    }

    /**
     * Wait n seconds.
     *
     * ```
     * Scenario: Wait example
     *   Given i wait '1' second
     * ```
     *
     * @param seconds Int
     */
    @When("^[I|i] wait '(\\d+)' second[s]?$")
    override fun wait(seconds: Int) {
        super.wait(seconds)
    }

    /**
     * Saving a value to a variable.
     *
     * ```
     * Scenario: Example of saving a value to a variable
     *   Given i save 'example' in variable 'ONE'
     * ```
     *
     * @param value String
     * @param key String
     */
    @Given("^[I|i] save '(.*?)' in variable '([A-Za-z0-9_]+)'$")
    override fun save(value: String, key: String) {
        super.save(value, key)
    }

    /**
     * Comparison of two values.
     * Numbers are compared as double.
     *
     * ```
     * Scenario: Examples of checking for equality
     *   Then '100' is '100'
     *   And 'example' is 'example'
     * ```
     * ```
     * Scenario: Examples of checking for inequality
     *   Then '100' is different from '200'
     *   And 'example' is different from 'ex'
     * ```
     * ```
     * Scenario: Examples of checking by regular expression
     *   Then '200' matches '^\d+'
     *   And 'example' matches '^[A-Za-z]+'
     * ```
     * ```
     * Scenario: Examples of checking the occurrence of a substring
     *   Then '100' contains '10'
     *   And 'example' contains 'ex'
     * ```
     * ```
     * Scenario: Examples of comparing numbers
     *   Then '100.1' is higher than '100'
     *   And '200' is lower than '200.2'
     *   And '0xAF' is lower than '176'
     * ```
     *
     * @param first String
     * @param operation String
     * @param second String
     */
    @Then("^'(.*?)' (is|matches|is higher than|is lower than|contains|is different from) '(.*?)'$")
    fun comparison(first: String, operation: String, second: String) {
        super.comparison(first, toComparisonOperation[operation]!!, second)
    }

    /**
     * Conditional operator - If.
     * The condition is written in kotlin.
     *
     * ```
     * Scenario: Usage example - If, End If
     *   Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
     *   * If '${NUMBER} == 1.0':
     *     Then '${NUMBER}' is '1.0'
     *   * End If
     * ```
     *
     * @param condition String
     */
    @Given("^[I|i]f '(.*?)':$")
    override fun aspectIf(condition: String) {
    }

    /**
     * Conditional operator - Else If.
     * The condition is written in kotlin.
     *
     * ```
     * Scenario: Usage example - If, Else If, End If
     *   Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
     *   * If '${NUMBER} == 1.0':
     *     Then '${NUMBER}' is '1.0'
     *   * Else If '${NUMBER} == 2.0':
     *     Then '${NUMBER}' is '2.0'
     *   * End If
     * ```
     *
     * @param condition String
     */
    @Given("^[E|e]lse [I|i]f '(.*?)':$")
    override fun aspectElseIf(condition: String) {
    }

    /**
     * Conditional operator - Else.
     *
     * ```
     * Scenario: Usage example - If, Else, End If
     *   Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
     *   * If '${NUMBER} == 1.0':
     *     Then '${NUMBER}' is '1.0'
     *   * Else:
     *     Then '${NUMBER}' is higher than '1.0'
     *   * End If
     * ```
     */
    @Given("^[E|e]lse:$")
    override fun aspectElse() {
    }

    /**
     * Conditional operator - End If.
     *
     * ```
     * Scenario: Usage example - If, End If
     *   Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
     *   * If '${NUMBER} == 1.0':
     *     Then '${NUMBER}' is '1.0'
     *   * End If
     * ```
     */
    @Given("^[E|e]nd [I|i]f$")
    override fun aspectEndIf() {
    }

    /**
     * Loop - joint.
     *
     * ```
     * Scenario: An example of cyclic generation from a list
     *   * Loop in 'first,second,three':
     *     * i save '${loop.value}' in variable 'VAR'
     *   * End Loop
     * ```
     * ```
     * Scenario: An example of cyclic generation from a list
     *   Given i save '1' in variable 'NUMBER'
     *   * Loop in '${NUMBER},first,second':
     *     * i save '${loop.value}' in variable 'VAR'
     *   * End Loop
     * ```
     *
     * @param condition String
     */
    @Given("^[L|l]oop in '(.*?)':$")
    override fun aspectLoop(condition: String) {
    }

    /**
     * Loop - with a counter.
     *
     * ```
     * Scenario: An example of cyclic generation of steps by a cycle with a counter
     *   * Loop from '1' to '3':
     *     * i save '${loop.value}' in variable 'NUMBER'
     *   * End Loop
     * ```
     * ```
     * Scenario: An example of cyclic generation of steps by a loop with a counter in reverse order
     *   * Loop from '3' to '1':
     *     * i save '${loop.value}' in variable 'NUMBER'
     *   * End Loop
     * ```
     *
     * @param from Int
     * @param to Int
     */
    @Given("^[L|l]oop from '(-?\\d+)' to '(-?\\d+)':$")
    override fun aspectLoop(from: Int, to: Int) {
    }

    /**
     * End loop
     *
     * ```
     * Scenario: An example of cyclic generation of steps by a cycle with a counter
     *   * Loop from '1' to '3':
     *     * i save '${loop.value}' in variable 'NUMBER'
     *   * End Loop
     * ```
     */
    @Given("^[E|e]nd [L|l]oop$")
    override fun aspectEndLoop() {
    }

    /**
     * Scenario reuse.
     *
     * ```
     * Scenario: An example of creating a variable
     *   Given i save '2' in variable 'NUMBER'
     *
     * Scenario: An example of include a scenario from the current feature
     *   Given i save '1' in variable 'NUMBER'
     *   Then '${NUMBER}' is '1'
     *   Given i include scenario 'An example of creating a variable'
     *   Then '${NUMBER}' is '2'
     * ```
     * ```
     * Feature: Included scenarios
     *   Scenario: An example of creating a variable
     *     Given i save '3' in variable 'NUMBER'
     *
     * Feature: Main feature
     *   Scenario: An example of include a scenario from another feature
     *     Given i save '1' in variable 'NUMBER'
     *     Then '${NUMBER}' is '1'
     *     Given i include scenario 'An example of creating a variable' from feature 'classpath:include.feature'
     *     Then '${NUMBER}' is '3'
     * ```
     *
     * @param scenario String
     * @param feature String?
     */
    @When("^[I|i] include scenario '(.*?)'( from feature '(.*?\\.feature)')?$")
    override fun aspectInclude(scenario: String, feature: String?) {
    }
}
