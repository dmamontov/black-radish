package tech.mamontov.blackradish.core.testng

import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions
import org.testng.ITestContext
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import tech.mamontov.blackradish.core.cucumber.OptionsCucumber
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.property.ThreadProperty
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.lang.reflect.Method

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

            var parallel = false
            run breaking@{
                this::class.java.declaredMethods.forEach { method: Method ->
                    if (method.isAnnotationPresent(DataProvider::class.java)) {
                        val annotation: Annotation = method.getAnnotation(DataProvider::class.java) as DataProvider
                        parallel = Reflecation.method(annotation, "parallel").call() as Boolean
                    }
                }
            }

            map[CucumberOptions::class.java] = OptionsCucumber(this::class.java, parallel)
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
