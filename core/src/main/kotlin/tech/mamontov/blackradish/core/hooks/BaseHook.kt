package tech.mamontov.blackradish.core.hooks

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import org.apache.commons.io.FilenameUtils
import org.testng.SkipException
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.storages.ConfigurationStorage
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.core.storages.DatabaseConnectionStorage
import tech.mamontov.blackradish.core.storages.PropertyStorage
import tech.mamontov.blackradish.core.storages.ThreadExecutorStorage
import tech.mamontov.blackradish.core.storages.ThreadFutureStorage
import java.util.concurrent.TimeUnit

/**
 * Base hooks
 *
 * @author Dmitry Mamontov
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class BaseHook {
    /**
     * Reset all properties after finished scenario
     *
     * @param scenario Scenario
     */
    @After(order = 0)
    fun reset(scenario: Scenario) {
        destroyBackground(false)

        DatabaseConnectionStorage.disconnect(true)
        ConvertedResultStorage.reset()
        PropertyStorage.reset()
    }

    /**
     * Force close connections after finished scenario with @force tag
     *
     * @param scenario Scenario
     */
    @After(value = "@force or @FORCE or @Force", order = 98)
    fun force(scenario: Scenario) {
        destroyBackground(true)
    }

    /**
     * Ignore script when @ignore tag is present
     *
     * @param scenario Scenario
     */
    @Before(value = "@ignore or @IGNORE or @Ignore", order = 99)
    fun ignore(scenario: Scenario) {
        scenario.log("Scenario '${scenario.name}' ignored.")
        throw SkipException("Scenario '${scenario.name}' ignored.")
    }

    /**
     * Setting the default properties
     *
     * @param scenario Scenario
     */
    @Before(order = 100)
    fun setProperties(scenario: Scenario) {
        PropertyStorage.set("feature", FilenameUtils.getName(scenario.uri.path))
        PropertyStorage.set("scenario", scenario.name)
    }

    /**
     * Destroy background process
     *
     * @param force Boolean
     */
    private fun destroyBackground(force: Boolean) {
        val pool = ThreadExecutorStorage.get()
        if (pool === null) {
            return
        }

        val futures = ThreadFutureStorage.all()

        if (futures.isNotEmpty()) {
            futures.values.forEach {
                it.terminate()
            }

            ThreadFutureStorage.reset()
        }

        val timeout = ConfigurationStorage.get(ConfigurationStorage.THREAD_POOL_SHUTDOWN_TIMEOUT, 10).toLong()
        if (force || !pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
            pool.shutdownNow()
        }

        ThreadExecutorStorage.reset()
    }
}
