package tech.mamontov.blackradish.core.asserts

import org.apache.commons.lang3.math.NumberUtils
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions

class NumberAssert(val number: String) : AbstractAssert<NumberAssert, String>(number, NumberAssert::class.java) {
    companion object {
        fun assertThat(number: Any): NumberAssert {
            return NumberAssert(number.toString())
        }
    }

    fun isNumber(): NumberAssert {
        return this.isNumber(number)
    }

    private fun isNumber(number: String): NumberAssert {
        if (!NumberUtils.isCreatable(number)) {
            failWithMessage("The value of '%s' must be a number.", number)
        }

        return this
    }

    fun isEqualTo(number: String): NumberAssert {
        this.isNumber()
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isEqualTo(NumberUtils.createNumber(number).toDouble())

        return this
    }

    fun isGreaterThan(number: String): NumberAssert {
        this.isNumber()
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isGreaterThan(NumberUtils.createNumber(number).toDouble())

        return this
    }

    fun isLessThan(number: String): NumberAssert {
        this.isNumber()
        this.isNumber(number)

        Assertions.assertThat(
            NumberUtils.createNumber(this.number).toDouble(),
        )
            .isLessThan(NumberUtils.createNumber(number).toDouble())

        return this
    }
}
