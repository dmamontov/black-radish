package tech.mamontov.blackradish.csv.specs

import io.cucumber.datatable.DataTable
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.apache.commons.text.StringEscapeUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.csv.parsers.CsvParser
import tech.mamontov.blackradish.csv.properties.ThreadBuilderProperty
import tech.mamontov.blackradish.filesystem.properties.ThreadFileContentProperty
import tech.mamontov.blackradish.filesystem.specs.FilesystemSpec
import java.io.File
import java.io.FileReader
import java.io.Reader

abstract class CsvSpec : Logged, FilesystemSpec() {

    private val availableProperties: List<String> = listOf(
        "charset",
        "columnTypes",
        "commentChar",
        "cryptoFilterClassName",
        "cryptoFilterParameterTypes",
        "cryptoFilterParameters",
        "defectiveHeaders",
        "fileExtension",
        "fileTailParts",
        "fileTailPattern",
        "fileTailPrepend",
        "fixedWidths",
        "headerline",
        "ignoreNonParseableLines",
        "indexedFiles",
        "isHeaderFixedWidth",
        "missingValue",
        "quotechar",
        "quoteStyle",
        "locale",
        "maxDataLines",
        "separator",
        "skipLeadingLines",
        "skipLeadingDataLines",
        "suppressHeaders",
        "timestampFormat",
        "timeFormat",
        "dateFormat",
        "timeZoneName",
        "trimHeaders",
        "trimValues",
        "useDateTimeFormatter",
    )

    open fun delimiter(delimiter: String) {
        try {
            ThreadBuilderProperty.get().setDelimiter(StringEscapeUtils.unescapeJava(delimiter))
        } catch (e: IllegalArgumentException) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun separator(separator: String) {
        Assertions.assertThat(separator).isIn("\\n", "\\r\\n", "\\r")
        try {
            ThreadBuilderProperty.get().setRecordSeparator(StringEscapeUtils.unescapeJava(separator))
        } catch (e: IllegalArgumentException) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun create(path: String, content: DataTable) {
        val out = StringBuilder()

        try {
            CSVPrinter(out, ThreadBuilderProperty.get().build()).use { printer: CSVPrinter ->
                content.asLists().forEach { row: List<String> ->
                    printer.printRecord(*row.toTypedArray())
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(out).`as`("Csv file cannot be empty").isNotEmpty

        this.create(path, out.toString())
    }

    open fun read(path: String, firstLine: Boolean = false, headers: List<String>? = null) {
        val absolutePath = this.exists(path, false)!!.path

        val records: MutableList<Any> = mutableListOf()
        val list: MutableList<List<Any>> = mutableListOf()

        try {
            val file = File(absolutePath)
            Assertions.assertThat(file).isFile
            FileAssert.assertThat(file).isMimeTypeEquals("text/csv")

            val reader: Reader = FileReader(absolutePath)

            val builder: CSVFormat.Builder = ThreadBuilderProperty.reload()

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
                    records.add(record.toMap())
                } else {
                    records.add(record.toList())
                }
            }

            reader.close()
            csvParser.close()
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }

        Assertions.assertThat(records).`as`("Failed to open csv file or it is empty").isNotEmpty

        super.read(absolutePath, CsvParser(list, records))
    }

    open fun readWithoutFirstLine(path: String) {
        this.read(path, true, null)
    }

    open fun readWithHeaders(path: String, dataTable: DataTable) {
        val headers = dataTable.asLists()

        Assertions.assertThat(headers).`as`("Headers can only be one line").hasSize(1)

        this.read(path, false, headers.first())
    }

    open fun contentIs(content: DataTable, not: Boolean) {
        Assertions.assertThat(
            ThreadFileContentProperty.get().parser,
        ).`as`("There is no open csv file.")
            .isInstanceOf(CsvParser::class.java)

        val parser = ThreadFileContentProperty.get().parser as CsvParser

        if (not) {
            Assertions.assertThat(parser.list).isNotEqualTo(content.asLists())
        } else {
            Assertions.assertThat(parser.list).isEqualTo(content.asLists())
        }
    }

    open fun connect(path: String, dataTable: DataTable? = null) {
        var jdbc = "jdbc:relique:csv:"

        try {
            val file = File(this.exists(path, false)!!.path)
            if (file.isDirectory) {
                Assertions.assertThat(file).isNotEmptyDirectory
            } else {
                FileAssert.assertThat(file).isMimeTypeEquals("application/zip")
                jdbc += "zip:"
            }

            super.connect(jdbc + file.path, dataTable, this.availableProperties)
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message, e)
        }
    }
}
