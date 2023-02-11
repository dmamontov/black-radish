package tech.mamontov.blackradish.core.aspects

import io.cucumber.core.backend.TestCaseState
import io.cucumber.core.feature.FeatureParser
import io.cucumber.core.feature.Options
import io.cucumber.core.gherkin.Feature
import io.cucumber.core.gherkin.Pickle
import io.cucumber.core.gherkin.Step
import io.cucumber.core.resource.ResourceScanner
import io.cucumber.plugin.event.Result
import io.cucumber.plugin.event.Status
import io.cucumber.plugin.event.TestCase
import io.cucumber.plugin.event.TestStep
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.generators.IncludeGenerator
import tech.mamontov.blackradish.core.generators.OperationGenerator
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.reflecation.Reflecation
import java.net.URI
import java.time.Instant
import java.util.function.Supplier
import io.cucumber.messages.types.Pickle as TypesPickle

/**
 * AspectJ aspect to generate steps
 *
 * @see [AspectJ](https://www.eclipse.org/aspectj/)
 *
 * @author Dmitry Mamontov
 *
 * @property includeGenerator IncludeGenerator
 * @property operationGenerator OperationGenerator
 */
@Aspect
@Suppress(
    "UNCHECKED_CAST",
    "UNUSED_PARAMETER",
    "UNUSED",
    "KDocUnresolvedReference",
)
class GenerateAspect : Logged {
    private val includeGenerator = IncludeGenerator()
    private val operationGenerator = OperationGenerator()

    /**
     * Pointcut for **[io.cucumber.core.runner.Runner.createTestStepsForPickleSteps]**
     *
     * @param pickle io.cucumber.core.gherkin.Pickle
     */
    @Pointcut("execution (* io.cucumber.core.runner.Runner.createTestStepsForPickleSteps(..)) && args(pickle)")
    private fun createTestStepsForPickleSteps(pickle: Any) {
    }

    /**
     * Action before **[io.cucumber.core.runner.Runner.createTestStepsForPickleSteps]**
     *
     * @param jp JoinPoint
     * @param pickle io.cucumber.core.gherkin.Pickle
     */
    @Before(value = "createTestStepsForPickleSteps(pickle)")
    fun createTestStepsForPickleSteps(jp: JoinPoint, pickle: Any) {
        val runner: Any = jp.getThis()

        if (!Reflecation.instanceOf(pickle, "io.cucumber.core.gherkin.messages.GherkinMessagesPickle")) {
            return
        }

        val pickleStepDefinitionMatchMethod = Reflecation.method(
            runner,
            "matchStepToStepDefinition",
            false,
            Pickle::class.java,
            Step::class.java,
        )

        this.includeGenerator.pickleStepDefinitionMatchMethod = pickleStepDefinitionMatchMethod
        this.operationGenerator.pickleStepDefinitionMatchMethod = pickleStepDefinitionMatchMethod

        this.includeGenerator.fillStepsMap(
            pickle,
            Reflecation.getValue(pickle, "steps") as List<Step>,
            (Reflecation.getValue(pickle, "pickle") as TypesPickle).name,
        )
    }

    /**
     * Pointcut for **[io.cucumber.core.runner.PickleStepDefinitionMatch.runStep]**
     *
     * @param state TestCaseState
     */
    @Pointcut("execution (* io.cucumber.core.runner.PickleStepDefinitionMatch.runStep(..)) && args(state)")
    private fun runStep(state: TestCaseState) {
    }

