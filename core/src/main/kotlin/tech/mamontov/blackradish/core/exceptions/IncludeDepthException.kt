package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

class IncludeDepthException(step: Step, message: String) : IncludeException(step, message)
