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
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.generator.step.IncludeGenerator
import tech.mamontov.blackradish.core.utils.generator.step.OperationGenerator
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.net.URI
import java.time.Instant
import java.util.function.Supplier
import io.cucumber.messages.types.Pickle as TypesPickle

@Aspect
@Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")
open class GenerateAspect : Logged {
    private val includeGenerator = IncludeGenerator()
    private val operationGenerator = OperationGenerator()

    @Pointcut("execution (* io.cucumber.core.runner.Runner.createTestStepsForPickleSteps(..)) && args(pickle)")
    protected open fun create(pickle: Any) {
    }

    @Before(value = "create(pickle)")
    fun create(jp: JoinPoint, pickle: Any) {
        val runner: Any = jp.getThis()

        if (!Reflecation.match(pickle, "io.cucumber.core.gherkin.messages.GherkinMessagesPickle")) {
            return
        }

        val matcher = Reflecation.method(
            runner,
            "matchStepToStepDefinition",
            false,
            Pickle::class.java,
            Step::class.java,
        )

        this.includeGenerator.matcher = matcher
        this.operationGenerator.matcher = matcher

        this.includeGenerator.fill(
            pickle,
            Reflecation.get(pickle, "steps") as List<Step>,
            (Reflecation.get(pickle, "pickle") as TypesPickle).name,
        )
    }

    @Pointcut("execution (* io.cucumber.core.runner.PickleStepDefinitionMatch.runStep(..)) && args(state)")
    protected open fun run(state: TestCaseState) {
    }

    @Around(value = "run(state)")
    fun run(jp: ProceedingJoinPoint, state: TestCaseState) {
        if (!Reflecation.match(jp.getThis(), "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
            return
        }

        val step = Reflecation.get(jp.getThis(), "step") as Step

        val errors = this.errors()
        if (errors.containsKey(step.id)) {
            Assertions.fail<Any>(errors[step.id])
        }

        this.operationGenerator.proceed(jp.getThis(), step)

        if (!this.skipped().contains(step.id)) {
            jp.proceed()
        }
    }

    @Pointcut("execution (io.cucumber.core.runtime.FeaturePathFeatureSupplier.new(..)) && args(classLoader, featureOptions, parser)")
    protected open fun supplier(
        classLoader: Supplier<ClassLoader>,
        featureOptions: Options,
        parser: FeatureParser,
    ) {
    }

    @After(value = "supplier(classLoader, featureOptions, parser)")
    fun supplier(
        jp: JoinPoint,
        classLoader: Supplier<ClassLoader>,
        featureOptions: Options,
        parser: FeatureParser,
    ) {
        includeGenerator.scanner = Reflecation.get(jp.getThis(), "featureScanner") as ResourceScanner<Feature>
    }

    @Pointcut("execution (* io.cucumber.core.gherkin.messages.GherkinMessagesPickle.getSteps(..))")
    protected open fun steps() {
    }

    @Around(value = "steps()")
    fun steps(jp: ProceedingJoinPoint): List<Step> {
        var steps = this.includeGenerator.generate(
            jp.getThis(),
            Reflecation.get(jp.getThis(), "steps") as MutableList<Step>,
        )

        this.operationGenerator.errors = this.errors().toMutableMap()
        this.operationGenerator.skipped = this.skipped().toMutableList()

        return this.operationGenerator.generate(steps, jp.getThis()).toList()
    }

    @Pointcut("execution (* io.cucumber.core.runner.PickleStepTestStep.getUri(..))")
    protected open fun uri() {
    }

    @Around(value = "uri()")
    fun uri(jp: ProceedingJoinPoint): URI {
        val step = Reflecation.get(jp.getThis(), "step") as Step

        if (this.includeGenerator.uris.containsKey(step.id)) {
            return this.includeGenerator.uris[step.id]!!
        }

        return Reflecation.get(jp.getThis(), "uri") as URI
    }

    @Pointcut("execution (io.cucumber.plugin.event.TestStepFinished.new(..)) && args(timeInstant, testCase, testStep, testResult)")
    protected open fun result(
        timeInstant: Instant,
        testCase: TestCase,
        testStep: TestStep,
        testResult: Result,
    ) {
    }

    @After(value = "result(timeInstant, testCase, testStep, testResult)")
    open fun result(
        jp: JoinPoint,
        timeInstant: Instant,
        testCase: TestCase,
        testStep: TestStep,
        testResult: Result,
    ) {
        val results = Reflecation.field(jp.getThis(), "result")
        val result = Reflecation.get(jp.getThis(), results) as Result
        try {
            val id: String = (Reflecation.get(testStep, "step") as Step).id

            if (this.skipped().contains(id)) {
                results[jp.getThis()] = Result(Status.SKIPPED, result.duration, result.error)
            }
        } catch (_: NoSuchFieldException) {
            results[jp.getThis()] = Result(Status.SKIPPED, result.duration, result.error)
        }
    }

    private fun skipped(): List<String> {
        return this.includeGenerator.skipped + this.operationGenerator.skipped
    }

    private fun errors(): Map<String, String> {
        return this.includeGenerator.errors + this.operationGenerator.errors
    }
}
