package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import io.qameta.allure.Allure
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.apache.commons.io.FilenameUtils
import org.apache.commons.text.StringEscapeUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.csv.parsers.CsvConverter
import tech.mamontov.blackradish.csv.storages.CsvBuilderStorage
import java.io.File
import java.io.FileReader
import java.io.Reader
import java.nio.charset.Charset

/**
 * Csv spec
 *
 * @author Dmitry Mamontov
 */
open class CsvSpec : Logged {
    /**
     * Sets a delimiter.
     *
     * @param delimiter String
     */
    open fun delimiter(delimiter: String) {
        try {
            CsvBuilderStorage.get().setDelimiter(StringEscapeUtils.unescapeJava(delimiter))
        } catch (e: IllegalArgumentException) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Sets the line separator.
     *
     * @param separator String
     */
    open fun lineSeparator(separator: String) {
        Assertions.assertThat(separator).isIn("\\n", "\\r\\n", "\\r")
        try {
            CsvBuilderStorage.get().setRecordSeparator(StringEscapeUtils.unescapeJava(separator))
        } catch (e: IllegalArgumentException) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Creates a file with content.
     *
     * @param path String
     * @param dataTable DataTable
     */
    open fun create(path: String, dataTable: DataTable) {
        val file = Filesystem.that(path).file().asFile

        try {
            val writer = file.bufferedWriter(Charset.defaultCharset())

            CSVPrinter(writer, CsvBuilderStorage.get().build()).use { printer: CSVPrinter ->
                dataTable.asLists().forEach { row: List<String> ->
                    printer.printRecord(*row.toTypedArray())
                }
                printer.flush()
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            Allure.addAttachment(FilenameUtils.getName(path), file.inputStream())
        }
    }

    /**
     * Opens a file.
     *
     * @param path String
     * @param firstLine Boolean
     * @param headers List<String>?
     */
    open fun `open`(path: String, firstLine: Boolean = false, headers: List<String>? = null) {
        val absolutePath = Filesystem.that(path).file().path

        val records: MutableList<Any> = mutableListOf()
        val list: MutableList<List<Any>> = mutableListOf()

        val file = File(absolutePath)
        Assertions.assertThat(file).isFile
        FileAssert.assertThat(file).isCsv

        try {
            val reader: Reader = FileReader(absolutePath)
            val builder: CSVFormat.Builder = CsvBuilderStorage.reload()

            if (firstLine) {
                builder
                    .setHeader()
                    .setSkipHeaderRecord(true)
            } else if (headers !== null) {
                builder
                    .setHeader(*headers.toTypedArray())
                    .setSkipHeaderRecord(false)
            }

            val csvParser = builder.build().parse(reader)

            csvParser.toList().map { record: CSVRecord ->
                list.add(record.toList())

                if (csvParser.headerMap !== null && csvParser.headerMap.isNotEmpty()) {
                    records.add(
                        record.toMap().mapValues {
                            AnyObject.that(it.value).parse()
                        },
                    )
                } else {
                    records.add(
                        record.toList().map {
                            AnyObject.that(it).parse()
                        },
                    )
                }
            }

            reader.close()
            csvParser.close()
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        } finally {
            val json = AnyObject.that(records).asJsonString
            val result = ConvertedResult(json, CsvConverter(list))

            ConvertedResultStorage.set(result)

            Allure.addAttachment(FilenameUtils.getName(absolutePath), file.inputStream())
            Allure.addAttachment(FilenameUtils.getName(absolutePath) + ".json", json)
        }
    }

    /**
     * Opens the file, specifying that the first line contains the headers.
     *
     * @param path String
     */
    open fun openWithoutFirstLine(path: String) {
        this.`open`(path, true, null)
    }

    /**
     * Opens a file with headers.
     *
     * @param path String
     * @param dataTable DataTable
     */
    open fun openWithHeaders(path: String, dataTable: DataTable) {
        val headers = dataTable.asLists()

        Assertions.assertThat(headers).`as`("Headers can only be one line").hasSize(1)

        this.`open`(path, false, headers.first())
    }

    /**
     * Compares content.
     *
     * @param dataTable DataTable
     * @param not Boolean
     */
    open fun contentIs(dataTable: DataTable, not: Boolean) {
        Assertions.assertThat(
            ConvertedResultStorage.get().converter,
        ).`as`("There is no open csv file.")
            .isInstanceOf(CsvConverter::class.java)

        val parser = ConvertedResultStorage.get().converter as CsvConverter

        if (not) {
            Assertions.assertThat(parser.list).isNotEqualTo(dataTable.asLists())
        } else {
            Assertions.assertThat(parser.list).isEqualTo(dataTable.asLists())
        }
    }
}
