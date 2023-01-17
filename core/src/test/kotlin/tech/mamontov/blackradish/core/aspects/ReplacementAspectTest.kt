package tech.mamontov.blackradish.core.aspects

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import tech.mamontov.blackradish.core.plugins.ConfigurationLoader
import tech.mamontov.blackradish.core.utils.property.Configuration

class ReplacementAspectTest {
    @Test
    fun replaceEmptyPropertyTest() {
        assertThat(ReplacementAspect.replace("")).isEqualTo("")
    }

    @Test
    fun replaceSystemPropertyTest() {
        System.setProperty("FIRST", "1")
        System.setProperty("SECOND", "2")

        assertThat(ReplacementAspect.replace("\${FIRST}")).isEqualTo("1")
        assertThat(ReplacementAspect.replace("\${FIRST}\${SECOND}")).isEqualTo("12")
        assertThat(ReplacementAspect.replace("\${FIRST}:\${SECOND}")).isEqualTo("1:2")
        assertThat(ReplacementAspect.replace("|\${FIRST}|:|\${SECOND}|")).isEqualTo("|1|:|2|")
    }

    @Test
    fun replaceUndefinedSystemPropertyTest() {
        Assertions.assertThatThrownBy {
            ReplacementAspect.replace("\${THREE}")
        }.isInstanceOf(AssertionError::class.java)
    }

    @Test
    fun replaceUpperCaseTest() {
        System.setProperty("FIRST", "1")
        System.setProperty("SECOND", "a")

        assertThat(ReplacementAspect.replace("\${upper:\${FIRST}}")).isEqualTo("1")
        assertThat(ReplacementAspect.replace("\${upper:\${SECOND}}")).isEqualTo("A")
        assertThat(ReplacementAspect.replace("\${upper:b}")).isEqualTo("B")
        assertThat(ReplacementAspect.replace("\${upper:\${FIRST}}\${upper:\${SECOND}}")).isEqualTo("1A")
        assertThat(ReplacementAspect.replace("\${upper:\${FIRST}}:\${upper:\${SECOND}}")).isEqualTo("1:A")
        assertThat(ReplacementAspect.replace("|\${upper:\${FIRST}}|:|\${upper:\${SECOND}}|")).isEqualTo("|1|:|A|")
    }

    @Test
    fun replaceLowerCaseTest() {
        System.setProperty("FIRST", "1")
        System.setProperty("SECOND", "A")

        assertThat(ReplacementAspect.replace("\${lower:\${FIRST}}")).isEqualTo("1")
        assertThat(ReplacementAspect.replace("\${lower:\${SECOND}}")).isEqualTo("a")
        assertThat(ReplacementAspect.replace("\${lower:\${FIRST}}\${lower:\${SECOND}}")).isEqualTo("1a")
        assertThat(ReplacementAspect.replace("\${lower:\${FIRST}}:\${lower:\${SECOND}}")).isEqualTo("1:a")
        assertThat(ReplacementAspect.replace("|\${lower:\${FIRST}}|:|\${lower:\${SECOND}}|")).isEqualTo("|1|:|a|")
    }

    @Test
    fun replacePropertyTest() {
        System.setProperty("env", "test")
        ConfigurationLoader.load(
            arrayListOf("common", ReplacementAspect.replace("\${env}")),
            Configuration::add,
        )

        assertThat(ReplacementAspect.replace("\${FOUR}")).isEqualTo("4")
        assertThat(ReplacementAspect.replace("\${FIVE}")).isEqualTo("5")
    }

    @Test
    fun replaceMathTest() {
        System.setProperty("FIRST", "1")

        assertThat(ReplacementAspect.replace("\${math:2+2*2/2-2}")).isEqualTo("2.0")
        assertThat(ReplacementAspect.replace("\${math:2+2*2/2-\${FIRST}}")).isEqualTo("3.0")
    }

    @Test
    fun replaceFakerTest() {
        assertThat(
            ReplacementAspect.replace("\${faker:Internet.emailAddress}"),
        ).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        assertThat(
            ReplacementAspect.replace("\${faker:Name.first_name}"),
        ).matches("^[A-Za-z]+\$")
        assertThat(
            ReplacementAspect.replace("\${faker:ru:Name.first_name}"),
        ).matches("^[А-Яа-яЁё]+\$")
    }
}
