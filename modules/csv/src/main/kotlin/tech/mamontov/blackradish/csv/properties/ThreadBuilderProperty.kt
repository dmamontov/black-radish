package tech.mamontov.blackradish.csv.properties

import org.apache.commons.csv.CSVFormat
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.utils.reflecation.Reflecation

class ThreadBuilderProperty : Logged {
    companion object {
        private val builder: ThreadLocal<CSVFormat.Builder> = object : ThreadLocal<CSVFormat.Builder>() {
            override fun initialValue(): CSVFormat.Builder {
                return CSVFormat.DEFAULT.builder()
            }
        }

        fun get(): CSVFormat.Builder {
            return builder.get()
        }

        fun reload(): CSVFormat.Builder {
            val build = builder.get()
                .setDelimiter(Reflecation.get(builder.get(), "delimiter") as String)
                .setRecordSeparator(Reflecation.get(builder.get(), "recordSeparator") as String)

            this.reset()

            return build
        }

        fun reset() {
            builder.set(CSVFormat.DEFAULT.builder())
        }
    }
}
