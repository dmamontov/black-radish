package tech.mamontov.blackradish.csv.specs

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        "classpath:features/en/csv.feature",
        "classpath:features/ru/csv.feature",
    ],
)
class CsvSpecTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}
