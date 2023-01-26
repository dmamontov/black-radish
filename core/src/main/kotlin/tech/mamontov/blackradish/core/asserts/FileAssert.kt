package tech.mamontov.blackradish.core.asserts

import org.apache.commons.io.FileUtils
import org.apache.tika.Tika
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import java.io.File

class FileAssert(val file: File) : AbstractAssert<FileAssert, File>(file, FileAssert::class.java) {
    companion object {
        fun assertThat(file: File): FileAssert {
            return FileAssert(file)
        }
    }

    fun isMimeTypeEquals(mimeType: String): FileAssert {
        Assertions.assertThat(Tika().detect(this.file)).isEqualTo(mimeType)

        return this
    }

    fun isContentEquals(second: File): FileAssert {
        if (!FileUtils.contentEquals(file, second)) {
            failWithMessage("File content is not identical")
        }

        return this
    }
}
