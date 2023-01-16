package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

class SkipException(val steps: MutableList<Step>) : Exception()
