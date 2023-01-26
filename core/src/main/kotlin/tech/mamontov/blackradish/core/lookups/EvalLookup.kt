package tech.mamontov.blackradish.core.lookups

import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class EvalLookup : Logged, StringLookup {
    override fun lookup(key: String): String {
        val engine = ScriptEngineManager().getEngineByName("kotlin")

        try {
            return engine.eval(key).toString()
        } catch (e: ScriptException) {
            throw IllegalArgumentException(e.message, e)
        }
    }
}
