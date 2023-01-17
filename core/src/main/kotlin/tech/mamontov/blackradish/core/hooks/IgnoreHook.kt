package tech.mamontov.blackradish.core.hooks

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import org.testng.SkipException
import tech.mamontov.blackradish.core.annotations.Glue

@Glue
class IgnoreHook {
    @Before(value = "@ignore or @IGNORE or @Ignore", order = 5)
    fun ignore(scenario: Scenario) {
        scenario.log("Scenario '${scenario.name}' ignored.")
        throw SkipException("Scenario '${scenario.name}' ignored.")
    }
}
