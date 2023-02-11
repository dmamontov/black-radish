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
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.lookups.DefaultLookup
import tech.mamontov.blackradish.core.lookups.EvalLookup
import tech.mamontov.blackradish.core.lookups.FakerLookup
import tech.mamontov.blackradish.core.lookups.LowerLookup
import tech.mamontov.blackradish.core.lookups.MathLookup
import tech.mamontov.blackradish.core.lookups.UpperLookup
import tech.mamontov.blackradish.core.reflecation.Reflecation

/**
 * AspectJ aspect to replace variables in steps
 *
 * @see [AspectJ](https://www.eclipse.org/aspectj/)
 *
 * @author Dmitry Mamontov
 */
@Aspect
@Suppress(
    "UNCHECKED_CAST",
    "UNUSED_PARAMETER",
    "UNUSED",
    "KDocUnresolvedReference",
)
class ReplacementAspect : Logged {
    /**
     * @see [StringSubstitutor](https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/StringSubstitutor.html)
     *
     * @property lookups Map<String, StringLookup>
     * @property substitutor StringSubstitutor
     */
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

        /**
         * Replace string with **[StringSubstitutor]**
         *
         * @param string String
         * @return String
         */
        fun replace(string: String): String {
            try {
                return this.substitutor.replace(string)
            } catch (e: Exception) {
                Assertions.fail<Any>(e.message, e)
            }

            return string
        }
    }

    /**
     * Pointcut for **[io.cucumber.core.runner.PickleStepDefinitionMatch.runStep]**
     *
     * @param state: TestCaseState?
     */
    @Pointcut("execution (* io.cucumber.core.runner.PickleStepDefinitionMatch.runStep(..)) && args(state)")
    private fun runStep(state: TestCaseState?) {
    }

    /**
     * Action before **[io.cucumber.core.runner.PickleStepDefinitionMatch.runStep]**
     *
     * @param jp JoinPoint
     * @param state TestCaseState?
     */
    @Before(value = "runStep(state)")
    fun runStep(jp: JoinPoint, state: TestCaseState?) {
        if (!Reflecation.instanceOf(jp.getThis(), "io.cucumber.core.runner.PickleStepDefinitionMatch")) {
            return
        }

        val arguments = Reflecation.getValue(jp.getThis(), "arguments", true) as List<Argument>
        arguments.forEach { argument: Argument ->
            when (argument) {
                is ExpressionArgument -> this.replaceExpression(argument)
                is DataTableArgument -> this.replaceDataTable(argument)
                is DocStringArgument -> this.replaceDocString(argument)
            }
        }
    }

    /**
     * Replace string in Expression argument
     *
     * @param argument Argument
     */
    private fun replaceExpression(argument: Argument) {
        val textArgument = Reflecation.getValue(argument, "argument") as io.cucumber.cucumberexpressions.Argument<*>

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

    /**
     * Replace string in DataTable argument
     *
     * @param argument Argument
     */
    private fun replaceDataTable(argument: Argument) {
        val textArgument = Reflecation.field(argument, "argument")
        val rows = textArgument[argument] as List<MutableList<String>>

        textArgument[argument] = rows.map { row: MutableList<String> ->
            row.map { value: String -> replace(value) }
        }
    }

    /**
     * Replace string in DocString argument
     *
     * @param argument Argument
     */
    private fun replaceDocString(argument: Argument) {
        val contentArgument = Reflecation.field(argument, "content")

        contentArgument[argument] = replace(contentArgument[argument] as String)
    }
}
