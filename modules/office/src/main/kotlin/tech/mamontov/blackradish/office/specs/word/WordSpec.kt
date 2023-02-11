package tech.mamontov.blackradish.office.specs.word

import io.cucumber.docstring.DocString
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.asserts.FileAssert
import tech.mamontov.blackradish.core.data.Result
import tech.mamontov.blackradish.core.helpers.UriHelper
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.parsers.Parser
import tech.mamontov.blackradish.core.parsers.TemplateParser
import tech.mamontov.blackradish.filesystem.properties.ThreadFileContentProperty
import tech.mamontov.blackradish.filesystem.specs.FilesystemSpec
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

abstract class WordSpec : Logged, FilesystemSpec() {
    open fun createDoc(path: String, content: DocString) {
        this.createDocByContent(path, content.content)
    }

    open fun createDocBy(path: String, from: String) {
        this.read(from)
        this.createDocByContent(path, ThreadFileContentProperty.get().content)
    }

    open fun readDoc(path: String, parser: Parser? = null) {
        val absolutePath = UriHelper.uri(path, true).path
        val file = File(absolutePath)
        var content = ""

        Assertions.assertThat(file).isFile
        FileAssert
            .assertThat(file)
            .isMimeTypeEquals(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            )

        try {
            val document = XWPFDocument(FileInputStream(file))

            val paragraphs = document.paragraphsIterator
            while (paragraphs.hasNext()) {
                content += paragraphs.next()
                    .text
                    .replace("\r\n", System.lineSeparator()) + System.lineSeparator()
            }

            document.close()
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            Assertions.assertThat(content.trimEnd()).`as`("Empty content").isNotEmpty

            val result = if (parser !== null) {
                Result(content.trimEnd(), parser)
            } else {
                Result(content.trimEnd())
            }

            this.after(result, absolutePath)
        }
    }

    open fun parseDoc(path: String, templatePath: String) {
        try {
            this.readDoc(path, TemplateParser(this.exists(templatePath, false)!!))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        }
    }

    private fun createDocByContent(path: String, content: String) {
        val file = File(UriHelper.uri(path, true).path)

        try {
            val document = XWPFDocument()
            document.createParagraph().createRun().setText(content)
            document.write(FileOutputStream(file))
        } catch (e: Exception) {
            Assertions.fail<Any>(e.message, e)
        } finally {
            ThreadFileContentProperty.set(Result(content))

            this.after(file)
        }
    }
}
