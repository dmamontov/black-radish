package tech.mamontov.blackradish.core.generators

import io.cucumber.core.backend.StepDefinition
import io.cucumber.core.gherkin.Step
import tech.mamontov.blackradish.core.enumerated.StepToken
import tech.mamontov.blackradish.core.exceptions.IncludeException
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.parsers.AspectTokenParser
import tech.mamontov.blackradish.core.reflecation.Reflecation
import tech.mamontov.blackradish.core.reflecation.ReflecationMethod

/**
 * Abstract step generator
 *
 * @author Dmitry Mamontov
 *
 * @property pickleStepDefinitionMatchMethod ReflecationMethod?
 * @property stepErrors MutableMap<String, String>
 * @property skippedSteps MutableList<String>
 */
abstract class StepGenerator : Logged {
    var pickleStepDefinitionMatchMethod: ReflecationMethod? = null

    var stepErrors: MutableMap<String, String> = mutableMapOf()

    var skippedSteps: MutableList<String> = mutableListOf()

    /**
     * Step is token
     *
     * @param pickle Any
     * @param step Step
     * @return Boolean
     */
    protected fun `is`(pickle: Any, step: Step): Boolean {
        return this.isToken(pickle, step, StepToken.INCLUDE)
    }

    /**
     * Get step token
     *
     * @param pickle Any
     * @param step Step
     * @param retry Int
     * @return StepToken
     */
    protected fun getToken(pickle: Any, step: Step, retry: Int = 1): StepToken {
        val stepDefinition: StepDefinition =
            if (Reflecation.instanceOf(pickle, "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
                Reflecation.getValue(pickle, "stepDefinition") as StepDefinition
            } else {
                try {
                    val match = this.pickleStepDefinitionMatchMethod?.call(pickle, step) ?: return StepToken.UNDEFINED
                    Reflecation.method(match, "getStepDefinition").call() as StepDefinition
                } catch (_: Exception) {
                    if (retry >= 10) {
                        return StepToken.UNDEFINED
                    }

                    return this.getToken(pickle, step, retry + 1)
                }
            }

        return AspectTokenParser.parseLocation(stepDefinition.location)
    }

    /**
     * Throw step exception
     *
     * @param exception IncludeException
     */
    protected fun throwStep(exception: IncludeException) {
        this.stepErrors[exception.step.id] = exception.message

        throw exception
    }

    /**
     * Step is token
     *
     * @param pickle Any
     * @param step Step
     * @param token StepToken
     * @return Boolean
     */
    private fun isToken(pickle: Any, step: Step, token: StepToken): Boolean {
        return this.getToken(pickle, step) === token
    }
}
