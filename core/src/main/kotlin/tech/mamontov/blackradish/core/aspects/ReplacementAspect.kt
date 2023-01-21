package tech.mamontov.blackradish.core.aspects

import io.cucumber.core.backend.TestCaseState
import io.cucumber.core.stepexpression.Argument
import io.cucumber.core.stepexpression.DataTableArgument
import io.cucumber.core.stepexpression.DocStringArgument
import io.cucumber.core.stepexpression.ExpressionArgument
import io.cucumber.cucumberexpressions.Group
import org.apache.commons.text.StringSubstitutor
import org.apache.commons.text.lookup.StringLookup
import org.apache.commons.text.lookup.StringLookupFactory
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.lookups.DefaultLookup
import tech.mamontov.blackradish.core.lookups.EvalLookup
import tech.mamontov.blackradish.core.lookups.FakerLookup
import tech.mamontov.blackradish.core.lookups.LowerLookup
import tech.mamontov.blackradish.core.lookups.MathLookup
import tech.mamontov.blackradish.core.lookups.UpperLookup
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation

@Aspect
@Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")
open class ReplacementAspect : Logged {
    companion object {
        private val lookups: Map<String, StringLookup> = object : HashMap<String, StringLookup>() {
            init {
                put("upper", UpperLookup())
                put("lower", LowerLookup())
                put("faker", FakerLookup())
                put("math", MathLookup())
                put("eval", EvalLookup())
            }
        }

        private val substitutor: StringSubstitutor = StringSubstitutor(
            StringLookupFactory.INSTANCE.interpolatorStringLookup(
                this.lookups,
                DefaultLookup(),
                true,
            ),
        )

        init {
            this.substitutor.isEnableSubstitutionInVariables = true
            this.substitutor.isEnableUndefinedVariableException = true
        }

        fun replace(element: String): String {
            try {
                return this.substitutor.replace(element)
            } catch (e: Exception) {
                Assertions.fail<Any>(e.message, e)
            }

            return element
        }
    }

    @Pointcut("execution (* io.cucumber.core.runner.PickleStepDefinitionMatch.runStep(..)) && args(state)")
    @Suppress("unused")
    protected open fun replacement(state: TestCaseState?) {
    }

    @Before(value = "replacement(state)")
    fun replacement(jp: JoinPoint, state: TestCaseState?) {
        if (!Reflecation.match(jp.getThis(), "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
            return
        }

        val arguments = Reflecation.get(jp.getThis(), "arguments", true) as List<Argument>
        arguments.forEach { argument: Argument ->
            when (argument) {
                is ExpressionArgument -> this.replaceExpression(argument)
                is DataTableArgument -> this.replaceDataTable(argument)
                is DocStringArgument -> this.replaceDocString(argument)
            }
        }
    }

    private fun replaceExpression(argument: Argument) {
        val textArgument = Reflecation.get(argument, "argument") as io.cucumber.cucumberexpressions.Argument<*>

        val group = textArgument.group
        val currentValue = group.value ?: return

        val valueField = Reflecation.field(group, "value")
        valueField[group] = replace(currentValue)

        (group.children as List<Group>).forEach { childGroup ->
            val childField = Reflecation.field(childGroup, "value")

            if (childGroup.value !== null) {
                childField[childGroup] = replace(childGroup.value)
            }
        }
    }

    private fun replaceDataTable(argument: Argument) {
        val textArgument = Reflecation.field(argument, "argument")
        val rows = textArgument[argument] as List<MutableList<String>>

        textArgument[argument] = rows.map { row: MutableList<String> ->
            row.map { value: String -> replace(value) }
        }
    }

    private fun replaceDocString(argument: Argument) {
        val contentArgument = Reflecation.field(argument, "content")

        contentArgument[argument] = replace(contentArgument[argument] as String)
    }
}
