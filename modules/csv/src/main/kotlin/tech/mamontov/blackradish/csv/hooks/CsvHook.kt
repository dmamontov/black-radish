package tech.mamontov.blackradish.csv.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.csv.storages.CsvBuilderStorage

/**
 * Csv hook
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class CsvHook : Logged {
    /**
     * Reset builder
     *
     * @param scenario Scenario
     */
    @After(order = 100)
    fun reset(scenario: Scenario) {
        CsvBuilderStorage.reset()
    }
}
