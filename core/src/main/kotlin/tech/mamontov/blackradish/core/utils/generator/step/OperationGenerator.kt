package tech.mamontov.blackradish.core.utils.generator.step

import io.cucumber.core.gherkin.Step
import io.cucumber.core.stepexpression.Argument
import org.apache.commons.lang3.math.NumberUtils
import tech.mamontov.blackradish.core.aspects.ReplacementAspect
import tech.mamontov.blackradish.core.enumerated.Token
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.data.ConditionStatus
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation
import java.util.UUID

@Suppress("UNCHECKED_CAST")
class OperationGenerator : Logged, StepGenerator() {
    private val texts: MutableMap<String, String> = mutableMapOf()

    private val conditionStack: MutableList<ConditionStatus> = mutableListOf()

    fun proceed(pickle: Any, step: Step) {
        when (val token = getToken(pickle, step)) {
            Token.IF -> {
                if (conditionStack.isEmpty()) {
                    val valid = this.validate(step.text)
                    conditionStack.add(
                        ConditionStatus(token, valid, valid),
                    )

                    return
                }

                val valid = if (!this.conditionStack.last().proceed) {
                    this.skipped.add(step.id)
                    false
                } else {
                    this.validate(step.text)
                }

                conditionStack.add(
                    ConditionStatus(
                        token,
                        valid,
                        valid,
                        this.conditionStack.last().successful,
                        this.conditionStack.last().proceed,
                    ),
                )
            }

            Token.ELSEIF, Token.ELSE -> {
                val successful: Boolean
                val proceed: Boolean

                if (!this.conditionStack.last().parentProceed) {
                    this.skipped.add(step.id)
                }

                if (this.isNotProceed()) {
                    successful = this.conditionStack.last().successful
                    proceed = false
                } else if (Token.ELSE == token) {
                    successful = !this.conditionStack.last().successful
                    proceed = successful
                } else {
                    successful = this.validate(step.text)
                    proceed = successful
                }

                val last = this.conditionStack.last()
                conditionStack.removeLast()

                conditionStack.add(
                    ConditionStatus(
                        token,
                        successful,
                        proceed,
                        last.parentSuccessful,
                        last.parentProceed,
                    ),
                )
            }

            Token.ENDIF -> {
                conditionStack.removeLast()

                if (this.isSkipped()) {
                    this.skipped.add(step.id)
                }
            }

            else -> {
                if (this.isSkipped()) {
                    this.skipped.add(step.id)
                }
            }
        }
    }

    fun generate(steps: MutableList<Step>, pickle: Any): MutableList<Step> {
        var targetSteps = steps

        if (this.balanced(pickle, steps)) {
            targetSteps = this.generateLoop(pickle, steps)
        }

        return targetSteps
    }

    private fun generateLoop(pickle: Any, steps: MutableList<Step>): MutableList<Step> {
        val targetSteps: MutableList<Step> = mutableListOf()
        val stack: MutableList<Pair<Step, Token>> = mutableListOf()
        var sub = false

        steps.forEach { step: Step ->
            if (this.errors.containsKey(step.id) || this.skipped.contains(step.id)) {
                targetSteps += step
                return@forEach
            }

            val token = getToken(pickle, step)
            if (Token.LOOP == token) {
                stack.add(Pair(step, token))

                if (stack.isNotEmpty()) {
                    sub = true
                }
            } else if (Token.ENDLOOP == token) {
                if (stack.size == 1) {
                    targetSteps += this.generateLoop(pickle, stack.last().first, step, steps)

                    stack.removeLast()
                    return@forEach
                }

                stack.removeLast()
            }

            if (stack.isEmpty()) {
                targetSteps += step
            }
        }

        return if (sub) this.generateLoop(pickle, targetSteps) else targetSteps
    }

    private fun generateLoop(pickle: Any, start: Step, end: Step, steps: MutableList<Step>): MutableList<Step> {
        val subSteps = steps.subList(steps.indexOf(start) + 1, steps.indexOf(end))

        val targetSteps: MutableList<Step> = mutableListOf()

        val match = this.matcher!!.call(pickle, start)
        val arguments = Reflecation.get(match, "arguments", true) as List<Argument>

        when (arguments.size) {
            1 -> {
                arguments[0].value.toString().split(",").forEach { value: String ->
                    targetSteps += this.replace(value.trim(), subSteps)
                }
            }

            2 -> {
                val numberFrom = NumberUtils.createNumber(arguments[0].value.toString().trim()).toInt()
                val numberTo = NumberUtils.createNumber(arguments[1].value.toString().trim()).toInt()

                if (numberFrom <= numberTo) {
                    for (number in numberFrom..numberTo) {
                        targetSteps += this.replace(number, subSteps)
                    }
                } else {
                    for (number in numberFrom downTo numberTo) {
                        targetSteps += this.replace(number, subSteps)
                    }
                }
            }
        }

        return targetSteps
    }

