package tech.mamontov.blackradish.core.asserts

import org.apache.commons.lang3.math.NumberUtils
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions

/**
 * Custom number assertions
 *
 * @see [AssertJ](https://assertj.github.io/doc/)
 *
 * @author Dmitry Mamontov
 *
 * @property number String
 * @property isNumber NumberAssert
 * @constructor
 */
@Suppress("KDocUnresolvedReference")
class NumberAssert(val number: String) : AbstractAssert<NumberAssert, String>(number, NumberAssert::class.java) {
    companion object {
        /**
         * Static constructor
         *
         * @param number Any
         * @return NumberAssert
         */
        fun assertThat(number: Any): NumberAssert {
            return NumberAssert(number.toString())
        }
    }

    val isNumber
        get() = this.isNumber(number)

    /**
     * Assert is two number equals
     *
     * @param number String
     * @return NumberAssert
     */
    fun isEqualTo(number: String): NumberAssert {
        this.isNumber
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isEqualTo(NumberUtils.createNumber(number).toDouble())

        return this
    }

    /**
     * Assert number is greater than number
     *
     * @param number String
     * @return NumberAssert
     */
    fun isGreaterThan(number: String): NumberAssert {
        this.isNumber
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isGreaterThan(NumberUtils.createNumber(number).toDouble())

        return this
    }

    /**
     * Assert number is less than number
     *
     * @param number String
     * @return NumberAssert
     */
    fun isLessThan(number: String): NumberAssert {
        this.isNumber
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isLessThan(NumberUtils.createNumber(number).toDouble())

        return this
    }

    /**
     * String is number
     *
     * @param number String
     * @return NumberAssert
     */
    private fun isNumber(number: String): NumberAssert {
        if (!NumberUtils.isCreatable(number)) {
            failWithMessage("The value of '%s' must be a number.", number)
        }

        return this
    }
}
