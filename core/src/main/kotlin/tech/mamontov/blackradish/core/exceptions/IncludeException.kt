package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

/**
 * Include exception
 *
 * @author Dmitry Mamontov
 *
 * @property step Step
 * @property message String
 * @constructor
 */
open class IncludeException(open val step: Step, override val message: String) : Exception(message)
