package tech.mamontov.blackradish.core.parsers

import tech.mamontov.blackradish.core.enumerated.StepToken
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import java.util.Locale

/**
 * Token parser
 *
 * @author Dmitry Mamontov
 */
class AspectTokenParser : Logged {
    companion object {
        private const val ASPECT_PREFIX = "aspect"

        private var map: HashMap<String, StepToken> = hashMapOf()

        init {
            map = BaseSpec::class.java.declaredMethods.filter { it.name.startsWith(ASPECT_PREFIX) }.associate {
                "." + ASPECT_PREFIX + it.toGenericString().split(ASPECT_PREFIX)[1] to StepToken.valueOf(
                    it.name.replace(ASPECT_PREFIX, "").uppercase(Locale.getDefault()),
                )
            } as HashMap<String, StepToken>
        }

        /**
         * Parse location to token
         *
         * @param location String
         * @return StepToken
         */
        fun parseLocation(location: String): StepToken {
            for ((key: String, value: StepToken) in map) {
                if (location.endsWith(key)) {
                    return value
                }
            }

            return StepToken.UNDEFINED
        }
    }
}
