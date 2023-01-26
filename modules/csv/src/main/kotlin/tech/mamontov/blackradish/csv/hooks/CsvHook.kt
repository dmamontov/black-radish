package tech.mamontov.blackradish.csv.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.csv.properties.ThreadBuilderProperty

@Glue
@Suppress("UNUSED_PARAMETER")
class CsvHook : Logged {
    @After(order = 100)
    fun close(scenario: Scenario) {
        ThreadBuilderProperty.reset()
    }
}
