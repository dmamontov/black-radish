package tech.mamontov.blackradish.core.specs.converter

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseEnSpec

/**
 * Converter spec (EN)
 *
 * @author Dmitry Mamontov
 */
@Glue
class ConverterEnSpec : Logged, ConverterSpec() {
    /**
     * Converts the result by template.
     *
     * ```
     * Scenario: Template conversion example
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     * ```
     *
     * @param templatePath String
     */
    @When("^[I|i] convert the result by template '(.*?)'$")
    override fun template(templatePath: String) {
        super.template(templatePath)
    }

    /**
     * Checks if the converted result contains at least `size` records.
     *
     * ```
     * Scenario: An example of checking the number of records in json for equality
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then result contains '2' records
     * ```
     * ```
     * Scenario: An example of checking the number of records in json for the minimum number
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then result contains at least '2' records
     * ```
     * ```
     * Scenario: An example of checking the number of records in yaml for equality
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then result contains '2' records
     * ```
     * ```
     * Scenario: An example of checking the number of records in yaml for the minimum number
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then result contains at least '2' records
     * ```
     * ```
     * Scenario: An example of checking the number of records converted by template for equality
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then result contains '2' records
     * ```
     * ```
     * Scenario: An example of checking the number of records converted by template for the minimum number
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then result contains at least '2' records
     * ```
     *
     * @param atLeast String?
     * @param size Int
     */
    @Then("^[R|r]esult contains( at least)? '(\\d+)' record[s]?$")
    fun size(atLeast: String?, size: Int) {
        super.size(atLeast !== null, size)
    }

    /**
     * Checks if the sum of the elements in the json path is equal to the result of the transformation.
     *
     * ```
     * Scenario: An example of summing values in json by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then sum of '$..remoteAS' results in '131103.0'
     * ```
     * ```
     * Scenario: An example of summing values in yaml by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then sum of '$..remoteAS' results in '131103.0'
     * ```
     * ```
     * Scenario: An example of summing values in xml by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then sum of '$.root.element.*.remoteAS' results in '131103.0'
     * ```
     * ```
     * Scenario: An example of summing values in converted data by template and json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then sum of '$..remoteAS' results in '131103.0'
     * ```
     *
     * @param jsonPath String
     * @param number String
     */
    @Then("^[S|s]um of '(.*?)' results in '(.*?)'$")
    override fun sum(jsonPath: String, number: String) {
        super.sum(jsonPath, number)
    }

    /**
     * Checks the result of converted against conditions.
     *
     * ```
     * Scenario: An example of checking the result of json conversion by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then as a result:
     *     | $.[0].localAS  | is             | 65551 |
     *     | $.[0].remoteAS | is higher than | 65550 |
     *     | $.[1].localAS  | matches        | ^\d+$ |
     *     | $.[1].remoteAS | contains       | 65    |
     * ```
     * ```
     * Scenario: An example of checking the result of the yaml transformation by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then as a result:
     *     | $.[0].localAS  | is             | 65551 |
     *     | $.[0].remoteAS | is higher than | 65550 |
     *     | $.[1].localAS  | matches        | ^\d+$ |
     *     | $.[1].remoteAS | contains       | 65    |
     * ```
     * ```
     * Scenario: An example of checking the result of xml transformation by json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then as a result:
     *     | $.root.element.[0].localAS  | is             | 65551 |
     *     | $.root.element.[0].remoteAS | is higher than | 65550 |
     *     | $.root.element.[1].localAS  | matches        | ^\d+$ |
     *     | $.root.element.[1].remoteAS | contains       | 65    |
     * ```
     * ```
     * Scenario: An example of checking the result of data conversion by template and json path
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then as a result:
     *     | $.[0].localAS  | is             | 65551 |
     *     | $.[0].remoteAS | is higher than | 65550 |
     *     | $.[1].localAS  | matches        | ^\d+$ |
     *     | $.[1].remoteAS | contains       | 65    |
     * ```
     *
     * @param dataTable DataTable
     */
    @Then("^[A|a]s a result:$")
    fun match(dataTable: DataTable) {
        super.match(dataTable, BaseEnSpec.toComparisonOperation)
    }

    /**
     * Comparison of the original result.
     *
     * ```
     * Scenario: Examples of checking for equality
     *   ...
     *   Then result is 'example'
     * ```
     * ```
     * Scenario: Examples of checking for inequality
     *   ...
     *   Then result is different from 'ex'
     * ```
     * ```
     * Scenario: Examples of checking by regular expression
     *   ...
     *   Then result matches '^[A-Za-z]+'
     * ```
     * ```
     * Scenario: Examples of checking the occurrence of a substring
     *   ...
     *   Then result contains 'ex'
     * ```
     * ```
     * Scenario: Examples of comparing numbers
     *   ...
     *   Then result is lower than '200.2'
     *   And result is higher than '100'
     * ```
     *
     * @param operation String
     * @param value String
     */
    @Тогда("^[R|r]esult (is|matches|is higher than|is lower than|contains|is different from) '(.*?)'$")
    fun comparison(operation: String, value: String) {
        super.comparison(value, BaseEnSpec.toComparisonOperation[operation]!!)
    }

