package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * Evaluate kotlin lookup for replacement
 *
 * @author Dmitry Mamontov
 */
class EvalLookup : Logged, StringLookup {
    /**
     * Evaluate value
     *
     * @param value String
     * @return String
     */
    override fun lookup(value: String): String {
        val engine = ScriptEngineManager().getEngineByName("kotlin")

        try {
            return engine.eval(value).toString()
        } catch (e: ScriptException) {
            throw IllegalArgumentException(e.message, e)
        }
    }
}
