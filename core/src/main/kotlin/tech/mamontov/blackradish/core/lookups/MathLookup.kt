package tech.mamontov.blackradish.core.lookups

import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged

class MathLookup : Logged, StringLookup {
    override fun lookup(key: String): String {
        return ExpressionBuilder(key).build().evaluate().toString()
    }
}
