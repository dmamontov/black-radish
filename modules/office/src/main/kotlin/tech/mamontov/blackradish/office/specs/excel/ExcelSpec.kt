package tech.mamontov.blackradish.office.specs.excel

import io.cucumber.datatable.DataTable
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.helpers.EvaluateHelper
import tech.mamontov.blackradish.core.helpers.JsonHelper
import tech.mamontov.blackradish.core.helpers.UriHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.filesystem.specs.FilesystemSpec
import tech.mamontov.blackradish.office.parsers.ExcelParser
import tech.mamontov.blackradish.office.properties.ThreadWorkbookProperty
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

abstract class ExcelSpec : Logged, FilesystemSpec() {

    open fun createEmpty(format: String, path: String) {
        val file = File(UriHelper.uri(path, true).path)

        try {
            val workbook: Workbook = if (format == "xlsx") {
                XSSFWorkbook()
            } else {
                HSSFWorkbook()
            }

            workbook.write(FileOutputStream(file))

            ThreadWorkbookProperty.set(workbook, file)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            this.after(file)
        }
    }

    open fun listAdd(name: String, dataTable: DataTable) {
        val workbook = ThreadWorkbookProperty.get()
        val file = ThreadWorkbookProperty.file()

        try {
            val sheet = workbook.createSheet(name)

            val style = workbook.createCellStyle()
            style.wrapText = true

            dataTable.asLists().forEachIndexed { rowNumber: Int, row: List<String> ->
                val sheetRow = sheet.createRow(rowNumber)

                row.forEachIndexed { columnNumber: Int, column: String ->
                    val cell = sheetRow.createCell(columnNumber)
                    when (val value = this.evaluate(column)) {
                        is Number -> cell.setCellValue(value.toDouble())
                        is Boolean -> cell.setCellValue(value)
                        else -> cell.setCellValue(value as String)
                    }
                    cell.cellStyle = style
                }
            }

            workbook.write(FileOutputStream(file))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            this.after(file)
            this.createResult(workbook, file)
        }
    }

    open fun cellsMerge(name: String, from: String, to: String) {
        val workbook = ThreadWorkbookProperty.get()
        val file = ThreadWorkbookProperty.file()

        try {
            val sheet = workbook.getSheet(name)

            Assertions.assertThat(sheet).`as`("List not found").isNotNull

            sheet.addMergedRegion(CellRangeAddress.valueOf("$from:$to"))

            workbook.write(FileOutputStream(file))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            this.after(file)
            this.createResult(workbook, file)
        }
    }

    open fun cellWrite(name: String, value: String, cell: String, formula: Boolean) {
        val workbook = ThreadWorkbookProperty.get()
        val file = ThreadWorkbookProperty.file()

        try {
            val sheet = workbook.getSheet(name)

            Assertions.assertThat(sheet).`as`("List not found").isNotNull

            val style = workbook.createCellStyle()
            style.wrapText = true

            val reference = CellReference(cell)

            val row = sheet.getRow(reference.row) ?: sheet.createRow(reference.row)
            val rowCell = row.getCell(reference.col.toInt()) ?: row.createCell(reference.col.toInt())

            if (formula) {
                rowCell.cellFormula = value
            } else {
                when (val eval = this.evaluate(value)) {
                    is Number -> rowCell.setCellValue(eval.toDouble())
                    is Boolean -> rowCell.setCellValue(eval)
                    else -> rowCell.setCellValue(eval as String)
                }
            }

            rowCell.cellStyle = style

            workbook.write(FileOutputStream(file))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            this.after(file)
            this.createResult(workbook, file)
        }
    }

    open fun read(format: String, path: String) {
        val absolutePath = UriHelper.uri(path, true).path
        val file = File(absolutePath)

        Assertions.assertThat(file).isFile

        if (format == "xlsx") {
            FileAssert.assertThat(file).isMimeTypeEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        } else {
            FileAssert.assertThat(file).isMimeTypeEquals("application/vnd.ms-excel")
        }

        try {
            val input = FileInputStream(file)

            val workbook: Workbook = if (format == "xlsx") {
                XSSFWorkbook(input)
            } else {
                HSSFWorkbook(input)
            }

            this.createResult(workbook, file)

            ThreadWorkbookProperty.set(workbook, file)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    private fun evaluate(value: String): Any? {
        val match = Regex("^(.*?)\\{:(\\w+)}$").find(value) ?: return value

        return EvaluateHelper.eval(match.groupValues[1], match.groupValues[2])
    }

    private fun createResult(workbook: Workbook, file: File) {
        val result: MutableList<List<Map<String, Any>>> = mutableListOf()

        val sheets = workbook.iterator()
        while (sheets.hasNext()) {
            val sheet: MutableList<Map<String, Any>> = mutableListOf()
            val rows = sheets.next().iterator()

            while (rows.hasNext()) {
                val row: MutableMap<String, Any> = mutableMapOf()
                val cells = rows.next().iterator()

                while (cells.hasNext()) {
                    val cell = cells.next()
                    val alphabet = CellReference.convertNumToColString(cell.columnIndex)

                    row[alphabet] = when (cell.cellType) {
                        CellType.STRING -> cell.richStringCellValue.string
                        CellType.BOOLEAN -> cell.booleanCellValue
                        CellType.NUMERIC -> cell.numericCellValue
                        else -> ""
                    }
                }

                sheet.add(row)
            }

            result.add(sheet)
        }

        this.afterParse(result, file)
    }

    private fun afterParse(result: MutableList<List<Map<String, Any>>>, file: File) {
        val contentResult = Result(JsonHelper.toJson(result), ExcelParser(result))

        this.after(contentResult, file.path)
    }
}
