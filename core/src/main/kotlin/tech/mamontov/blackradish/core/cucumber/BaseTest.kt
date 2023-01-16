package tech.mamontov.blackradish.core.cucumber

import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions
import org.testng.ITestContext
import org.testng.annotations.BeforeClass
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.property.ThreadProperty
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation

@Suppress("UNCHECKED_CAST")
abstract class BaseTest : Logged, AbstractTestNGCucumberTests() {
    @BeforeClass(alwaysRun = true)
    override fun setUpClass(context: ITestContext) {
        try {
            val annotationData = Reflecation.method(this.javaClass, "annotationData").call()
            val map = Reflecation.get(
                annotationData,
                "annotations",
            ) as MutableMap<Class<out Annotation>, Annotation>

            map[CucumberOptions::class.java] = OptionsCucumber(this::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        super.setUpClass(context)
    }

    @BeforeClass(alwaysRun = true)
    fun beforeClass() {
        ThreadProperty.set("class", this::class.java.canonicalName)
    }
}
