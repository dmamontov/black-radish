package tech.mamontov.blackradish.core.specs.base

import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.NumberAssert
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.helpers.UriHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.properties.ThreadProperty
import java.net.URI

abstract class BaseSpec : Logged {
    open fun wait(seconds: Int) {
        Thread.sleep((seconds * 1000).toLong())
    }

    open fun save(value: String, key: String) {
        ThreadProperty.set(key, value)
    }

    open fun comparison(first: String, operation: ComparisonOperation, second: String) {
        when (operation) {
            ComparisonOperation.IS -> {
                Assertions.assertThat(first).isEqualTo(second)
            }

            ComparisonOperation.MATCHES -> {
                Assertions.assertThat(first).matches(second)
            }

            ComparisonOperation.IS_HIGHER_THAN -> {
                NumberAssert.assertThat(first).isGreaterThan(second)
            }

            ComparisonOperation.IS_LOWER_THAN -> {
                NumberAssert.assertThat(first).isLessThan(second)
            }

            ComparisonOperation.CONTAINS -> {
                Assertions.assertThat(first).contains(second)
            }

            ComparisonOperation.IS_DIFFERENT_FROM -> {
                Assertions.assertThat(first).isNotEqualTo(second)
            }
        }
    }

    open fun aspectIf(condition: String) {}

    open fun aspectElseIf(condition: String) {}

    open fun aspectElse() {}

    open fun aspectEndIf() {}

    open fun aspectLoop(condition: String) {}

    open fun aspectLoop(from: Int, to: Int) {}

    open fun aspectEndLoop() {}

    open fun aspectInclude(scenario: String, feature: String?) {}

    protected fun uri(path: String): URI? {
        var uri: URI? = null

        try {
            uri = try {
                UriHelper.uri(path)
            } catch (_: AssertionError) {
                UriHelper.current(path)
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        return uri
    }
}
