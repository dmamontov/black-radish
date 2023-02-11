package tech.mamontov.blackradish.ssh.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.ssh.storages.SshClientStorage

/**
 * Ssh hooks.
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class SshHook : Logged {
    /**
     * Close ssh connection.
     *
     * @param scenario Scenario
     */
    @After(order = 100)
    fun close(scenario: Scenario) {
        SshClientStorage.close()
    }
}