    /**
     * Action around **[io.cucumber.core.runner.PickleStepDefinitionMatch.runStep]**
     *
     * @param jp ProceedingJoinPoint
     * @param state TestCaseState
     */
    @Around(value = "runStep(state)")
    fun runStep(jp: ProceedingJoinPoint, state: TestCaseState) {
        if (!Reflecation.instanceOf(jp.getThis(), "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
            return
        }

        val step = Reflecation.getValue(jp.getThis(), "step") as Step

        val stepErrors = this.getStepErrors()
        if (stepErrors.containsKey(step.id)) {
            Assertions.fail<Any>(stepErrors[step.id])
        }

        this.operationGenerator.validateStep(jp.getThis(), step)

        if (!this.getSkippedSteps().contains(step.id)) {
            jp.proceed()
        }
    }

    /**
     * Pointcut for create **[io.cucumber.core.runtime.FeaturePathFeatureSupplier]**
     *
     * @param classLoader Supplier<ClassLoader>
     * @param featureOptions Options
     * @param parser FeatureParser
     */
    @Pointcut(
        "execution (io.cucumber.core.runtime.FeaturePathFeatureSupplier.new(..)) && args(classLoader, featureOptions, parser)",
    )
    private fun newFeaturePathFeatureSupplier(
        classLoader: Supplier<ClassLoader>,
        featureOptions: Options,
        parser: FeatureParser,
    ) {
    }

    /**
     * Action after create **[io.cucumber.core.runtime.FeaturePathFeatureSupplier]**
     *
     * @param jp JoinPoint
     * @param classLoader Supplier<ClassLoader>
     * @param featureOptions Options
     * @param parser FeatureParser
     */
    @After(value = "newFeaturePathFeatureSupplier(classLoader, featureOptions, parser)")
    fun newFeaturePathFeatureSupplier(
        jp: JoinPoint,
        classLoader: Supplier<ClassLoader>,
        featureOptions: Options,
        parser: FeatureParser,
    ) {
        includeGenerator.featureScanner =
            Reflecation.getValue(jp.getThis(), "featureScanner") as ResourceScanner<Feature>
    }

    /**
     * Pointcut for **[io.cucumber.core.gherkin.messages.GherkinMessagesPickle.getSteps]**
     */
    @Pointcut("execution (* io.cucumber.core.gherkin.messages.GherkinMessagesPickle.getSteps(..))")
    private fun getSteps() {
    }

    /**
     * Action around **[io.cucumber.core.gherkin.messages.GherkinMessagesPickle.getSteps]**
     *
     * @param jp ProceedingJoinPoint
     * @return List<Step>
     */
    @Around(value = "getSteps()")
    fun getSteps(jp: ProceedingJoinPoint): List<Step> {
        val steps = this.includeGenerator.generate(
            jp.getThis(),
            Reflecation.getValue(jp.getThis(), "steps") as MutableList<Step>,
        )

        this.operationGenerator.stepErrors = this.getStepErrors().toMutableMap()
        this.operationGenerator.skippedSteps = this.getSkippedSteps().toMutableList()

        return this.operationGenerator.generateSteps(steps, jp.getThis()).toList()
    }

    /**
     * Pointcut for **[io.cucumber.core.runner.PickleStepTestStep.getUri]**
     */
    @Pointcut("execution (* io.cucumber.core.runner.PickleStepTestStep.getUri(..))")
    private fun getUri() {
    }

    /**
     * Action around **[io.cucumber.core.runner.PickleStepTestStep.getUri]**
     *
     * @param jp ProceedingJoinPoint
     * @return URI
     */
    @Around(value = "getUri()")
    fun getUri(jp: ProceedingJoinPoint): URI {
        val step = Reflecation.getValue(jp.getThis(), "step") as Step

        if (this.includeGenerator.stepsUri.containsKey(step.id)) {
            return this.includeGenerator.stepsUri[step.id]!!
        }

        return Reflecation.getValue(jp.getThis(), "uri") as URI
    }

    /**
     * Pointcut for create **[io.cucumber.plugin.event.TestStepFinished]**
     *
     * @param timeInstant Instant
     * @param testCase TestCase
     * @param testStep TestStep
     * @param testResult ConvertedResult
     */
    @Pointcut(
        "execution (io.cucumber.plugin.event.TestStepFinished.new(..)) && args(timeInstant, testCase, testStep, testResult)",
    )
    private fun newTestStepFinished(
        timeInstant: Instant,
        testCase: TestCase,
        testStep: TestStep,
        testResult: Result,
    ) {
    }

    /**
     * Action after create **[io.cucumber.plugin.event.TestStepFinished]**
     *
     * @param jp JoinPoint
     * @param timeInstant Instant
     * @param testCase TestCase
     * @param testStep TestStep
     * @param testResult ConvertedResult
     */
    @After(value = "newTestStepFinished(timeInstant, testCase, testStep, testResult)")
    fun newTestStepFinished(
        jp: JoinPoint,
        timeInstant: Instant,
        testCase: TestCase,
        testStep: TestStep,
        testResult: Result,
    ) {
        val resultsMap = Reflecation.field(jp.getThis(), "result")
        val result = Reflecation.getValue(jp.getThis(), resultsMap) as Result
        try {
            val id: String = (Reflecation.getValue(testStep, "step") as Step).id

            if (this.getSkippedSteps().contains(id)) {
                resultsMap[jp.getThis()] = Result(Status.SKIPPED, result.duration, result.error)
            }
        } catch (_: NoSuchFieldException) {
            resultsMap[jp.getThis()] = Result(Status.SKIPPED, result.duration, result.error)
        }
    }

    /**
     * List of skipped steps from all generators
     *
     * @return List<String>
     */
    private fun getSkippedSteps(): List<String> {
        return this.includeGenerator.skippedSteps + this.operationGenerator.skippedSteps
    }

    /**
     * List of steps with errors, from all generators
     *
     * @return Map<String, String>
     */
    private fun getStepErrors(): Map<String, String> {
        return this.includeGenerator.stepErrors + this.operationGenerator.stepErrors
    }
}
