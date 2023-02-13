package {{cookiecutter.package}}

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        "classpath:features",
    ],
    glue = [
        "{{cookiecutter.package}}.specs"
    ]
)
class MainTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}
