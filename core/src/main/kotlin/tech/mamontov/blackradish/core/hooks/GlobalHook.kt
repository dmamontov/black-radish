package tech.mamontov.blackradish.core.hooks

import io.cucumber.java.Before
import io.cucumber.java.Scenario
import org.apache.commons.io.FilenameUtils
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.properties.ThreadProperty

@Glue
class GlobalHook {
    @Before(order = 0)
    fun global(scenario: Scenario) {
        ThreadProperty.set("feature", FilenameUtils.getName(scenario.uri.path))
        ThreadProperty.set("scenario", scenario.name)
    }
}
