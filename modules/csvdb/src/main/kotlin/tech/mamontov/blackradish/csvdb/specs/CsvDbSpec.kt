package tech.mamontov.blackradish.csvdb.specs

import io.cucumber.datatable.DataTable
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.database.DatabaseSpec

/**
 * Csv database spec
 *
 * @author Dmitry Mamontov
 *
 * @property databaseSpec DatabaseSpec
 */
open class CsvDbSpec : Logged {
    private val databaseSpec = DatabaseSpec()

    /**
     * Connecting to the csv database via jdbc.
     *
     * @param path String
     * @param dataTable DataTable?
     */
    open fun connect(path: String, dataTable: DataTable?) {
        var connection = "jdbc:relique:csv:"
        val file = Filesystem.that(path).safe().asFile

        if (file.isDirectory) {
            Assertions.assertThat(file).isNotEmptyDirectory
        } else {
            FileAssert.assertThat(file).isZip
            connection += "zip:"
        }

        databaseSpec.load("org.relique.jdbc.csv.CsvDriver")
        databaseSpec.connect(connection + file.path, dataTable)
    }
}
