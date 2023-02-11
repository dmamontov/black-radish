package tech.mamontov.blackradish.dbf.specs

import io.cucumber.core.stepexpression.StepTypeRegistry
import io.cucumber.datatable.DataTable
import io.cucumber.datatable.DataTableTypeRegistryTableConverter
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.csvdb.specs.CsvDbSpec
import java.util.Locale

/**
 * Dbf database spec
 *
 * @author Dmitry Mamontov
 *
 * @property csvDbSpec CsvDbSpec
 */
open class DbfSpec : Logged {
    private val csvDbSpec = CsvDbSpec()

    /**
     * Connecting to the csv database via jdbc.
     *
     * @param path String
     * @param dataTable DataTable?
     */
    open fun connect(path: String, dataTable: DataTable?) {
        val extension = listOf("fileExtension", ".dbf")

        StepTypeRegistry(Locale.getDefault())

        val properties = if (dataTable === null) {
            DataTable.create(
                listOf(extension),
                DataTableTypeRegistryTableConverter(
                    StepTypeRegistry(Locale.getDefault()).dataTableTypeRegistry(),
                ),
            )
        } else {
            DataTable.create(dataTable.asLists() + listOf(extension), dataTable.tableConverter)
        }

        csvDbSpec.connect(path, properties)
    }
}
