package tech.mamontov.blackradish.core.output

import io.cucumber.messages.types.DataTable
import io.cucumber.messages.types.DocString
import io.cucumber.messages.types.Feature
import io.cucumber.messages.types.Scenario
import io.cucumber.messages.types.Step
import io.cucumber.messages.types.TableCell
import io.cucumber.messages.types.TableRow
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader

/**
 * Sphinx rst writer
 *
 * @author Dmitry Mamontov
 *
 * @constructor
 */
class RstWriter(out: Appendable) : Logged, OutputAppendable(out) {
    companion object {
        const val BUILD_DIRECTORY = "build/sphinx"

        private const val NOTE = "#Note:"
        private const val WARNING = "#Warning:"
        private const val DANGER = "#Danger:"
        private const val FILE = "#File:"

        private const val TYPE_BIN = "bin"
        private const val MAX_LINES = 20

        private const val CODE_BLOCK_INDENT = "   "
        private const val CODE_BLOCK_ARGUMENTS_INDENT = "       "
    }

    /**
     * Write tags
     *
     * @param tags List<String>
     * @return OutputAppendable
     */
    fun writeTags(tags: List<String>): OutputAppendable {
        return this.println(
            ".. tags:: " + tags.joinToString(", ").replace("@", ""),
        ).println()
    }

    /**
     * Write header
     *
     * @param feature Feature
     * @return OutputAppendable
     */
    fun writeHeader(feature: Feature): OutputAppendable {
        writeH(feature.name, "=")

        if (feature.description.isNotEmpty()) {
            feature.description.trim().lineSequence().forEach { line: String ->
                if (line.trim().startsWith("#")) {
                    this.writeComment(line.trim(), feature.name.lowercase().replace(" ", "_"))
                } else {
                    this.println(line.trim())
                }
            }

            println()
        }

        return this
    }

    /**
     * Write tree
     *
     * @return OutputAppendable
     */
    fun writeTree(): OutputAppendable {
        return println(".. include:: ../sections/sub-tree-section.rst").println()
    }

    /**
     * Write comment
     *
     * @param note String
     * @return OutputAppendable
     */
    @Suppress("NAME_SHADOWING")
    fun writeComment(note: String, moduleName: String): OutputAppendable {
        if (note.startsWith(NOTE, true)) {
            return println(".. note:: " + note.replace(NOTE, "", true).trim()).println()
        }

        if (note.startsWith(WARNING, true)) {
            return println(".. warning:: " + note.replace(WARNING, "", true).trim()).println()
        }

        if (note.startsWith(DANGER, true)) {
            return println(".. danger:: " + note.replace(DANGER, "", true).trim()).println()
        }

        if (note.startsWith(FILE, true)) {
            try {
                val fileSplit = note.replace(FILE, "", true).trim().split(": ")

                val path: String
                var type = ""

                if (fileSplit.size == 2) {
                    path = fileSplit.last()
                    type = fileSplit.first()
                } else {
                    path = fileSplit.first()
                }

                val file = Filesystem.current(path).absolute().asFile
                val name = FilenameUtils.getName(file.path)

                var lines: Long = 0
                if (type != TYPE_BIN) {
                    try {
                        LineNumberReader(FileReader(file)).use { lnr ->
                            while (lnr.readLine() != null) {
                                lines = lnr.lineNumber.toLong()

                                if (lines > MAX_LINES) {
                                    break
                                }
                            }
                        }
                    } catch (_: IOException) {
                    }
                }

                if (type == TYPE_BIN || lines > MAX_LINES) {
                    val dirName = FilenameUtils.getName(FilenameUtils.getPath(file.path).trimEnd('/'))
                    val fileName = "$moduleName.$dirName.$name"

                    FileUtils.copyFile(
                        file,
                        Filesystem.current(
                            BUILD_DIRECTORY + File.separator + "attachments" + File.separator + fileName,
                        ).file().asFile,
                    )

                    return println(":download:`$name <attachments/$fileName>`").println()
                }

                println(".. code-block:: $type")
                    .println("$CODE_BLOCK_INDENT:caption: $name")
                    .println()

                file.forEachLine { line: String ->
                    println(CODE_BLOCK_INDENT + line)
                }

                return println()
            } catch (_: Exception) {
            } catch (_: AssertionError) {
            }
        }

        return this
    }

    /**
     * Write code block
     *
     * @param scenario Scenario
     * @return OutputAppendable
     */
    fun writeCode(scenario: Scenario): OutputAppendable {
        println(".. code-block:: gherkin")
            .println("$CODE_BLOCK_INDENT:caption: example.feature")
            .println().println(CODE_BLOCK_INDENT + scenario.keyword.trim() + ": " + scenario.name.trim())

        scenario.steps.forEach { step: Step ->
            println(
                CODE_BLOCK_INDENT + " ".repeat(
                    step.location.column.get().toInt() - 7 + 2,
                ) + step.keyword.trim() + " " + step.text.trim(),
            )
            writeArguments(step)
        }

        return println()
    }

    /**
     * Write h1, h2, h3 and etc
     *
     * @param header String
     * @param separator String
     * @return OutputAppendable
     */
    fun writeH(header: String, separator: String): OutputAppendable {
        return this.println(header).println(separator.repeat(header.length)).println()
    }

    /**
     * Write step arguments
     *
     * @param step Step
     * @return OutputAppendable
     */
    private fun writeArguments(step: Step): OutputAppendable {
        if (!step.dataTable.isEmpty) {
            return writeDataTable(step.dataTable.get())
        }

        if (!step.docString.isEmpty) {
            return writeDocString(step.docString.get())
        }

        return this
    }

    /**
     * Write DataTable argument
     *
     * @param dataTable DataTable
     * @return OutputAppendable
     */
    private fun writeDataTable(dataTable: DataTable): OutputAppendable {
        val maxLength = mutableMapOf<Int, Int>()

        dataTable.rows.forEach { row: TableRow ->
            row.cells.forEachIndexed { index: Int, cell: TableCell ->
                if (!maxLength.containsKey(index) || cell.value.length > maxLength[index]!!) {
                    maxLength[index] = cell.value.length
                }
            }
        }

        dataTable.rows.forEach { row: TableRow ->
            val cells = row.cells.mapIndexed { index: Int, cell: TableCell ->
                cell.value + " ".repeat(maxLength[index]!! - cell.value.length)
            }

            this.println(CODE_BLOCK_ARGUMENTS_INDENT + "| " + cells.joinToString(" | ") + " |")
        }

        return this
    }

    /**
     * Write DocString argument
     *
     * @param docString DocString
     * @return OutputAppendable
     */
    private fun writeDocString(docString: DocString): OutputAppendable {
        return this.println(CODE_BLOCK_ARGUMENTS_INDENT + "\"\"\"")
            .println(docString.content.replace("(?m)^".toRegex(), CODE_BLOCK_ARGUMENTS_INDENT))
            .println(CODE_BLOCK_ARGUMENTS_INDENT + "\"\"\"")
    }
}
