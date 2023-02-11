package tech.mamontov.blackradish.csv.storages

import org.apache.commons.csv.CSVFormat
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.reflecation.Reflecation

/**
 * Csv builder storage
 *
 * @author Dmitry Mamontov
 */
class CsvBuilderStorage : Logged {
    companion object {
        private val builder: ThreadLocal<CSVFormat.Builder> = object : ThreadLocal<CSVFormat.Builder>() {
            override fun initialValue(): CSVFormat.Builder {
                return CSVFormat.DEFAULT.builder()
            }
        }

        /**
         * Get csv builder
         *
         * @return CSVFormat.Builder
         */
        fun get(): CSVFormat.Builder {
            return builder.get()
        }

        /**
         * Reload csv builder
         *
         * @return CSVFormat.Builder
         */
        fun reload(): CSVFormat.Builder {
            val build = builder.get()
                .setDelimiter(Reflecation.getValue(builder.get(), "delimiter") as String)
                .setRecordSeparator(Reflecation.getValue(builder.get(), "recordSeparator") as String)

            reset()

            return build
        }

        /**
         * Reset csv builder
         */
        fun reset() {
            builder.set(CSVFormat.DEFAULT.builder())
        }
    }
}
