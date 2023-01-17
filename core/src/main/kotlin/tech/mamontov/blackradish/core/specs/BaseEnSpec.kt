package tech.mamontov.blackradish.core.specs

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.utils.Logged

@Glue
class BaseEnSpec() : Logged, BaseSpec() {
    @When("^[I|i] wait '(\\d+)' second[s]?$")
    override fun wait(seconds: Int) {
        super.wait(seconds)
    }

    @Given("^[I|i] save '(.*?)' in variable '([A-Za-z0-9_]+)'$")
    override fun save(value: String, key: String) {
        super.save(value, key)
    }

    @Then("^'(.*?)' (is|matches|is higher than|is lower than|contains|is different from) '(.*?)'$")
    fun comparison(first: String, operation: String, second: String) {
        val toComparisonOperation: Map<String, ComparisonOperation> = hashMapOf(
            "is" to ComparisonOperation.IS,
            "matches" to ComparisonOperation.MATCHES,
            "is higher than" to ComparisonOperation.IS_HIGHER_THAN,
            "is lower than" to ComparisonOperation.IS_LOWER_THAN,
            "contains" to ComparisonOperation.CONTAINS,
            "is different from" to ComparisonOperation.IS_DIFFERENT_FROM,
        )

        toComparisonOperation[operation]?.let {
            super.comparison(first, it, second)
        }
    }

    @Given("^[I|i]f '(.*?)':$")
    override fun aspectIf(condition: String) {
    }

    @Given("^[E|e]lse [I|i]f '(.*?)':$")
    override fun aspectElseIf(condition: String) {
    }

    @Given("^[E|e]lse:$")
    override fun aspectElse() {
    }

    @Given("^[E|e]nd [I|i]f$")
    override fun aspectEndIf() {
    }

    @Given("^[L|l]oop in '(.*?)':$")
    override fun aspectLoop(condition: String) {
    }

    @Given("^[L|l]oop from '(-?\\d+)' to '(-?\\d+)':$")
    override fun aspectLoop(from: Int, to: Int) {
    }

    @Given("^[E|e]nd [L|l]oop$")
    override fun aspectEndLoop() {
    }

    @When("^[I|i] include scenario '(.*?)'( from feature '(.*?\\.feature)')?$")
    override fun aspectInclude(scenario: String, feature: String?) {
    }
}
