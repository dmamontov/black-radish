package tech.mamontov.blackradish.filesystem.specs

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseEnSpec
import tech.mamontov.blackradish.filesystem.enumerated.FileType

/**
 * Filesystem spec (EN)
 *
 * @author Dmitry Mamontov
 *
 * @property toType Map<String, FileType>
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class FilesystemEnSpec : Logged, FilesystemSpec() {
    private val toType: Map<String, FileType> = hashMapOf(
        "directory" to FileType.DIR,
        "file" to FileType.FILE,
    )

    /**
     * Directory creation.
     *
     * ```
     * Scenario: An example of creating a directory
     *   When i create directory 'artifacts/tmp/example/first/second'
     * ```
     *
     * @param path String
     */
    @When("^[I|i] create directory '(.*?)'$")
    override fun mkdir(path: String) {
        super.mkdir(path)
    }

    /**
     * Create an empty file.
     *
     * ```
     * Scenario: An example of creating an empty file
     *   When i create an empty file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param path String
     */
    @When("^[I|i] create an empty file '(.*?)'$")
    override fun touch(path: String) {
        super.touch(path)
    }

    /**
     * Creation of a file with content.
     *
     * ```
     * Scenario: An example of creating a file with custom content
     *   When i create a file 'artifacts/tmp/hello.txt' containing:
     *     """
     *     HELLO WORLD
     *     """
     * ```
     *
     * @param path String
     * @param content DocString
     */
    @When("^[I|i] create a file '(.*?)' containing:$")
    override fun create(path: String, content: DocString) {
        super.create(path, content)
    }

    /**
     * Deleting a file or directory.
     *
     * ```
     * Scenario: An example of deleting a directory
     *   When i create directory 'artifacts/tmp/example/first/second'
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of deleting a file.
     *   When i create an empty file 'artifacts/tmp/example.txt'
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param type String
     * @param path String
     */
    @Then("^[I|i] delete the (directory|file) '(.*?)'$")
    fun delete(type: String, path: String) {
        toType[type]?.let {
            super.delete(path, it)
        }
    }

    /**
     * Checking if a file or directory exists.
     *
     * ```
     * Scenario: An example of checking the existence of a directory in resources
     *   When i create directory 'artifacts/tmp/example/first/second'
     *   Then directory 'artifacts/tmp/example/first/second' exist
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of checking the existence of a directory relative to the package root
     *   Given directory 'src/test/resources/artifacts/tmp' exist
     * ```
     * ```
     * Scenario: An example of checking if a directory does not exist
     *   Given directory 'src/undefined' does not exist
     * ```
     * ```
     * Scenario: An example of checking the existence of a file in resources
     *   When i create an empty file 'artifacts/tmp/example.txt'
     *   Then file 'artifacts/tmp/example.txt' exist
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     * ```
     * Scenario: An example of checking the existence of a file relative to the package root
     *   Given file 'src/test/resources/artifacts/tmp/.gitkeep' exist
     * ```
     * ```
     * Scenario: An example of checking the non-existence of a file
     *   Given file 'src/undefined.txt' does not exist
     * ```
     *
     * @param type String
     * @param path String
     * @param isNot String?
     */
    @Then("^([D|d]irectory|[F|f]ile) '(.*?)'( does not)? exist$")
    fun exists(type: String, path: String, isNot: String?) {
        super.exists(path, isNot !== null && isNot.isNotEmpty())
    }

    /**
     * Copying a file or directory.
     *
     * ```
     * Scenario: An example of copying a directory
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   When i copy directory 'artifacts/tmp/example/first/second' to 'artifacts/tmp/example/first/copy'
     *   Then directory 'artifacts/tmp/example/first/copy' exist
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of copying a file
     *   Given i create directory 'artifacts/tmp/tree'
     *   Given i create an empty file 'artifacts/tmp/example.txt'
     *   When i copy file 'artifacts/tmp/example.txt' to 'artifacts/tmp/tree/example.txt'
     *   Then file 'artifacts/tmp/tree/example.txt' exist
     *   Then i delete the directory 'artifacts/tmp/tree'
     *   And i delete the file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Then("^[I|i] copy (directory|file) '(.*?)' to '(.*?)'$")
    fun copy(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.copy(sourcePath, targetPath, it)
        }
    }

    /**
     * Moving a file or directory.
     *
     * ```
     * Scenario: An example of moving a directory
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   When i move directory 'artifacts/tmp/example/first/second' to 'artifacts/tmp/example/first/move'
     *   Then directory 'artifacts/tmp/example/first/second' does not exist
     *   And directory 'artifacts/tmp/example/first/move' exist
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of moving a file
     *   Given i create directory 'artifacts/tmp/tree'
     *   Given i create an empty file 'artifacts/tmp/example.txt'
     *   When i move file 'artifacts/tmp/example.txt' to 'artifacts/tmp/tree/example.txt'
     *   Then file 'artifacts/tmp/example.txt' does not exist
     *   And file 'artifacts/tmp/tree/example.txt' exist
     *   Then i delete the directory 'artifacts/tmp/tree'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Then("^[I|i] move (directory|file) '(.*?)' to '(.*?)'$")
    fun move(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.move(sourcePath, targetPath, it)
        }
    }

    /**
     * Move a file or directory to a directory.
     *
     * ```
     * Scenario: An example of moving a directory to another directory
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/move'
     *   When i move directory 'artifacts/tmp/example/first/second' to directory 'artifacts/tmp/example/first/move'
     *   Then directory 'artifacts/tmp/example/first/second' does not exist
     *   And directory 'artifacts/tmp/example/first/move/second' exist
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario:An example of moving a file to a directory
     *   Given i create directory 'artifacts/tmp/tree'
     *   Given i create an empty file 'artifacts/tmp/example.txt'
     *   When i move file 'artifacts/tmp/example.txt' to directory 'artifacts/tmp/tree'
     *   Then file 'artifacts/tmp/example.txt' does not exist
     *   And file 'artifacts/tmp/tree/example.txt' exist
     *   Then i delete the directory 'artifacts/tmp/tree'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Then("^[I|i] move (directory|file) '(.*?)' to directory '(.*?)'$")
    fun moveTo(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.moveTo(sourcePath, targetPath, it)
        }
    }

    /**
     * Getting a directory tree.
     *
     * ```
     * Scenario: An example of getting and comparing a list of files and directories
     *   Given i create an empty file 'artifacts/tmp/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp'
     *   Then directory contain:
     *     | .gitkeep           |
     *     | example_first.txt   |
     *     | example_second.txt |
     *   Then i delete the file 'artifacts/tmp/example_first.txt'
     *   And i delete the file 'artifacts/tmp/example_second.txt'
     * ```
     * ```
     * Scenario: An example of getting and comparing a list of files and directories recursively
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then directory contain:
     *     | example/first/second/.black-radish    |
     *     | example/first/second/example_first.txt |
     *     | example/first/third/.black-radish     |
     *     | example/first/second                  |
     *     | example/first/third                   |
     *     | example/first                         |
     *     | example                              |
     *   And directory does not contain:
     *     | undefined |
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of checking the number of files and directories in a directory
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then result contains '9' records
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of checking the number of files and directories in a directory for the minimum number
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then result contains at least '2' records
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of checking the contents of a directory by json path
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then as a result:
     *     | $.[0]  | is       | example/first/third/example_second.txt |
     *     | $.[1]  | matches  | ^.*\/\.black-radish$                  |
     *     | $.[2]  | contains | example_first                          |
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of saving the contents of a directory to a variable as json
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then i save the result in a variable 'JSON'
     *   Then '${JSON}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-tree.json}'
     *   And i delete the directory 'artifacts/tmp/example'
     * ```
     * ```
     * Scenario: An example of saving the contents of a directory by json path to a variable
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then i save the result '$.[4]' in a variable 'PATH'
     *   Then '${PATH}' is '.gitkeep'
     *   And i delete the directory 'artifacts/tmp/example'
     * ```
     *
     * @param path String
     * @param recursively String?
     */
    @When("^[I|i] get a directory tree '(.*?)'( recursively)?$")
    fun tree(path: String, recursively: String?) {
        super.tree(path, recursively !== null)
    }

    /**
     * Opening a file.
     *
     * ```
     * Scenario: An example of reading from a file
     *   When i create a file 'artifacts/tmp/example.txt' containing:
     *     """
     *     Example
     *     """
     *   Then i open file 'artifacts/tmp/example.txt'
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param path String
     */
    @When("^[I|i] open file '(.*?)'$")
    fun `open`(path: String) {
        super.`open`(path, null)
    }

    /**
     * Checking the contents of a file for consistency line by line.
     *
     * ```
     * Scenario: An example of reading from a file and comparing the contents line by line
     *   When i create a file 'artifacts/tmp/example.txt' containing:
     *     """
     *     100
     *     example
     *     """
     *   Then i open file 'artifacts/tmp/example.txt'
     *   Then file contents:
     *     | 1 | is                | 100        |
     *     | 2 | is                | example    |
     *     | 1 | matches           | ^\d+       |
     *     | 2 | matches           | ^[A-Za-z]+ |
     *     | 1 | contains          | 10         |
     *     | 2 | contains          | ex         |
     *     | 1 | is different from | 10         |
     *     | 2 | is different from | ex         |
     *     | 1 | is higher than    | 10         |
     *     | 1 | is lower than     | 101        |
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param dataTable DataTable
     */
    @Then("^[F|f]ile contents:$")
    fun contentLineByLineMatch(dataTable: DataTable) {
        super.contentLineByLineMatch(dataTable, BaseEnSpec.toComparisonOperation)
    }

    /**
     * Checking for the number of lines in a file.
     *
     * ```
     * Scenario: An example of reading from a file and comparing the number of lines for equality
     *   When i create a file 'artifacts/tmp/example.txt' containing:
     *     """
     *     HELLO WORLD
     *     EXAMPLE
     *     """
     *   Then i open file 'artifacts/tmp/example.txt'
     *   And file contains '2' lines
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     * ```
     * Scenario: An example of reading from a file and comparing the number of lines to the minimum number
     *   When i create a file 'artifacts/tmp/example.txt' containing:
     *     """
     *     HELLO WORLD
     *     EXAMPLE
     *     """
     *   Then i open file 'artifacts/tmp/example.txt'
     *   And file contains at least '1' lines
     *   Then i delete the file 'artifacts/tmp/example.txt'
     * ```
     *
     * @param atLeast String?
     * @param size Int
     */
    @Then("^[F|f]ile contains( at least)? '(\\d+)' line[s]?$")
    fun lineIs(atLeast: String?, size: Int) {
        super.linesCount(atLeast !== null, size)
    }

    /**
     * Checking a directory for the contents of a file or directory.
     *
     * ```
     * Scenario: An example of getting and comparing a list of files and directories
     *   Given i create an empty file 'artifacts/tmp/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp'
     *   Then directory contain:
     *     | .gitkeep           |
     *     | example_first.txt   |
     *     | example_second.txt |
     *   Then i delete the file 'artifacts/tmp/example_first.txt'
     *   And i delete the file 'artifacts/tmp/example_second.txt'
     * ```
     * ```
     * Scenario: An example of getting and comparing a list of files and directories recursively
     *   Given i create directory 'artifacts/tmp/example/first/second'
     *   Given i create directory 'artifacts/tmp/example/first/third'
     *   Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
     *   When i get a directory tree 'artifacts/tmp' recursively
     *   Then directory contain:
     *     | example/first/second/.black-radish    |
     *     | example/first/second/example_first.txt |
     *     | example/first/third/.black-radish     |
     *     | example/first/second                  |
     *     | example/first/third                   |
     *     | example/first                         |
     *     | example                              |
     *   And directory does not contain:
     *     | undefined |
     *   Then i delete the directory 'artifacts/tmp/example'
     * ```
     *
     * @param isNot String?
     * @param dataTable DataTable
     */
    @Then("^[D|d]irectory( does not)? contain:$")
    fun directoryContains(isNot: String?, dataTable: DataTable) {
        super.directoryContains(dataTable, isNot !== null)
    }

    /**
     * Checking if the contents of two files match.
     *
     * ```
     * Scenario: An example of comparing the contents of files
     *   Given i create an empty file 'artifacts/tmp/example_first.txt'
     *   Given i create an empty file 'artifacts/tmp/example_second.txt'
     *   Then contents of files 'artifacts/tmp/example_first.txt' and 'artifacts/tmp/example_second.txt' are identical
     *   Then i delete the file 'artifacts/tmp/example_first.txt'
     *   And i delete the file 'artifacts/tmp/example_second.txt'
     * ```
     *
     * @param firstFilePath String
     * @param secondFilePath String
     */
    @Then("^[C|c]ontents of files '(.*?)' and '(.*?)' are identical$")
    override fun contentEquals(firstFilePath: String, secondFilePath: String) {
        super.contentEquals(firstFilePath, secondFilePath)
    }
}
