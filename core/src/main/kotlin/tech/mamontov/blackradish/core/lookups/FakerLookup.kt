package tech.mamontov.blackradish.core.lookups

import com.github.javafaker.Faker
import org.apache.commons.text.lookup.StringLookup
import tech.mamontov.blackradish.core.interfaces.Logged
import java.util.Locale

class FakerLookup : Logged, StringLookup {
    override fun lookup(key: String): String? {
        var variable = key

        var faker = Faker()

        val path = variable.split(":").dropLastWhile { it.isEmpty() }.toTypedArray()

        if (path.size >= 2) {
            faker = Faker(Locale.Builder().setLanguageTag(path[0]).build())
            variable = path[1]
        }

        return faker.expression("#{$variable}")
    }
}
