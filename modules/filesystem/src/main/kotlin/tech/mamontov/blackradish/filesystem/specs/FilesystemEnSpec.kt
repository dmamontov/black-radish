package tech.mamontov.blackradish.filesystem.specs

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseEnSpec
import tech.mamontov.blackradish.filesystem.enumerated.FileType

@Glue
@Suppress("UNUSED_PARAMETER")
class FilesystemEnSpec : Logged, FilesystemSpec() {
    private val toType: Map<String, FileType> = hashMapOf(
        "directory" to FileType.DIR,
        "file" to FileType.FILE,
    )

    @When("^[I|i] create directory '(.*?)'$")
    override fun mkdir(path: String) {
        super.mkdir(path)
    }

    @When("^[I|i] create an empty file '(.*?)'$")
    override fun touch(path: String) {
        super.touch(path)
    }

    @When("^[I|i] create a file '(.*?)' containing:$")
    override fun create(path: String, content: DocString) {
        super.create(path, content)
    }

    @Then("^[I|i] delete the (directory|file) '(.*?)'$")
    fun delete(type: String, path: String) {
        toType[type]?.let {
            super.delete(path, it)
        }
    }

    @Then("^([D|d]irectory|[F|f]ile) '(.*?)'( does not)? exist$")
    fun exists(type: String, path: String, not: String?) {
        super.exists(path, not !== null && not.isNotEmpty())
    }

    @Then("^[I|i] copy (directory|file) '(.*?)' to '(.*?)'$")
    fun copy(type: String, source: String, target: String) {
        toType[type]?.let {
            super.copy(source, target, it)
        }
    }

    @Then("^[I|i] move (directory|file) '(.*?)' to '(.*?)'$")
    fun move(type: String, source: String, target: String) {
        toType[type]?.let {
            super.move(source, target, it)
        }
    }

    @Then("^[I|i] move (directory|file) '(.*?)' to directory '(.*?)'$")
    fun moveTo(type: String, source: String, target: String) {
        toType[type]?.let {
            super.moveTo(source, target, it)
        }
    }

    @When("^[I|i] get the directory structure '(.*?)'( recursively)?$")
    fun list(path: String, recursively: String?) {
        super.tree(path, recursively !== null)
    }

    @Then("^[D|d]irectory is empty$")
    override fun directoryIsEmpty() {
        super.directoryIsEmpty()
    }

    @When("^[I|i] open file '(.*?)'$")
    fun read(path: String) {
        super.read(path, null)
    }

    @When("^[I|i] parse the file '(.*?)' using the template '(.*?)'$")
    override fun parse(path: String, templatePath: String) {
        super.parse(path, templatePath)
    }

    @Then("^[F|f]ile content( does not)? match:$")
    fun contentIs(not: String?, content: DocString) {
        super.contentIs(content, not !== null)
    }

    @Then("^[F|f]ile contents:$")
    fun contentMatch(dataTable: DataTable) {
        super.contentMatch(dataTable, BaseEnSpec.toComparisonOperation)
    }

    @Then("^[F|f]ile contains( at least)? '(\\d+)' line[s]?$")
    fun lineIs(min: String?, count: Int) {
        super.linesCount(min !== null, count)
    }

    @Then("^[D|d]irectory( does not)? contain:$")
    fun dirContains(not: String?, dataTable: DataTable) {
        super.directoryContains(dataTable, not !== null)
    }

    @Then("^[C|c]ontents of files '(.*?)' and '(.*?)' are identical$")
    override fun contentEquals(first: String, second: String) {
        super.contentEquals(first, second)
    }
}
