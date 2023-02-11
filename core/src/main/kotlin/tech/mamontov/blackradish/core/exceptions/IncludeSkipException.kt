package tech.mamontov.blackradish.core.exceptions

import io.cucumber.core.gherkin.Step

/**
 * Include skip exception
 *
 * @author Dmitry Mamontov
 *
 * @property steps MutableList<Step>
 * @constructor
 */
class IncludeSkipException(val steps: MutableList<Step>) : Exception()
