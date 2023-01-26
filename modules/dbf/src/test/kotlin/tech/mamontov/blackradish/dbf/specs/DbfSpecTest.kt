package tech.mamontov.blackradish.dbf.specs

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        "classpath:features/en/dbf.feature",
        "classpath:features/ru/dbf.feature",
    ],
)
class DbfSpecTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}
