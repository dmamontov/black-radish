package tech.mamontov.blackradish.core.lookups

import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged

/**
 * Math lookup for replacement
 *
 * @author Dmitry Mamontov
 */
class MathLookup : Logged, StringLookup {
    /**
     * Evaluate math expression
     *
     * @param expression String
     * @return String
     */
    override fun lookup(expression: String): String {
        return ExpressionBuilder(expression).build().evaluate().toString()
    }
}
