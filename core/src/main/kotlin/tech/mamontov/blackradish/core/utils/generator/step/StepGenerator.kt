package tech.mamontov.blackradish.core.utils.generator.step

import io.cucumber.core.backend.StepDefinition
import io.cucumber.core.gherkin.Step
import tech.mamontov.blackradish.core.enumerated.Token
import tech.mamontov.blackradish.core.exceptions.IncludeException
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.TokenParser
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import tech.mamontov.blackradish.core.utils.reflecation.ReflecationMethod

abstract class StepGenerator : Logged {
    var matcher: ReflecationMethod? = null

    var errors: MutableMap<String, String> = mutableMapOf()

    var skipped: MutableList<String> = mutableListOf()

    protected fun match(pickle: Any, step: Step): Boolean {
        return this.match(pickle, step, Token.INCLUDE)
    }

    protected fun getToken(pickle: Any, step: Step): Token {
        val stepDefinition: StepDefinition =
            if (Reflecation.match(pickle, "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
                Reflecation.get(pickle, "stepDefinition") as StepDefinition
            } else {
                val match = this.matcher?.call(pickle, step) ?: return Token.UNDEFINED
                Reflecation.method(match, "getStepDefinition").call() as StepDefinition
            }

        return TokenParser.parse(stepDefinition.location)
    }

    protected fun throwStep(exception: IncludeException) {
        this.errors[exception.step.id] = exception.message

        throw exception
    }

    private fun match(pickle: Any, step: Step, token: Token): Boolean {
        return this.getToken(pickle, step) === token
    }
}
