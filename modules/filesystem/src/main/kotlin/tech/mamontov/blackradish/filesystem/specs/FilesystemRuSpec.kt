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

/**
 * Filesystem spec (RU)
 *
 * @author Dmitry Mamontov
 *
 * @property toType Map<String, FileType>
 */
@Glue
@Suppress("UNUSED_PARAMETER")
class FilesystemRuSpec : Logged, FilesystemSpec() {
    private val toType: Map<String, FileType> = hashMapOf(
        "директорию" to FileType.DIR,
        "файл" to FileType.FILE,
    )

    /**
     * Создание директории.
     *
     * ```
     * Сценарий: Пример создания директории
     *   Когда я создаю директорию 'artifacts/tmp/example/first/second'
     * ```
     *
     * @param path String
     */
    @Когда("^[Я|я] создаю директорию '(.*?)'$")
    override fun mkdir(path: String) {
        super.mkdir(path)
    }

    /**
     * Создание пустого файла.
     *
     * ```
     * Сценарий: Пример создания пустого файла
     *   Когда я создаю пустой файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param path String
     */
    @Когда("^[Я|я] создаю пустой файл '(.*?)'$")
    override fun touch(path: String) {
        super.touch(path)
    }

    /**
     * Создание файла с содержимым.
     *
     * ```
     * Сценарий: Пример создания файла c произвольным содержимым
     *   Когда я создаю файл 'artifacts/tmp/hello.txt' содержащий:
     *     """
     *     HELLO WORLD
     *     """
     * ```
     *
     * @param path String
     * @param content DocString
     */
    @Когда("^[Я|я] создаю файл '(.*?)' содержащий:$")
    override fun create(path: String, content: DocString) {
        super.create(path, content)
    }

    /**
     * Удаление файла или директории.
     *
     * ```
     * Сценарий: Пример удаления директории
     *   Когда я создаю директорию 'artifacts/tmp/example/first/second'
     *   Тогда я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример удаления файла
     *   Когда я создаю пустой файл 'artifacts/tmp/example.txt'
     *   Тогда я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param type String
     * @param path String
     */
    @Затем("^[Я|я] удаляю (директорию|файл) '(.*?)'$")
    fun delete(type: String, path: String) {
        toType[type]?.let {
            super.delete(path, it)
        }
    }

    /**
     * Проверка файла или директории на существование.
     *
     * ```
     * Сценарий: Пример проверки существования директории в ресурсах
     *   Когда я создаю директорию 'artifacts/tmp/example/first/second'
     *   Тогда директория 'artifacts/tmp/example/first/second' существует
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример проверки существования директории относительно корня пакета
     *   Допустим директория 'src/test/resourcePaths/artifacts/tmp' существует
     * ```
     * ```
     * Сценарий: Пример проверки не существования директории
     *   Допустим директория 'src/undefined' не существует
     * ```
     * ```
     * Сценарий: Пример проверки существования файла в ресурсах
     *   Когда я создаю пустой файл 'artifacts/tmp/example.txt'
     *   Тогда файл 'artifacts/tmp/example.txt' существует
     *   Затем я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     * ```
     * Сценарий: Пример проверки существования файла относительно корня пакета
     *   Допустим файл 'src/test/resourcePaths/artifacts/tmp/.gitkeep' существует
     * ```
     * ```
     * Сценарий: Пример проверки не существования файла
     *   Допустим файл 'src/undefined.txt' не существует
     * ```
     *
     * @param type String
     * @param path String
     * @param not String?
     */
    @Тогда("^([Д|д]иректория|[Ф|ф]айл) '(.*?)'( не)? существует$")
    fun exists(type: String, path: String, not: String?) {
        super.exists(path, not !== null && not.isNotEmpty())
    }

