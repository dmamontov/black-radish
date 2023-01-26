package tech.mamontov.blackradish.office.properties

import org.apache.poi.ss.usermodel.Workbook
import org.assertj.core.api.Assertions
import tech.mamontov.blackradish.core.interfaces.Logged
import java.io.File

class ThreadWorkbookProperty : Logged {
    companion object {
        private val workbook: ThreadLocal<Workbook?> = ThreadLocal<Workbook?>()
        private val file: ThreadLocal<File?> = ThreadLocal<File?>()

        fun get(): Workbook {
            Assertions.assertThat(workbook.get()).`as`("File not open or not create").isNotNull

            return workbook.get()!!
        }

        fun file(): File {
            Assertions.assertThat(file.get()).`as`("File not open or not create").isNotNull

            return file.get()!!
        }

        fun set(book: Workbook, fileObj: File) {
            if (workbook.get() !== null) {
                workbook.get()!!.close()
            }

            workbook.set(book)
            file.set(fileObj)
        }
    }
}
