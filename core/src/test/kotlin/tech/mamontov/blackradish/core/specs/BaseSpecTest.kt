package tech.mamontov.blackradish.core.specs

import io.cucumber.testng.CucumberOptions
import tech.mamontov.blackradish.core.cucumber.BaseTest

@CucumberOptions(
    features = [
        "classpath:features/en/base.feature",
        "classpath:features/ru/base.feature",
    ],
)
class BaseSpecTest : BaseTest()