    /**
     * Копирование файла или директории.
     *
     * ```
     * Сценарий: Пример копирования директории
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Когда я копирую директорию 'artifacts/tmp/example/first/second' в 'artifacts/tmp/example/first/copy'
     *   Тогда директория 'artifacts/tmp/example/first/copy' существует
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример копирования файла
     *   Пусть я создаю директорию 'artifacts/tmp/tree'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
     *   Когда я копирую файл 'artifacts/tmp/example.txt' в 'artifacts/tmp/tree/example.txt'
     *   Тогда файл 'artifacts/tmp/tree/example.txt' существует
     *   Затем я удаляю директорию 'artifacts/tmp/tree'
     *   И я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Тогда("^[Я|я] копирую (директорию|файл) '(.*?)' в '(.*?)'$")
    fun copy(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.copy(sourcePath, targetPath, it)
        }
    }

    /**
     * Перемещение файла или директории.
     *
     * ```
     * Сценарий: Пример перемещения директории
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Когда я перемещаю директорию 'artifacts/tmp/example/first/second' в 'artifacts/tmp/example/first/move'
     *   Тогда директория 'artifacts/tmp/example/first/second' не существует
     *   И директория 'artifacts/tmp/example/first/move' существует
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример перемещения файла
     *   Пусть я создаю директорию 'artifacts/tmp/tree'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
     *   Когда я перемещаю файл 'artifacts/tmp/example.txt' в 'artifacts/tmp/tree/example.txt'
     *   Тогда файл 'artifacts/tmp/example.txt' не существует
     *   И файл 'artifacts/tmp/tree/example.txt' существует
     *   Затем я удаляю директорию 'artifacts/tmp/tree'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Тогда("^[Я|я] перемещаю (директорию|файл) '(.*?)' в '(.*?)'$")
    fun move(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.move(sourcePath, targetPath, it)
        }
    }

    /**
     * Перемещение файла или директории в директорию.
     *
     * ```
     * Сценарий: Пример перемещения директории в другую директорию
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/move'
     *   Когда я перемещаю директорию 'artifacts/tmp/example/first/second' в директорию 'artifacts/tmp/example/first/move'
     *   Тогда директория 'artifacts/tmp/example/first/second' не существует
     *   И директория 'artifacts/tmp/example/first/move/second' существует
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример перемещения файла в директорию
     *   Пусть я создаю директорию 'artifacts/tmp/tree'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
     *   Когда я перемещаю файл 'artifacts/tmp/example.txt' в директорию 'artifacts/tmp/tree'
     *   Тогда файл 'artifacts/tmp/example.txt' не существует
     *   И файл 'artifacts/tmp/tree/example.txt' существует
     *   Затем я удаляю директорию 'artifacts/tmp/tree'
     * ```
     *
     * @param type String
     * @param sourcePath String
     * @param targetPath String
     */
    @Тогда("^[Я|я] перемещаю (директорию|файл) '(.*?)' в директорию '(.*?)'$")
    fun moveTo(type: String, sourcePath: String, targetPath: String) {
        toType[type]?.let {
            super.moveTo(sourcePath, targetPath, it)
        }
    }

    /**
     * Получение содержимого директории.
     *
     * ```
     * Сценарий: Пример получения и сравнения списка файлов и директорий
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp'
     *   Тогда директория содержит:
     *     | .gitkeep           |
     *     | example_first.txt   |
     *     | example_second.txt |
     *   Затем я удаляю файл 'artifacts/tmp/example_first.txt'
     *   И я удаляю файл 'artifacts/tmp/example_second.txt'
     * ```
     * ```
     * Сценарий: Пример получения и сравнения списка файлов и директорий рекурсивно
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда директория содержит:
     *     | example/first/second/.black-radish    |
     *     | example/first/second/example_first.txt |
     *     | example/first/third/.black-radish     |
     *     | example/first/second                  |
     *     | example/first/third                   |
     *     | example/first                         |
     *     | example                              |
     *   И директория не содержит:
     *     | undefined |
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример проверки количества файлов и директорий в директории
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда результат содержит '9' записей
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример проверки количества файлов и директорий в директории на минимальное количество
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда результат содержит не менее '2' записей
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример проверки содержимого директории по json path
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда в результате:
     *     | $.[0]  | равно         | example/first/third/example_second.txt |
     *     | $.[1]  | соответствует | ^.*\/\.black-radish$                  |
     *     | $.[2]  | содержит      | example_first                          |
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример сохранения содержимого директории в переменную как json
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда я сохраняю результат в переменной 'JSON'
     *   Затем '${JSON}' равно '${file:UTF-8:src/test/resourcePaths/artifacts/files/converted-tree.json}'
     *   И я удаляю директорию 'artifacts/tmp/example'
     * ```
     * ```
     * Сценарий: Пример сохранения содержимого директории по json path в переменную
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда я сохраняю результат '$.[4]' в переменной 'PATH'
     *   Затем '${PATH}' равно '.gitkeep'
     *   И я удаляю директорию 'artifacts/tmp/example'
     * ```
     *
     * @param path String
     * @param recursively String?
     */
    @Когда("^[Я|я] получаю содержимое директории '(.*?)'( рекурсивно)?$")
    fun tree(path: String, recursively: String?) {
        super.tree(path, recursively !== null)
    }