    private fun replace(value: Any, steps: MutableList<Step>): MutableList<Step> {
        val targetSteps: MutableList<Step> = mutableListOf()

        steps.forEach { step: Step ->
            targetSteps += this.replaceId(this.replaceText(step, value))
        }

        return targetSteps
    }

    private fun balanced(pickle: Any, steps: MutableList<Step>): Boolean {
        val stack: MutableList<Pair<Step, Token>> = mutableListOf()
        var stepError: Pair<Step, String>? = null
        var balanced = true

        run breaking@{
            steps.forEach { step: Step ->
                if (this.errors.containsKey(step.id) || this.skipped.contains(step.id)) {
                    return@forEach
                }

                stepError = this.error(
                    this.getToken(pickle, step),
                    step,
                    stack,
                )

                if (stepError != null) {
                    balanced = false

                    return@breaking
                }
            }
        }

        if (stepError === null && stack.isNotEmpty()) {
            stepError = Pair(
                stack.first().first,
                "At the end, left open: '" + stack.joinToString("', '") {
                    it.second.toString().lowercase()
                } + "'",
            )
        }

        if (stepError === null) {
            return balanced
        }

        this.errors[stepError!!.first.id] = stepError!!.second

        steps.subList(steps.indexOf(stepError!!.first) + 1, steps.size).forEach { step: Step ->
            this.skipped.add(step.id)
        }

        return balanced
    }

    private fun error(token: Token, step: Step, stack: MutableList<Pair<Step, Token>>): Pair<Step, String>? {
        if (Token.UNDEFINED === token) {
            return null
        }

        if (listOf(Token.LOOP, Token.IF).contains(token)) {
            stack.add(Pair(step, token))

            return null
        }

        if (!listOf(Token.ELSEIF, Token.ELSE, Token.ENDIF, Token.ENDLOOP).contains(token)) {
            return null
        }

        val expected = when (token) {
            Token.ELSEIF, Token.ELSE -> listOf(Token.IF, Token.ELSEIF)
            Token.ENDIF -> listOf(Token.IF, Token.ELSEIF, Token.ELSE)
            Token.ENDLOOP -> listOf(Token.LOOP)
            else -> listOf()
        }

        if (stack.isEmpty() || !expected.contains(stack.last().second)) {
            if (listOf(Token.ENDLOOP, Token.ENDIF).contains(token)) {
                var lastPair: Pair<Step, Token>? = null
                if (stack.isNotEmpty()) {
                    lastPair = stack.filter {
                        listOf(Token.LOOP, Token.IF, Token.ELSEIF, Token.ELSE).contains(it.second)
                    }.last()
                }

                if (lastPair !== null) {
                    return Pair(
                        lastPair.first,
                        "At the end, left open: '" + lastPair.second.toString().lowercase() + "'",
                    )
                }

                return Pair(
                    step,
                    "Closing statement '" + token.toString().lowercase() + "' when there is nothing to close",
                )
            }

            return Pair(
                step,
                "Before '" + token.toString().lowercase() + "', expect '" + expected.joinToString(
                    "' or '",
                ) {
                    it.toString().lowercase()
                } + "'",
            )
        }

        stack.removeLast()
        if (listOf(Token.ELSEIF, Token.ELSE).contains(token)) {
            stack.add(Pair(step, token))
        }

        return null
    }

    private fun validate(condition: String): Boolean {
        val match: MatchResult? = Regex("\'(.*)\'").find(condition)
        if (match === null) {
            return false
        }

        return ReplacementAspect.replace(
            "\${eval:" + match.value.trim('\'') + "}",
        ).toBoolean()
    }

    private fun replaceText(step: Step, value: Any): Step {
        val pickleStep = Reflecation.get(step, "pickleStep")
        val valueField = Reflecation.field(pickleStep, "text")

        var text = valueField[pickleStep] as String
        if (this.texts.containsKey(step.id)) {
            text = this.texts[step.id]!!
        } else {
            this.texts[step.id] = text
        }
        valueField[pickleStep] = text.replace("\${loop.value}", value.toString())

        return Reflecation.clone(step) as Step
    }

    private fun replaceId(step: Step): Step {
        val pickleStep = Reflecation.get(step, "pickleStep")
        val idField = Reflecation.field(pickleStep, "id")
        idField[pickleStep] = UUID.randomUUID().toString()

        return Reflecation.clone(step) as Step
    }

    private fun isSkipped(): Boolean {
        return this.conditionStack.isNotEmpty() && !this.conditionStack.last().proceed
    }

    private fun isNotProceed(): Boolean {
        return this.conditionStack.last().successful || !this.conditionStack.last().parentProceed
    }
}
