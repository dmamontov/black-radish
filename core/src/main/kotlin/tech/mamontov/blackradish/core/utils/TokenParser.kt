package tech.mamontov.blackradish.core.utils

import tech.mamontov.blackradish.core.enumerated.Token
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import java.util.Locale

class TokenParser : Logged {
    companion object {
        private const val ASPECT_PREFIX = "aspect"

        private var map: HashMap<String, Token> = hashMapOf()

        init {
            this.map = BaseSpec::class.java.declaredMethods.filter { it.name.startsWith(ASPECT_PREFIX) }.associate {
                "." + ASPECT_PREFIX + it.toGenericString().split(ASPECT_PREFIX)[1] to Token.valueOf(
                    it.name.replace(ASPECT_PREFIX, "").uppercase(Locale.getDefault()),
                )
            } as HashMap<String, Token>
        }

        fun parse(location: String): Token {
            for ((key: String, value: Token) in this.map) {
                if (location.endsWith(key)) {
                    return value
                }
            }

            return Token.UNDEFINED
        }
    }
}
