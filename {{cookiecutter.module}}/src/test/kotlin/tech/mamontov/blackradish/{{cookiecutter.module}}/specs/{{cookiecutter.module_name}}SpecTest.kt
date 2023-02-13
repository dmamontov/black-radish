package tech.mamontov.blackradish.{{cookiecutter.module}}.specs

import io.cucumber.testng.CucumberOptions
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.testng.BaseTest

@CucumberOptions(
    features = [
        "classpath:features/en/{{cookiecutter.module}}.feature",
        "classpath:features/ru/{{cookiecutter.module}}.feature",
    ],
)
class {{cookiecutter.module_name}}SpecTest : BaseTest() {
    @DataProvider(parallel = false)
    override fun scenarios(): Array<Array<Any?>?>? {
        return super.scenarios()
    }
}