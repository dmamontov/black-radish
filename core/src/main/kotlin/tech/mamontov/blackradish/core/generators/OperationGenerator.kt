package tech.mamontov.blackradish.core.generators

import io.cucumber.core.gherkin.Step
import io.cucumber.core.stepexpression.Argument
import org.apache.commons.lang3.math.NumberUtils
import tech.mamontov.blackradish.core.aspects.ReplacementAspect
import tech.mamontov.blackradish.core.data.ConditionStatus
import tech.mamontov.blackradish.core.enumerated.StepToken
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.reflecation.Reflecation
import java.util.UUID

/**
 * Operation generator
 *
 * @author Dmitry Mamontov
 *
 * @property stepTextMap MutableMap<String, String>
 * @property conditionStack ThreadLocal<MutableList<ConditionStatus>>
 */
@Suppress("UNCHECKED_CAST")
class OperationGenerator : Logged, StepGenerator() {
    private val stepTextMap: MutableMap<String, String> = mutableMapOf()

    private val conditionStack: ThreadLocal<MutableList<ConditionStatus>> =
        object : ThreadLocal<MutableList<ConditionStatus>>() {
            override fun initialValue(): MutableList<ConditionStatus> {
                return mutableListOf()
            }
        }

    /**
     * Validate operation step
     *
     * @param pickle Any
     * @param step Step
     */
    fun validateStep(pickle: Any, step: Step) {
        val token = getToken(pickle, step)
        when (token) {
            StepToken.IF -> {
                if (conditionStack.get().isEmpty()) {
                    val valid = this.validateCondition(step.text)
                    conditionStack.get().add(
                        ConditionStatus(token, valid, valid),
                    )

                    return
                }

                val valid = if (!this.conditionStack.get().last().proceed) {
                    this.skippedSteps.add(step.id)
                    false
                } else {
                    this.validateCondition(step.text)
                }

                conditionStack.get().add(
                    ConditionStatus(
                        token,
                        valid,
                        valid,
                        this.conditionStack.get().last().successful,
                        this.conditionStack.get().last().proceed,
                    ),
                )
            }

            StepToken.ELSEIF, StepToken.ELSE -> {
                val successful: Boolean
                val proceed: Boolean

                if (!this.conditionStack.get().last().parentProceed) {
                    this.skippedSteps.add(step.id)
                }

                if (this.isNotProceed()) {
                    successful = this.conditionStack.get().last().successful
                    proceed = false
                } else if (StepToken.ELSE == token) {
                    successful = !this.conditionStack.get().last().successful
                    proceed = successful
                } else {
                    successful = this.validateCondition(step.text)
                    proceed = successful
                }

                val last = this.conditionStack.get().last()
                conditionStack.get().removeLast()

                conditionStack.get().add(
                    ConditionStatus(
                        token,
                        successful,
                        proceed,
                        last.parentSuccessful,
                        last.parentProceed,
                    ),
                )
            }

            StepToken.ENDIF -> {
                conditionStack.get().removeLast()
            }

            else -> {}
        }

        if (this.isSkipped() && listOf(StepToken.ENDIF, StepToken.UNDEFINED).contains(token)) {
            this.skippedSteps.add(step.id)
        }
    }

    /**
     * Generate steps
     *
     * @param steps MutableList<Step>
     * @param pickle Any
     * @return MutableList<Step>
     */
    fun generateSteps(steps: MutableList<Step>, pickle: Any): MutableList<Step> {
        var targetSteps = steps

        if (this.isBalanced(pickle, steps)) {
            targetSteps = this.generateLoops(pickle, steps)
        }

        return targetSteps
    }

