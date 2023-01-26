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
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.enumerated.ComparisonOperation
import tech.mamontov.blackradish.core.helpers.UriHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.parsers.Parser
import tech.mamontov.blackradish.core.parsers.TemplateParser
import tech.mamontov.blackradish.core.properties.ThreadContentProperty
import tech.mamontov.blackradish.core.specs.database.DatabaseSpec
import tech.mamontov.blackradish.filesystem.enumerated.FileType
import tech.mamontov.blackradish.filesystem.properties.ThreadFileContentProperty
import tech.mamontov.blackradish.filesystem.properties.ThreadFilesProperty
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.nio.charset.Charset

abstract class FilesystemSpec : Logged, DatabaseSpec() {
    open fun mkdir(path: String) {
        val uri: URI = this.newPath(path)

        try {
            FileUtils.forceMkdir(File(uri.path))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        this.touch(uri.path + File.separator + ".black-radish")
    }

    open fun touch(path: String) {
        val absolutePath = UriHelper.uri(path, true).path

        try {
            FileUtils.touch(File(absolutePath))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun create(path: String, content: DocString) {
        this.create(path, content.content)
    }

    open fun delete(path: String, type: FileType) {
        try {
            when (type) {
                FileType.DIR -> {
                    val file = File(UriHelper.path(path).path)
                    Assertions.assertThat(file).isDirectory

                    FileUtils.deleteDirectory(file)
                }

                FileType.FILE -> {
                    val file = File(UriHelper.uri(path).path)
                    Assertions.assertThat(file).isFile

                    FileUtils.delete(file)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun exists(path: String, not: Boolean): URI? {
        var uri: URI? = null

        try {
            uri = try {
                UriHelper.uri(path)
            } catch (_: AssertionError) {
                UriHelper.current(path)
            }

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

        return uri
    }

    open fun copy(source: String, target: String, type: FileType) {
        try {
            val sourceFile = File(this.exists(source, false)!!.path)

            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile).isDirectory

                    FileUtils.copyDirectory(sourceFile, File(this.newPath(target).path))
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile).isFile

                    FileUtils.copyFile(sourceFile, File(UriHelper.uri(target, true).path))
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun move(source: String, target: String, type: FileType) {
        try {
            val sourceFile = File(this.exists(source, false)!!.path)
            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile).isDirectory

                    FileUtils.moveDirectory(sourceFile, File(this.newPath(target).path))
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile).isFile

                    FileUtils.moveFile(sourceFile, File(UriHelper.uri(target, true).path))
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun moveTo(source: String, target: String, type: FileType) {
        try {
            val sourceFile = File(this.exists(source, false)!!.path)
            val targetFile = File(this.newPath(target).path)

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

    open fun tree(path: String, recursively: Boolean) {
        val uri: URI = this.exists(path, false)!!
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

        val files: MutableList<String> = mutableListOf()
        collection!!.filter { it.toString() != uri.path }.forEach {
            files.add(it.toString().substringAfter(uri.path).trimStart(File.separatorChar))
        }

        ThreadFilesProperty.set(files.toList())
    }

    open fun directoryIsEmpty() {
        Assertions.assertThat(ThreadFilesProperty.get())
            .`as`("Directory is not empty")
            .isEmpty()
    }

    open fun directoryContains(dataTable: DataTable, not: Boolean) {
        Assertions.assertThat(dataTable.columns(0).asList())
            .`as`("Content cannot be empty")
            .isNotEmpty

        if (not) {
            val list: List<String> = dataTable.columns(0).asList()
            Assertions.assertThat(ThreadFilesProperty.get()).doesNotContain(*list.toTypedArray())
        } else {
            Assertions.assertThat(ThreadFilesProperty.get()).containsAll(dataTable.columns(0).asList())
        }
    }

    open fun read(path: String, parser: Parser? = null) {
        val absolutePath = this.exists(path, false)!!.path
        var content = ""

        try {
            val file = File(absolutePath)
            Assertions.assertThat(file).isFile

            content = this.read(file)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(content).`as`("Empty content").isNotEmpty

        val result = if (parser !== null) {
            Result(content, parser)
        } else {
            Result(content)
        }

        this.after(result, absolutePath)
    }

    open fun parse(path: String, templatePath: String) {
        try {
            this.read(path, TemplateParser(this.exists(templatePath, false)!!))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun contentIs(content: DocString, not: Boolean) {
        val replaced = content.content.replace("\r\n", System.lineSeparator())

        if (not) {
            Assertions.assertThat(
                ThreadFileContentProperty.raw().trimEnd('\n', '\r'),
            ).isNotEqualTo(replaced)
        } else {
            Assertions.assertThat(
                ThreadFileContentProperty.raw().trimEnd('\n', '\r'),
            ).isEqualTo(replaced)
        }
    }

    open fun contentMatch(dataTable: DataTable, toComparisonOperation: Map<String, ComparisonOperation>) {
        val lines = ThreadFileContentProperty.raw().split(System.lineSeparator())
        dataTable.asLists().forEach { row: List<String> ->
            Assertions.assertThat(row)
                .`as`("Should be 3 columns.")
                .hasSize(3)

            NumberAssert.assertThat(row[0]).isNumber()

            val line = NumberUtils.createNumber(row[0]).toInt()

            Assertions.assertThat(line)
                .`as`("The line number '%s' not found.", line)
                .isBetween(1, lines.size)

            Assertions.assertThat(toComparisonOperation).containsKey(row[1])

            this.comparison(lines[line - 1], toComparisonOperation[row[1]]!!, row[2])
        }
    }

    open fun linesCount(min: Boolean, count: Int) {
        val assert = Assertions.assertThat(ThreadFileContentProperty.raw().split(System.lineSeparator()).size)
        if (min) {
            assert.isGreaterThanOrEqualTo(count)
        } else {
            assert.isEqualTo(count)
        }
    }

    open fun contentEquals(first: String, second: String) {
        try {
            val fileFirst = File(this.exists(first, false)!!.path)
            Assertions.assertThat(fileFirst).isFile

            val fileSecond = File(this.exists(second, false)!!.path)
            Assertions.assertThat(fileSecond).isFile

            FileAssert.assertThat(fileFirst).isContentEquals(fileSecond)
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    protected open fun create(path: String, content: String) {
        val absolutePath = UriHelper.uri(path, true).path

        try {
            FileUtils.write(
                File(absolutePath),
                content,
                Charset.defaultCharset(),
                false,
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        this.after(content, absolutePath)
    }

    private fun read(file: File): String {
        val content = FileUtils.readFileToString(file, Charset.defaultCharset().displayName())
            .replace("\r\n", System.lineSeparator())

        Assertions.assertThat(content).`as`("File %s is empty", file.path).isNotNull

        return content
    }

    private fun newPath(path: String): URI {
        var checkPath = path
        var uri: URI? = null

        while (checkPath.isNotEmpty()) {
            try {
                uri = UriHelper.path(checkPath)
                break
            } catch (_: AssertionError) {
                checkPath = FilenameUtils.getFullPath(checkPath.trimEnd(File.separatorChar))
            }
        }

        if (uri === null || uri.path == File.separator) {
            uri = UriHelper.current()
        }

        val uriPath = uri.path.trimEnd(File.separatorChar) + File.separator + path.replaceFirst(checkPath, "")
            .trimStart(File.separatorChar)

        return UriHelper.create(uriPath.trimEnd(File.separatorChar))
    }

    protected fun after(content: String, absolutePath: String) {
        Allure.addAttachment(FilenameUtils.getName(absolutePath), content)
    }

    protected fun after(result: Result, absolutePath: String) {
        ThreadFileContentProperty.set(result)
        Allure.addAttachment(FilenameUtils.getName(absolutePath), result.content)

        if (result.json !== null) {
            ThreadContentProperty.set(result)
            Allure.addAttachment(FilenameUtils.getName(absolutePath) + ".json", result.json.toString())
        }
    }

    protected fun after(file: File) {
        Assertions.assertThat(file).exists()
        Allure.addAttachment(FilenameUtils.getName(file.path), FileInputStream(file))
    }
}
