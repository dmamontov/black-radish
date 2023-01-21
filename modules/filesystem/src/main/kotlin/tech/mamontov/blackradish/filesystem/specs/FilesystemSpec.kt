package tech.mamontov.blackradish.filesystem.specs

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.qameta.allure.Allure
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.LineIterator
import org.apache.commons.io.filefilter.TrueFileFilter
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.specs.BaseSpec
import tech.mamontov.blackradish.core.utils.Logged
import tech.mamontov.blackradish.core.utils.UriHelper
import tech.mamontov.blackradish.filesystem.enumerated.FileType
import tech.mamontov.blackradish.filesystem.properties.ThreadFileContentProperty
import tech.mamontov.blackradish.filesystem.properties.ThreadFilesProperty
import java.io.File
import java.net.URI
import java.nio.charset.Charset

abstract class FilesystemSpec : Logged, BaseSpec() {
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
        val absolutePath = UriHelper.uri(path, true).path
        val data = content.content

        try {
            FileUtils.write(
                File(absolutePath),
                data,
                Charset.defaultCharset(),
                false,
            )
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Allure.addAttachment(FilenameUtils.getName(absolutePath), data)
    }

    open fun delete(path: String, type: FileType) {
        try {
            when (type) {
                FileType.DIR -> {
                    val file = File(UriHelper.path(path).path)
                    Assertions.assertThat(file.isDirectory).isTrue

                    FileUtils.deleteDirectory(file)
                }

                FileType.FILE -> {
                    val file = File(UriHelper.uri(path).path)
                    Assertions.assertThat(file.isFile).isTrue

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
                    Assertions.assertThat(sourceFile.isDirectory).isTrue

                    FileUtils.copyDirectory(sourceFile, File(this.newPath(target).path))
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile.isFile).isTrue

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
                    Assertions.assertThat(sourceFile.isDirectory).isTrue

                    FileUtils.moveDirectory(sourceFile, File(this.newPath(target).path))
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile.isFile).isTrue

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

            Assertions.assertThat(targetFile.isDirectory).isTrue

            when (type) {
                FileType.DIR -> {
                    Assertions.assertThat(sourceFile.isDirectory).isTrue
                    FileUtils.moveDirectoryToDirectory(sourceFile, targetFile, true)
                }

                FileType.FILE -> {
                    Assertions.assertThat(sourceFile.isFile).isTrue
                    FileUtils.moveFileToDirectory(sourceFile, targetFile, true)
                }
            }
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    open fun list(path: String, recursively: Boolean) {
        val uri: URI = this.exists(path, false)!!
        var collection: Collection<File>? = null

        try {
            val file = File(uri.path)

            Assertions.assertThat(file.isDirectory).isTrue

            collection = FileUtils.listFilesAndDirs(
                file,
                TrueFileFilter.TRUE,
                if (recursively) TrueFileFilter.TRUE else null
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

    open fun dirIsEmpty() {
        Assertions.assertThat(ThreadFilesProperty.get()).isEmpty()
    }

    open fun dirContains(dataTable: DataTable, not: Boolean) {
        Assertions.assertThat(dataTable.columns(0).asList()).isNotEmpty

        val isSub = CollectionUtils.isSubCollection(dataTable.columns(0).asList(), ThreadFilesProperty.get())

        if (not) {
            Assertions.assertThat(isSub).isFalse
        } else {
            Assertions.assertThat(isSub).isTrue
        }
    }

    open fun read(path: String) {
        val absolutePath = this.exists(path, false)!!.path
        var content: String? = null

        try {
            val file = File(absolutePath)
            Assertions.assertThat(file.isFile).isTrue

            content = FileUtils.readFileToString(file, Charset.defaultCharset().displayName())
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }

        Assertions.assertThat(content).isNotNull

        ThreadFileContentProperty.set(content!!)

        Allure.addAttachment(FilenameUtils.getName(absolutePath), content)
    }

    open fun contentIs(content: DocString, not: Boolean) {
        Assertions.assertThat(ThreadFileContentProperty.get()).isNotNull

        if (not) {
            Assertions.assertThat(ThreadFileContentProperty.get()!!).isNotEqualTo(content.content)
        } else {
            Assertions.assertThat(ThreadFileContentProperty.get()!!).isEqualTo(content.content)
        }
    }

    open fun equals(first: String, second: String) {
        try {
            val fileFirst = File(this.exists(first, false)!!.path)
            Assertions.assertThat(fileFirst.isFile).isTrue

            val fileSecond = File(this.exists(second, false)!!.path)
            Assertions.assertThat(fileSecond.isFile).isTrue

            Assertions.assertThat(
                FileUtils.contentEquals(
                    fileFirst,
                    fileSecond,
                ),
            ).isTrue
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
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
}