    /**
     * Открытие файла.
     *
     * ```
     * Сценарий: Пример чтения из файла
     *   Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
     *     """
     *     Example
     *     """
     *   Тогда я открываю файл 'artifacts/tmp/example.txt'
     *   Затем я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param path String
     */
    @Когда("^[Я|я] открываю файл '(.*?)'$")
    fun `open`(path: String) {
        super.`open`(path, null)
    }

    /**
     * Проверка содержимого файла на соответствие построчно.
     *
     * ```
     * Сценарий: Пример чтения из файла и сравнения содержимого построчно
     *   Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
     *     """
     *     100
     *     example
     *     """
     *   Тогда я открываю файл 'artifacts/tmp/example.txt'
     *   Тогда содержимое файла:
     *     | 1 | равно         | 100        |
     *     | 2 | равно         | example    |
     *     | 1 | соответствует | ^\d+       |
     *     | 2 | соответствует | ^[A-Za-z]+ |
     *     | 1 | содержит      | 10         |
     *     | 2 | содержит      | ex         |
     *     | 1 | не равно      | 10         |
     *     | 2 | не равно      | ex         |
     *     | 1 | больше        | 10         |
     *     | 1 | меньше        | 101        |
     *   Затем я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param dataTable DataTable
     */
    @Тогда("^[С|с]одержимое файла:$")
    fun contentLineByLineMatch(dataTable: DataTable) {
        super.contentLineByLineMatch(dataTable, BaseRuSpec.toComparisonOperation)
    }

    /**
     * Проверка на количество строк в файле.
     *
     * ```
     * Сценарий: Пример чтения из файла и сравнения количества строк на равенство
     *   Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
     *     """
     *     HELLO WORLD
     *     EXAMPLE
     *     """
     *   Тогда я открываю файл 'artifacts/tmp/example.txt'
     *   И файл содержит '2' строки
     *   Затем я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     * ```
     * Сценарий: Пример чтения из файла и сравнения количества строк на минимальное количество
     *   Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
     *     """
     *     HELLO WORLD
     *     EXAMPLE
     *     """
     *   Тогда я открываю файл 'artifacts/tmp/example.txt'
     *   И файл содержит не менее '1' строки
     *   Затем я удаляю файл 'artifacts/tmp/example.txt'
     * ```
     *
     * @param atLeast String?
     * @param size Int
     */
    @Тогда("^[Ф|ф]айл содержит( не менее)? '(\\d+)' строк[у|и]?$")
    fun lines(atLeast: String?, size: Int) {
        super.linesCount(atLeast !== null, size)
    }

    /**
     * Проверка директории на содержание файла или директории.
     *
     * ```
     * Сценарий: Пример получения и сравнения списка файлов и директорий
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp'
     *   Тогда директория содержит:
     *     | .gitkeep           |
     *     | example_first.txt   |
     *     | example_second.txt |
     *   Затем я удаляю файл 'artifacts/tmp/example_first.txt'
     *   И я удаляю файл 'artifacts/tmp/example_second.txt'
     * ```
     * ```
     * Сценарий: Пример получения и сравнения списка файлов и директорий рекурсивно
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/second'
     *   Пусть я создаю директорию 'artifacts/tmp/example/first/third'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
     *   Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
     *   Тогда директория содержит:
     *     | example/first/second/.black-radish    |
     *     | example/first/second/example_first.txt |
     *     | example/first/third/.black-radish     |
     *     | example/first/second                  |
     *     | example/first/third                   |
     *     | example/first                         |
     *     | example                              |
     *   И директория не содержит:
     *     | undefined |
     *   Затем я удаляю директорию 'artifacts/tmp/example'
     * ```
     *
     * @param isNot String?
     * @param dataTable DataTable
     */
    @Тогда("^[Д|д]иректория( не)? содержит:$")
    fun directoryContains(isNot: String?, dataTable: DataTable) {
        super.directoryContains(dataTable, isNot !== null)
    }

    /**
     * Проверка содержимого двух файлов на соответствие.
     *
     * ```
     * Сценарий: Пример сравнения содержимого файлов
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_first.txt'
     *   Пусть я создаю пустой файл 'artifacts/tmp/example_second.txt'
     *   Тогда содержимое файлов 'artifacts/tmp/example_first.txt' и 'artifacts/tmp/example_second.txt' идентично
     *   Затем я удаляю файл 'artifacts/tmp/example_first.txt'
     *   И я удаляю файл 'artifacts/tmp/example_second.txt'
     * ```
     *
     * @param firstFilePath String
     * @param secondFilePath String
     */
    @Тогда("^[С|с]одержимое файлов '(.*?)' и '(.*?)' идентично$")
    override fun contentEquals(firstFilePath: String, secondFilePath: String) {
        super.contentEquals(firstFilePath, secondFilePath)
    }
}
