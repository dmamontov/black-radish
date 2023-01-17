package tech.mamontov.blackradish.ssh.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.ssh.properties.ThreadSessionProperty

@Glue
@Suppress("UNUSED_PARAMETER")
class SshHook: Logged {
    @After(order = 10, value = "@ssh")
    fun close(scenario: Scenario) {
        ThreadSessionProperty.close()
    }
}