    /**
     * Comparison of the original result.
     *
     * ```
     * Scenario: Examples of checking for equality
     *   ...
     *   Then result is:
     *     """
     *     example
     *     """
     * ```
     * ```
     * Scenario: Examples of checking for inequality
     *   ...
     *   Then result is different from:
     *    """
     *    ex
     *    """
     * ```
     * ```
     * Scenario: Examples of checking by regular expression
     *   ...
     *   Then result matches:
     *     """
     *     ^[A-Za-z]+
     *     """
     * ```
     * ```
     * Scenario: Examples of checking the occurrence of a substring
     *   ...
     *   Then result contains:
     *     """
     *     ex
     *     """
     * ```
     * ```
     * Scenario: Examples of comparing numbers
     *   ...
     *   Then result is lower than:
     *     """
     *     200.2
     *     """
     *   And result is higher than:
     *     """
     *     100
     *     """
     * ```
     *
     * @param operation String
     * @param value DocString
     */
    @Тогда("^[R|r]esult (is|matches|is higher than|is lower than|contains|is different from):$")
    fun comparison(operation: String, value: DocString) {
        super.comparison(value.content.trim(), BaseEnSpec.toComparisonOperation[operation]!!)
    }

    /**
     * Validates content against a schema.
     *
     * ```
     * Scenario: An example of json validation against json schema
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then i check the content against the schema 'artifacts/files/schema-json.json'
     * ```
     * ```
     * Scenario: An example of yaml validation against json schema
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then i check the content against the schema 'artifacts/files/schema-yaml.json'
     * ```
     * ```
     * Scenario: Example of xml validation according to xsd schema
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then i check the content against the schema 'artifacts/files/schema-xml.xsd'
     * ```
     *
     * @param schemaPath String
     */
    @Then("^[I|i] check the content against the schema '(.*?)'$")
    override fun validate(schemaPath: String) {
        super.validate(schemaPath)
    }

    /**
     * Stores the original content in a variable
     *
     * ```
     * Scenario: An example of saving the original json to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then i save original in a variable 'ORIGINAL'
     *   Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'
     * ```
     * ```
     * Scenario: An example of saving the original yaml to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then i save original in a variable 'ORIGINAL'
     *   Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'
     * ```
     * ```
     * Scenario: An example of saving the original xml to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then i save original in a variable 'ORIGINAL'
     *   Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'
     * ```
     * ```
     * Scenario: An example of saving the original converted data by template into a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then i save original in a variable 'ORIGINAL'
     *   And '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'
     * ```
     *
     * @param key String
     */
    @Then("^[I|i] save original in a variable '([A-Za-z0-9_]+)'$")
    fun save(key: String) {
        super.save(null, key, true)
    }

    /**
     * Saves the result of converted along the json path or in its entirety into a variable.
     *
     * ```
     * Scenario: An example of saving the result of json conversion to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then i save the result in a variable 'RESULT'
     *   Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'
     * ```
     * ```
     * Scenario: An example of saving the result of converting json by json path to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
     *   Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
     *   Then '${LOCAL_AS}' is '65551'
     * ```
     * ```
     * Scenario: An example of saving the result of yaml conversion to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then i save the result in a variable 'RESULT'
     *   Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'
     * ```
     * ```
     * Scenario: An example of saving the result of converting yaml by json path to a variable.
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
     *   Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
     *   Then '${LOCAL_AS}' is '65551'
     * ```
     * ```
     * Scenario: An example of saving the result of xml transformation into a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then i save the result in a variable 'RESULT'
     *   Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'
     * ```
     * ```
     * Scenario: An example of saving the result of converting xml by json path to a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
     *   Then i save the result '$.root.element.[0].localAS' in a variable 'LOCAL_AS'
     *   Then '${LOCAL_AS}' is '65551'
     * ```
     * ```
     * Scenario: An example of saving the result of converted data by template into a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then i save the result in a variable 'RESULT'
     *   And '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'
     * ```
     * ```
     * Scenario: An example of saving the result of converted data by template and json path into a variable
     *   When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
     *   Then i convert the result by template 'artifacts/files/template.xml'
     *   Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
     *   And '${LOCAL_AS}' is '65551'
     * ```
     *
     * @param jsonPath String?
     * @param key String
     */
    @Then("^[I|i] save the result( '(.*?)')? in a variable '([A-Za-z0-9_]+)'$")
    fun save(jsonPath: String?, key: String) {
        super.save(jsonPath, key, false)
    }
}
