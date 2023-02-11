package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

/**
 * Include depth exception
 *
 * @author Dmitry Mamontov
 *
 * @property step Step
 * @property message String
 * @constructor
 */
class IncludeDepthException(override val step: Step, override val message: String) : IncludeException(step, message)