    /**
     * Generate loops
     *
     * @param pickle Any
     * @param steps MutableList<Step>
     * @return MutableList<Step>
     */
    private fun generateLoops(pickle: Any, steps: MutableList<Step>): MutableList<Step> {
        val targetSteps: MutableList<Step> = mutableListOf()
        val stack: MutableList<Pair<Step, StepToken>> = mutableListOf()
        var sub = false

        steps.forEach { step: Step ->
            if (this.stepErrors.containsKey(step.id) || this.skippedSteps.contains(step.id)) {
                targetSteps += step
                return@forEach
            }

            val token = getToken(pickle, step)
            if (StepToken.LOOP == token) {
                stack.add(Pair(step, token))

                if (stack.isNotEmpty()) {
                    sub = true
                }
            } else if (StepToken.ENDLOOP == token) {
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

        return if (sub) this.generateLoops(pickle, targetSteps) else targetSteps
    }

    /**
     * Generate loop
     *
     * @param pickle Any
     * @param start Step
     * @param end Step
     * @param steps MutableList<Step>
     * @return MutableList<Step>
     */
    private fun generateLoop(pickle: Any, start: Step, end: Step, steps: MutableList<Step>): MutableList<Step> {
        val subSteps = steps.subList(steps.indexOf(start) + 1, steps.indexOf(end))

        val targetSteps: MutableList<Step> = mutableListOf()

        val match = this.pickleStepDefinitionMatchMethod!!.call(pickle, start)
        val arguments = Reflecation.getValue(match, "arguments", true) as List<Argument>

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

    /**
     * Replace step text
     *
     * @param value Any
     * @param steps MutableList<Step>
     * @return MutableList<Step>
     */
    private fun replace(value: Any, steps: MutableList<Step>): MutableList<Step> {
        val targetSteps: MutableList<Step> = mutableListOf()

        steps.forEach { step: Step ->
            targetSteps += this.replaceUuid(this.replaceText(step, value))
        }

        return targetSteps
    }

    /**
     * Is balanced steps
     *
     * @param pickle Any
     * @param steps MutableList<Step>
     * @return Boolean
     */
    private fun isBalanced(pickle: Any, steps: MutableList<Step>): Boolean {
        val stack: MutableList<Pair<Step, StepToken>> = mutableListOf()
        var stepError: Pair<Step, String>? = null
        var balanced = true

        run breaking@{
            steps.forEach { step: Step ->
                if (this.stepErrors.containsKey(step.id) || this.skippedSteps.contains(step.id)) {
                    return@forEach
                }

                stepError = this.getErrorStep(
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

        this.stepErrors[stepError!!.first.id] = stepError!!.second

        steps.subList(steps.indexOf(stepError!!.first) + 1, steps.size).forEach { step: Step ->
            this.skippedSteps.add(step.id)
        }

        return balanced
    }

    /**
     * Get error step
     *
     * @param stepToken StepToken
     * @param step Step
     * @param stack MutableList<Pair<Step, StepToken>>
     * @return Pair<Step, String>?
     */
    private fun getErrorStep(
        stepToken: StepToken,
        step: Step,
        stack: MutableList<Pair<Step, StepToken>>,
    ): Pair<Step, String>? {
        if (StepToken.UNDEFINED === stepToken) {
            return null
        }

        if (listOf(StepToken.LOOP, StepToken.IF).contains(stepToken)) {
            stack.add(Pair(step, stepToken))

            return null
        }

        if (!listOf(StepToken.ELSEIF, StepToken.ELSE, StepToken.ENDIF, StepToken.ENDLOOP).contains(stepToken)) {
            return null
        }

        val expected = when (stepToken) {
            StepToken.ELSEIF, StepToken.ELSE -> listOf(StepToken.IF, StepToken.ELSEIF)
            StepToken.ENDIF -> listOf(StepToken.IF, StepToken.ELSEIF, StepToken.ELSE)
            StepToken.ENDLOOP -> listOf(StepToken.LOOP)
            else -> listOf()
        }

        if (stack.isEmpty() || !expected.contains(stack.last().second)) {
            if (listOf(StepToken.ENDLOOP, StepToken.ENDIF).contains(stepToken)) {
                var lastPair: Pair<Step, StepToken>? = null
                if (stack.isNotEmpty()) {
                    lastPair = stack.filter {
                        listOf(StepToken.LOOP, StepToken.IF, StepToken.ELSEIF, StepToken.ELSE).contains(it.second)
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
                    "Closing statement '" + stepToken.toString().lowercase() + "' when there is nothing to close",
                )
            }

            return Pair(
                step,
                "Before '" + stepToken.toString().lowercase() + "', expect '" + expected.joinToString(
                    "' or '",
                ) {
                    it.toString().lowercase()
                } + "'",
            )
        }

        stack.removeLast()
        if (listOf(StepToken.ELSEIF, StepToken.ELSE).contains(stepToken)) {
            stack.add(Pair(step, stepToken))
        }

        return null
    }

    /**
     * Validate condition
     *
     * @param condition String
     * @return Boolean
     */
    private fun validateCondition(condition: String): Boolean {
        val match: MatchResult? = Regex("\'(.*)\'").find(condition)
        if (match === null) {
            return false
        }

        return ReplacementAspect.replace(
            "\${eval:" + match.value.trim('\'') + "}",
        ).toBoolean()
    }

    /**
     * Replace step text
     *
     * @param step Step
     * @param value Any
     * @return Step
     */
    private fun replaceText(step: Step, value: Any): Step {
        val pickleStep = Reflecation.getValue(step, "pickleStep")
        val valueField = Reflecation.field(pickleStep, "text")

        var text = valueField[pickleStep] as String
        if (this.stepTextMap.containsKey(step.id)) {
            text = this.stepTextMap[step.id]!!
        } else {
            this.stepTextMap[step.id] = text
        }
        valueField[pickleStep] = text.replace("\${loop.value}", value.toString())

        return Reflecation.clone(step) as Step
    }

    /**
     * Replace step uuid
     *
     * @param step Step
     * @return Step
     */
    private fun replaceUuid(step: Step): Step {
        val pickleStep = Reflecation.getValue(step, "pickleStep")
        val idField = Reflecation.field(pickleStep, "id")
        idField[pickleStep] = UUID.randomUUID().toString()

        return Reflecation.clone(step) as Step
    }

    /**
     * Step is skipped
     *
     * @return Boolean
     */
    private fun isSkipped(): Boolean {
        return this.conditionStack.get().isNotEmpty() && !this.conditionStack.get().last().proceed
    }

    /**
     * Step is not proceed
     *
     * @return Boolean
     */
    private fun isNotProceed(): Boolean {
        return this.conditionStack.get().last().successful || !this.conditionStack.get().last().parentProceed
    }
}
