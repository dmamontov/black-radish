package tech.mamontov.blackradish.core.specs.converter

import com.jayway.jsonpath.matchers.JsonPathMatchers
import io.cucumber.datatable.DataTable
import org.assertj.core.api.Assertions
import org.hamcrest.MatcherAssert
import tech.mamontov.blackradish.core.asserts.NumberAssert
import tech.mamontov.blackradish.core.converters.TemplateConverter
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.enumerated.ConvertedResultOperation
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.helpers.ExtJsonObject
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage

/**
 * Converter spec
 *
 * @author Dmitry Mamontov
 *
 * @property baseSpec BaseSpec
 */
open class ConverterSpec : Logged {
    private val baseSpec = BaseSpec()

    /**
     * Converts the result by template.
     *
     * @param templatePath String
     */
    open fun template(templatePath: String) {
        ConvertedResultStorage.set(
            ConvertedResult(
                ConvertedResultStorage.asRaw(),
                TemplateConverter(Filesystem.that(templatePath).safe().uri),
            ),
        )
    }

    /**
     * Checks that the converted result contains at least `size` records.
     *
     * @param atLeast Boolean
     * @param size Int
     */
    open fun size(atLeast: Boolean, size: Int) {
        val json = ConvertedResultStorage.asJson()
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter.operations).`as`(
            converter::class.java.simpleName + " not supported operation " + ConvertedResultOperation.SIZE,
        ).contains(ConvertedResultOperation.SIZE)

        Assertions.assertThat(json.isJsonArray).`as`("Must be an array").isTrue

        if (atLeast) {
            Assertions.assertThat(json.asJsonArray.size()).isGreaterThanOrEqualTo(size)
        } else {
            Assertions.assertThat(json.asJsonArray.size()).isEqualTo(size)
        }
    }

    /**
     * Checks if the sum of elements along the json path as a result of converted is equal to `number`.
     *
     * @param jsonPath String
     * @param number String
     */
    open fun sum(jsonPath: String, number: String) {
        val json = ConvertedResultStorage.asJson()
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter.operations).`as`(
            converter::class.java.simpleName + " not supported operation " + ConvertedResultOperation.SUM,
        ).contains(ConvertedResultOperation.SUM)

        MatcherAssert.assertThat(AnyObject.that(json).asJsonString, JsonPathMatchers.hasJsonPath(jsonPath))
        NumberAssert.assertThat(ExtJsonObject.that(json).sum(jsonPath)).isEqualTo(number)
    }

    /**
     * Checks the result of converted against conditions.
     *
     * @param dataTable DataTable
     * @param toComparisonOperation Map<String, ComparisonOperation>
     */
    open fun match(dataTable: DataTable, toComparisonOperation: Map<String, ComparisonOperation>) {
        val json = ConvertedResultStorage.asJson()
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter.operations).`as`(
            converter::class.java.simpleName + " not supported operation " + ConvertedResultOperation.MATCH,
        ).contains(ConvertedResultOperation.MATCH)

        dataTable.asLists().forEach { row: List<String> ->
            Assertions.assertThat(row)
                .`as`("Should be 3 columns.")
                .hasSize(3)

            MatcherAssert.assertThat(AnyObject.that(json).asJsonString, JsonPathMatchers.hasJsonPath(row[0]))
            Assertions.assertThat(toComparisonOperation).containsKey(row[1])

            this.baseSpec.comparison(
                ExtJsonObject.that(json).findString(row[0]),
                toComparisonOperation[row[1]]!!,
                row[2],
            )
        }
    }

    /**
     * Comparison of the original result.
     *
     * @param value String
     * @param operation ComparisonOperation
     */
    open fun comparison(value: String, operation: ComparisonOperation) {
        this.baseSpec.comparison(
            ConvertedResultStorage.asRaw().replace("\r\n", System.lineSeparator()).trimEnd('\n', '\r'),
            operation,
            value,
        )
    }

    /**
     * Validates content against a schema.
     *
     * @param schemaPath String
     */
    open fun validate(schemaPath: String) {
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter.operations).`as`(
            converter::class.java.simpleName + " not supported operation " + ConvertedResultOperation.VALIDATE,
        ).contains(ConvertedResultOperation.VALIDATE)

        ConvertedResultStorage.validate(Filesystem.that(schemaPath).safe().asFile)
    }

    /**
     * Saves the result of converted along the json path or in its entirety into a variable.
     *
     * @param jsonPath String?
     * @param key String
     */
    open fun save(jsonPath: String? = null, key: String, isRaw: Boolean) {
        if (isRaw) {
            this.baseSpec.save(ConvertedResultStorage.asRaw(), key)

            return
        }

        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter.operations).`as`(
            converter::class.java.simpleName + " not supported operation " + ConvertedResultOperation.SAVE,
        ).contains(ConvertedResultOperation.SAVE)

        val json = ConvertedResultStorage.asJson()

        if (jsonPath === null) {
            this.baseSpec.save(AnyObject.that(json).asJsonString, key)

            return
        }

        MatcherAssert.assertThat(AnyObject.that(json).asJsonString, JsonPathMatchers.hasJsonPath(jsonPath))

        this.baseSpec.save(ExtJsonObject.that(json).findString(jsonPath), key)
    }
}
