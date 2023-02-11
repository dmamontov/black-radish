package tech.mamontov.blackradish.filesystem.specs

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.qameta.allure.Allure
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.TrueFileFilter
import org.apache.commons.lang3.math.NumberUtils
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.asserts.NumberAssert
import tech.mamontov.blackradish.core.data.ConvertedResult
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.helpers.AnyObject
import tech.mamontov.blackradish.core.helpers.Filesystem
import tech.mamontov.blackradish.core.interfaces.Converter
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseSpec
import tech.mamontov.blackradish.core.storages.ConvertedResultStorage
import tech.mamontov.blackradish.filesystem.converters.DirectoryTreeConverter
import tech.mamontov.blackradish.filesystem.enumerated.FileType
import java.io.File
import java.net.URI
import java.nio.charset.Charset

/**
 * Filesystem spec
 *
 * @author Dmitry Mamontov
 *
 * @property baseSpec BaseSpec
 */
open class FilesystemSpec : Logged {
    private val baseSpec = BaseSpec()

    /**
     * Directory creation.
     *
     * @param path String
     */
    open fun mkdir(path: String) {
        val filesystem = Filesystem.that(path).new()

        try {
            FileUtils.forceMkdir(filesystem.asFile)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        this.touch(filesystem.uri.path + File.separator + ".black-radish")
    }

    /**
     * Create an empty file.
     *
     * @param path String
     */
    open fun touch(path: String) {
        try {
            FileUtils.touch(File(Filesystem.that(path).file().path))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Creation of a file with content.
     *
     * @param path String
     * @param content DocString
     */
    open fun create(path: String, content: DocString) {
        val absolutePath = Filesystem.that(path).file().path

        try {
            FileUtils.write(
                File(absolutePath),
                content.content,
                Charset.defaultCharset(),
                false,
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            Allure.addAttachment(FilenameUtils.getName(absolutePath), content.content)
        }
    }

    /**
     * Deleting a file or directory.
     *
     * @param path String
     * @param type FileType
     */
    open fun delete(path: String, type: FileType) {
        try {
            when (type) {
                FileType.DIR -> {
                    val file = Filesystem.that(path).directory().asFile
                    Assertions.assertThat(file).isDirectory

                    FileUtils.deleteDirectory(file)
                }

                FileType.FILE -> {
                    val file = Filesystem.that(path).absolute().asFile
                    Assertions.assertThat(file).isFile

                    FileUtils.delete(file)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Checking if a file or directory exists.
     *
     * @param path String
     * @param not Boolean
     */
    open fun exists(path: String, not: Boolean) {
        try {
            Filesystem.that(path).safe()

            if (not) {
                Assertions.fail<Any>("$path exist")
            }
        } catch (e: AssertionError) {
            if (!not || e.message.equals("$path exist")) {
                Assertions.fail<Any>(e.message, e)
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Copying a file or directory.
     *
     * @param sourcePath String
     * @param targetPath String
     * @param type FileType
     */
    open fun copy(sourcePath: String, targetPath: String, type: FileType) {
        try {
            val sourceFile = Filesystem.that(sourcePath).safe().asFile

            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile).isDirectory

                    FileUtils.copyDirectory(sourceFile, Filesystem.that(targetPath).new().asFile)
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile).isFile

                    FileUtils.copyFile(sourceFile, Filesystem.that(targetPath).file().asFile)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Moving a file or directory.
     *
     * @param sourcePath String
     * @param targetPath String
     * @param type FileType
     */
    open fun move(sourcePath: String, targetPath: String, type: FileType) {
        val sourceFile = Filesystem.that(sourcePath).safe().asFile

        try {
            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile).isDirectory

                    FileUtils.moveDirectory(sourceFile, Filesystem.that(targetPath).new().asFile)
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile).isFile

                    FileUtils.moveFile(sourceFile, Filesystem.that(targetPath).file().asFile)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Move a file or directory to a directory.
     *
     * @param sourcePath String
     * @param targetPath String
     * @param type FileType
     */
    open fun moveTo(sourcePath: String, targetPath: String, type: FileType) {
        val sourceFile = Filesystem.that(sourcePath).safe().asFile
        val targetFile = Filesystem.that(targetPath).new().asFile

        try {
            Assertions.assertThat(targetFile).isDirectory

            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile).isDirectory
                    FileUtils.moveDirectoryToDirectory(sourceFile, targetFile, true)
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile).isFile
                    FileUtils.moveFileToDirectory(sourceFile, targetFile, true)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    /**
     * Getting a directory tree.
     *
     * @param path String
     * @param recursively Boolean
     */
    open fun tree(path: String, recursively: Boolean) {
        val uri: URI = Filesystem.that(path).safe().uri
        var collection: Collection<File>? = null

        try {
            val file = File(uri.path)

            Assertions.assertThat(file).isDirectory

            collection = FileUtils.listFilesAndDirs(
                file,
                TrueFileFilter.TRUE,
                if (recursively) TrueFileFilter.TRUE else null,
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        val files: List<String> = collection!!.filter { it.toString() != uri.path }.toList().map {
            it.toString().substringAfter(uri.path).trimStart(File.separatorChar)
        }

        val json = AnyObject.that(files.toList()).asJsonString
        ConvertedResultStorage.set(ConvertedResult(json, DirectoryTreeConverter(files.toList())))
    }

    /**
     * Checking a directory for the contents of a file or directory.
     *
     * @param dataTable DataTable
     * @param isNot Boolean
     */
    open fun directoryContains(dataTable: DataTable, isNot: Boolean) {
        val converter = ConvertedResultStorage.converter()

        Assertions.assertThat(converter)
            .`as`("The last result is not the result of getting the directory tree.")
            .isInstanceOf(DirectoryTreeConverter::class.java)

        Assertions.assertThat(dataTable.columns(0).asList())
            .`as`("Content cannot be empty")
            .isNotEmpty

        val tree = (converter as DirectoryTreeConverter).tree

        if (isNot) {
            val list: List<String> = dataTable.columns(0).asList()
            Assertions.assertThat(tree).doesNotContain(*list.toTypedArray())
        } else {
            Assertions.assertThat(tree).containsAll(dataTable.columns(0).asList())
        }
    }

    /**
     * Opening a file.
     *
     * @param path String
     * @param converter Converter?
     */
    open fun `open`(path: String, converter: Converter? = null) {
        val absolutePath = Filesystem.that(path).safe().path
        var content = ""

        try {
            val file = File(absolutePath)
            Assertions.assertThat(file).isFile

            content = FileUtils.readFileToString(file, Charset.defaultCharset().displayName())
                .replace("\r\n", System.lineSeparator())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        val convertedResult = if (converter !== null) {
            ConvertedResult(content, converter)
        } else {
            ConvertedResult(content)
        }

        ConvertedResultStorage.set(convertedResult)

        Allure.addAttachment(FilenameUtils.getName(absolutePath), content)

        if (convertedResult.json !== null) {
            Allure.addAttachment(FilenameUtils.getName(absolutePath) + ".json", convertedResult.json.toString())
        }
    }

    /**
     * Checking the contents of a file for consistency line by line.
     *
     * @param dataTable DataTable
     * @param toComparisonOperation Map<String, ComparisonOperation>
     */
    open fun contentLineByLineMatch(dataTable: DataTable, toComparisonOperation: Map<String, ComparisonOperation>) {
        val lines = ConvertedResultStorage.asRaw().split(System.lineSeparator())
        dataTable.asLists().forEach { row: List<String> ->
            Assertions.assertThat(row)
                .`as`("Should be 3 columns.")
                .hasSize(3)

            NumberAssert.assertThat(row[0]).isNumber

            val line = NumberUtils.createNumber(row[0]).toInt()

            Assertions.assertThat(line)
                .`as`("The line number '%s' not found.", line)
                .isBetween(1, lines.size)

            Assertions.assertThat(toComparisonOperation).containsKey(row[1])

            baseSpec.comparison(lines[line - 1], toComparisonOperation[row[1]]!!, row[2])
        }
    }

    /**
     * Checking for the number of lines in a file.
     *
     * @param atLeast Boolean
     * @param size Int
     */
    open fun linesCount(atLeast: Boolean, size: Int) {
        val assert = Assertions.assertThat(ConvertedResultStorage.asRaw().split(System.lineSeparator()).size)
        if (atLeast) {
            assert.isGreaterThanOrEqualTo(size)
        } else {
            assert.isEqualTo(size)
        }
    }

    /**
     * Checking if the contents of two files match.
     *
     * @param firstFilePath String
     * @param secondFilePath String
     */
    open fun contentEquals(firstFilePath: String, secondFilePath: String) {
        val fileFirst = Filesystem.that(firstFilePath).safe().asFile
        Assertions.assertThat(fileFirst).isFile

        val fileSecond = Filesystem.that(secondFilePath).safe().asFile
        Assertions.assertThat(fileSecond).isFile

        FileAssert.assertThat(fileFirst).isContentEquals(fileSecond)
    }
}
