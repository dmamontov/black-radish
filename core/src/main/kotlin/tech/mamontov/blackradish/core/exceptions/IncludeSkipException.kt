package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

class IncludeSkipException(val steps: MutableList<Step>) : Exception()
