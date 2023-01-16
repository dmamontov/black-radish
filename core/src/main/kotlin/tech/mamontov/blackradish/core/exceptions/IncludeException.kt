package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

open class IncludeException(val step: Step, override val message: String) : Exception(message)
