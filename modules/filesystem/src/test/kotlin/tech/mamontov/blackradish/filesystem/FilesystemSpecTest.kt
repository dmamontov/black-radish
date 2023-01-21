package tech.mamontov.blackradish.filesystem

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        // "classpath:features/en/ssh.feature",
        "classpath:features/ru/filesystem.feature",
    ],
)
class FilesystemSpecTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}
