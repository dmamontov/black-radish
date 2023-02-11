package tech.mamontov.blackradish.core.lookups

import com.github.javafaker.Faker
import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Locale

/**
 * Fake data lookup for replacement
 *
 * @author Dmitry Mamontov
 */
class FakerLookup : Logged, StringLookup {
    /**
     * Generate fake data by expression
     *
     * @param expression String
     * @return String?
     */
    override fun lookup(expression: String): String? {
        var variable = expression

        var faker = Faker()

        val path = variable.split(":").dropLastWhile { it.isEmpty() }.toTypedArray()

        if (path.size >= 2) {
            faker = Faker(Locale.Builder().setLanguageTag(path[0]).build())
            variable = path[1]
        }

        return faker.expression("#{$variable}")
    }
}
