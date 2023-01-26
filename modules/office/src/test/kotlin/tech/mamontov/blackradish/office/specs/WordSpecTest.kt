package tech.mamontov.blackradish.office.specs

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        "classpath:features/en/word.feature",
        "classpath:features/ru/word.feature",
    ],
)
class WordSpecTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}
