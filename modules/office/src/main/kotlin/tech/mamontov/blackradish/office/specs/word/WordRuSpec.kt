package tech.mamontov.blackradish.office.specs.word

import io.cucumber.docstring.DocString
import io.cucumber.java.ru.Когда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class WordRuSpec : Logged, WordSpec() {
    @Когда("^[Я|я] создаю docx файл '(.*?\\.docx)' содержащий:$")
    override fun createDoc(path: String, content: DocString) {
        super.createDoc(path, content)
    }

    @Когда("^[Я|я] создаю docx файл '(.*?\\.docx)' на основе '(.*?)'$")
    override fun createDocBy(path: String, from: String) {
        super.createDocBy(path, from)
    }

    @Когда("^[Я|я] открываю docx файл '(.*?\\.docx)'$")
    fun readDoc(path: String) {
        super.readDoc(path, null)
    }

    @Когда("^[Я|я] разбираю docx файл '(.*?\\.docx)' используя шаблон '(.*?)'$")
    override fun parseDoc(path: String, templatePath: String) {
        super.parseDoc(path, templatePath)
    }
}
