package tech.mamontov.blackradish.command.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.command.storages.CommandStorage
import tech.mamontov.blackradish.core.annotations.Glue

/**
 * Command hooks
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class CommandHook {
    /**
     * Reset command storage
     *
     * @param scenario Scenario
     */
    @After(order = 100)
    fun reset(scenario: Scenario) {
        CommandStorage.reset()
    }
}
