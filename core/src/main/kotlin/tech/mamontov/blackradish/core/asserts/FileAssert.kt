package tech.mamontov.blackradish.core.asserts

import org.apache.commons.io.FileUtils
import org.apache.tika.Tika
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import java.io.File

/**
 * Custom file assertions
 *
 * @see [AssertJ](https://assertj.github.io/doc/)
 *
 * @author Dmitry Mamontov
 *
 * @property file File
 * @property isJson FileAssert
 * @property isXml FileAssert
 * @property isSql FileAssert
 * @property isCsv FileAssert
 * @property isZip FileAssert
 * @constructor
 */
@Suppress("KDocUnresolvedReference")
class FileAssert(val file: File) : AbstractAssert<FileAssert, File>(file, FileAssert::class.java) {
    /**
     * @property MIME_TYPE_JSON String
     * @property MIME_TYPE_XML String
     * @property MIME_TYPE_SQL String
     * @property MIME_TYPE_CSV String
     * @property MIME_TYPE_ZIP String
     */
    companion object {
        const val MIME_TYPE_JSON = "application/json"
        const val MIME_TYPE_XML = "application/xml"
        const val MIME_TYPE_SQL = "text/x-sql"
        const val MIME_TYPE_CSV = "text/csv"
        const val MIME_TYPE_ZIP = "application/zip"

        /**
         * Static constructor
         *
         * @param file File
         * @return FileAssert
         */
        fun assertThat(file: File): FileAssert {
            return FileAssert(file)
        }
    }

    val isJson
        get() = this.isMimeTypeEquals(MIME_TYPE_JSON)

    val isXml
        get() = this.isMimeTypeEquals(MIME_TYPE_XML)

    val isSql
        get() = this.isMimeTypeEquals(MIME_TYPE_SQL)

    val isCsv
        get() = this.isMimeTypeEquals(MIME_TYPE_CSV)

    val isZip
        get() = this.isMimeTypeEquals(MIME_TYPE_ZIP)

    /**
     * Assert is file mime type equals
     *
     * @param mimeType String
     * @return FileAssert
     */
    fun isMimeTypeEquals(mimeType: String): FileAssert {
        Assertions.assertThat(Tika().detect(this.file)).isEqualTo(mimeType)

        return this
    }

    /**
     * Assert is content two files equals
     *
     * @param second File
     * @return FileAssert
     */
    fun isContentEquals(second: File): FileAssert {
        if (!FileUtils.contentEquals(file, second)) {
            failWithMessage("File content is not identical")
        }

        return this
    }
}
