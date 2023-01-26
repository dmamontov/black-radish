package tech.mamontov.blackradish.ssh.hooks

import io.cucumber.java.After
import io.cucumber.java.Scenario
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.ssh.properties.ThreadSshProperty

@Glue
@Suppress("UNUSED_PARAMETER")
class SshHook : Logged {
    @After(value = "@ssh", order = 100)
    fun close(scenario: Scenario) {
        ThreadSshProperty.close()
    }
}
