package tech.mamontov.blackradish.filesystem.specs

import io.cucumber.datatable.DataTable
import io.cucumber.docstring.DocString
import io.cucumber.java.ru.Затем
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.Тогда
import tech.mamontov.blackradish.core.annotations.Glue
import tech.mamontov.blackradish.core.interfaces.Logged
import tech.mamontov.blackradish.core.specs.base.BaseRuSpec
import tech.mamontov.blackradish.filesystem.enumerated.FileType

@Glue
@Suppress("UNUSED_PARAMETER")
class FilesystemRuSpec : Logged, FilesystemSpec() {
    private val toType: Map<String, FileType> = hashMapOf(
        "директорию" to FileType.DIR,
        "файл" to FileType.FILE,
    )

    @Когда("^[Я|я] создаю директорию '(.*?)'$")
    override fun mkdir(path: String) {
        super.mkdir(path)
    }

    @Когда("^[Я|я] создаю пустой файл '(.*?)'$")
    override fun touch(path: String) {
        super.touch(path)
    }

    @Когда("^[Я|я] создаю файл '(.*?)' содержащий:$")
    override fun create(path: String, content: DocString) {
        super.create(path, content)
    }

    @Затем("^[Я|я] удаляю (директорию|файл) '(.*?)'$")
    fun delete(type: String, path: String) {
        toType[type]?.let {
            super.delete(path, it)
        }
    }

    @Тогда("^([Д|д]иректория|[Ф|ф]айл) '(.*?)'( не)? существует$")
    fun exists(type: String, path: String, not: String?) {
        super.exists(path, not !== null && not.isNotEmpty())
    }

    @Тогда("^[Я|я] копирую (директорию|файл) '(.*?)' в '(.*?)'$")
    fun copy(type: String, source: String, target: String) {
        toType[type]?.let {
            super.copy(source, target, it)
        }
    }

    @Тогда("^[Я|я] перемещаю (директорию|файл) '(.*?)' в '(.*?)'$")
    fun move(type: String, source: String, target: String) {
        toType[type]?.let {
            super.move(source, target, it)
        }
    }

    @Тогда("^[Я|я] перемещаю (директорию|файл) '(.*?)' в директорию '(.*?)'$")
    fun moveTo(type: String, source: String, target: String) {
        toType[type]?.let {
            super.moveTo(source, target, it)
        }
    }

    @Когда("^[Я|я] получаю структуру директории '(.*?)'( рекурсивно)?$")
    fun list(path: String, recursively: String?) {
        super.tree(path, recursively !== null)
    }

    @Тогда("^[Д|д]иректория пуста$")
    override fun directoryIsEmpty() {
        super.directoryIsEmpty()
    }

    @Когда("^[Я|я] открываю файл '(.*?)'$")
    fun read(path: String) {
        super.read(path, null)
    }

    @Когда("^[Я|я] разбираю файл '(.*?)' используя шаблон '(.*?)'$")
    override fun parse(path: String, templatePath: String) {
        super.parse(path, templatePath)
    }

    @Тогда("^[С|с]одержимое файла( не)? соответствует:$")
    fun contentIs(not: String?, content: DocString) {
        super.contentIs(content, not !== null)
    }

    @Тогда("^[С|с]одержимое файла:$")
    fun contentMatch(dataTable: DataTable) {
        super.contentMatch(dataTable, BaseRuSpec.toComparisonOperation)
    }

    @Тогда("^[Ф|ф]айл содержит( не менее)? '(\\d+)' строк[у|и]?$")
    fun lines(min: String?, count: Int) {
        super.linesCount(min !== null, count)
    }

    @Тогда("^[Д|д]иректория( не)? содержит:$")
    fun dirContains(not: String?, dataTable: DataTable) {
        super.directoryContains(dataTable, not !== null)
    }

    @Тогда("^[С|с]одержимое файлов '(.*?)' и '(.*?)' идентично$")
    override fun contentEquals(first: String, second: String) {
        super.contentEquals(first, second)
    }
}
