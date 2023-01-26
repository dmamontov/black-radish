package tech.mamontov.blackradish.office.specs.word

import io.cucumber.docstring.DocString
import io.cucumber.java.en.When
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged

@Glue
class WordEnSpec : Logged, WordSpec() {
    @When("^[I|i] create a docx file '(.*?\\.docx)' containing:$")
    override fun createDoc(path: String, content: DocString) {
        super.createDoc(path, content)
    }

    @When("^[I|i] create a docx file '(.*?\\.docx)' based on '(.*?)'$")
    override fun createDocBy(path: String, from: String) {
        super.createDocBy(path, from)
    }

    @When("^[I|i] open docx file '(.*?\\.docx)'$")
    fun readDoc(path: String) {
        super.readDoc(path, null)
    }

    @When("^[I|i] parse docx file '(.*?\\.docx)' using the template '(.*?)'$")
    override fun parseDoc(path: String, templatePath: String) {
        super.parseDoc(path, templatePath)
    }
}
