package tech.mamontov.blackradish.core.specs

import org.apache.commons.lang3.math.NumberUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.properties.ThreadProperty

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

            ComparisonOperation.IS_HIGHER_THAN, ComparisonOperation.IS_LOWER_THAN -> {
                listOf(first, second).forEach { number: String ->
                    Assertions.assertThat(NumberUtils.isCreatable(number))
                        .`as`("The value of '%s' must be a number.", number).isTrue
                }

                val assert = Assertions.assertThat(NumberUtils.createNumber(first).toDouble())
                if (operation == ComparisonOperation.IS_HIGHER_THAN) {
                    assert.isGreaterThan(NumberUtils.createNumber(second).toDouble())
                } else {
                    assert.isLessThan(NumberUtils.createNumber(second).toDouble())
                }
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
}
