package tech.mamontov.blackradish.dbf.specs

import io.cucumber.core.stepexpression.StepTypeRegistry
import io.cucumber.datatable.DataTable
import io.cucumber.datatable.DataTableTypeRegistryTableConverter
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.csv.specs.CsvSpec
import java.util.Locale

abstract class DbfSpec : Logged, CsvSpec() {
    override fun connect(path: String, dataTable: DataTable?) {
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

        super.connect(path, properties)
    }
}
