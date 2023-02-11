package tech.mamontov.blackradish.core.specs.base

import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.NumberAssert
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.core.storages.PropertyStorage

/**
 * Base spec
 *
 * @author Dmitry Mamontov
 */
open class BaseSpec : Logged {
    /**
     * Wait n seconds.
     *
     * @param seconds Int
     */
    open fun wait(seconds: Int) {
        Thread.sleep((seconds * 1000).toLong())
    }

    /**
     * Saving a value to a variable.
     *
     * @param value String
     * @param key String
     */
    open fun save(value: String, key: String) {
        ConvertedResultStorage.set(ConvertedResult(value))

        PropertyStorage.set(key, value)
    }

    /**
     * Comparison of two values.
     * Numbers are compared as double.
     *
     * @param first String
     * @param operation ComparisonOperation
     * @param second String
     */
    open fun comparison(first: String, operation: ComparisonOperation, second: String) {
        when (operation) {
            ComparisonOperation.IS -> {
                Assertions.assertThat(first.trim()).isEqualTo(second.trim())
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

    /**
     * Conditional operator - If.
     * The condition is written in kotlin.
     *
     * @param condition String
     */
    open fun aspectIf(condition: String) {}

    /**
     * Conditional operator - Else If.
     * The condition is written in kotlin.
     *
     * @param condition String
     */
    open fun aspectElseIf(condition: String) {}

    /**
     * Conditional operator - Else.
     */
    open fun aspectElse() {}

    /**
     * Conditional operator - End If.
     */
    open fun aspectEndIf() {}

    /**
     * Loop - joint.
     *
     * @param condition String
     */
    open fun aspectLoop(condition: String) {}

    /**
     * Loop - with a counter.
     *
     * @param from Int
     * @param to Int
     */
    open fun aspectLoop(from: Int, to: Int) {}

    /**
     * End loop
     */
    open fun aspectEndLoop() {}

    /**
     * Scenario reuse.
     *
     * @param scenario String
     * @param feature String?
     */
    open fun aspectInclude(scenario: String, feature: String?) {}
}
