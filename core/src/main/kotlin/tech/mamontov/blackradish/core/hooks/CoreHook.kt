package tech.mamontov.blackradish.core.hooks

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import org.apache.commons.io.FilenameUtils
import org.testng.SkipException
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.properties.ConfigurationProperty
import tech.mamontov.blackradish.core.properties.ThreadDatabaseProperty
import tech.mamontov.blackradish.core.properties.ThreadFutureProperty
import tech.mamontov.blackradish.core.properties.ThreadPoolProperty
import tech.mamontov.blackradish.core.properties.ThreadProperty
import java.util.concurrent.TimeUnit

@Glue
@Suppress("UNUSED_PARAMETER")
class CoreHook {
    @After(order = 0)
    fun destroy(scenario: Scenario) {
        destroyBackground(false)
        ThreadDatabaseProperty.disconnect(true)
    }

    @After(value = "@sql or @SQL or @Sql", order = 97)
    fun sql(scenario: Scenario) {
        ThreadDatabaseProperty.disconnect(true)
    }

    @After(value = "@force or @FORCE or @Force", order = 98)
    fun force(scenario: Scenario) {
        destroyBackground(true)
    }

    @Before(value = "@ignore or @IGNORE or @Ignore", order = 99)
    fun ignore(scenario: Scenario) {
        scenario.log("Scenario '${scenario.name}' ignored.")
        throw SkipException("Scenario '${scenario.name}' ignored.")
    }

    @Before(order = 100)
    fun global(scenario: Scenario) {
        ThreadProperty.set("feature", FilenameUtils.getName(scenario.uri.path))
        ThreadProperty.set("scenario", scenario.name)
    }

    private fun destroyBackground(force: Boolean) {
        val pool = ThreadPoolProperty.get()
        if (pool === null) {
            return
        }

        val futures = ThreadFutureProperty.all()

        if (futures.isNotEmpty()) {
            futures.values.forEach {
                it.destroy()
            }

            ThreadFutureProperty.reset()
        }

        val timeout = ConfigurationProperty.get(ConfigurationProperty.THREAD_POOL_SHUTDOWN_TIMEOUT, 10).toLong()
        if (force || !pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
            pool.shutdownNow()
        }

        ThreadPoolProperty.reset()
    }
}
