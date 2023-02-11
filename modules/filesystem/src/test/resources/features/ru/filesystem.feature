# language: ru

@examples @module:filesystem @module:core @file @converter
Функция: Filesystem

  Данный модуль предоставляет возможность работы с файловой системой.

  Правило: Директории

    Сценарий: Пример создания директории
      Когда я создаю директорию 'artifacts/tmp/example/first/second'

    Сценарий: Пример удаления директории
      Когда я создаю директорию 'artifacts/tmp/example/first/second'
      Тогда я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример проверки существования директории в ресурсах
      Когда я создаю директорию 'artifacts/tmp/example/first/second'
      Тогда директория 'artifacts/tmp/example/first/second' существует
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример проверки существования директории относительно корня пакета
      Допустим директория 'src/test/resources/artifacts/tmp' существует

    Сценарий: Пример проверки не существования директории
      Допустим директория 'src/undefined' не существует

    Сценарий: Пример проверки с удалением директорий
      Когда я создаю директорию 'artifacts/tmp/example/first/second'
      Тогда директория 'artifacts/tmp/example/first/second' существует
      Затем я удаляю директорию 'artifacts/tmp/example'
      И директория 'artifacts/tmp/example/first/second' не существует

    Сценарий: Пример копирования директории
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Когда я копирую директорию 'artifacts/tmp/example/first/second' в 'artifacts/tmp/example/first/copy'
      Тогда директория 'artifacts/tmp/example/first/copy' существует
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример перемещения директории
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Когда я перемещаю директорию 'artifacts/tmp/example/first/second' в 'artifacts/tmp/example/first/move'
      Тогда директория 'artifacts/tmp/example/first/second' не существует
      И директория 'artifacts/tmp/example/first/move' существует
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример перемещения директории в другую директорию
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/move'
      Когда я перемещаю директорию 'artifacts/tmp/example/first/second' в директорию 'artifacts/tmp/example/first/move'
      Тогда директория 'artifacts/tmp/example/first/second' не существует
      И директория 'artifacts/tmp/example/first/move/second' существует
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример получения и сравнения списка файлов и директорий
      Пусть я создаю пустой файл 'artifacts/tmp/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp'
      Тогда директория содержит:
        | .gitkeep           |
        | example_first.txt   |
        | example_second.txt |
      Затем я удаляю файл 'artifacts/tmp/example_first.txt'
      И я удаляю файл 'artifacts/tmp/example_second.txt'

    Сценарий: Пример получения и сравнения списка файлов и директорий рекурсивно
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда директория содержит:
        | example/first/second/.black-radish    |
        | example/first/second/example_first.txt |
        | example/first/third/.black-radish     |
        | example/first/second                  |
        | example/first/third                   |
        | example/first                         |
        | example                              |
      И директория не содержит:
        | undefined |
      Затем я удаляю директорию 'artifacts/tmp/example'

  Правило: Файлы

    Сценарий: Пример создания пустого файла
      Когда я создаю пустой файл 'artifacts/tmp/example.txt'

    Сценарий: Пример удаления файла
      Когда я создаю пустой файл 'artifacts/tmp/example.txt'
      Тогда я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример создания файла c произвольным содержимым
      Когда я создаю файл 'artifacts/tmp/hello.txt' содержащий:
        """
        HELLO WORLD
        """
      Тогда я удаляю файл 'artifacts/tmp/hello.txt'

    Сценарий: Пример проверки существования файла в ресурсах
      Когда я создаю пустой файл 'artifacts/tmp/example.txt'
      Тогда файл 'artifacts/tmp/example.txt' существует
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример проверки существования файла относительно корня пакета
      Допустим файл 'src/test/resources/artifacts/tmp/.gitkeep' существует

    Сценарий: Пример проверки не существования файла
      Допустим файл 'src/undefined.txt' не существует

    Сценарий: Пример проверки с удалением файла
      Когда я создаю пустой файл 'artifacts/tmp/example.txt'
      Тогда файл 'artifacts/tmp/example.txt' существует
      Затем я удаляю файл 'artifacts/tmp/example.txt'
      Тогда файл 'artifacts/tmp/example.txt' не существует

    Сценарий: Пример чтения из файла
      Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        Example
        """
      Тогда я открываю файл 'artifacts/tmp/example.txt'
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример чтения из файла и сравнения содержимого построчно
      #Note: Для построчного сравнения файла используется следующий порядок: номер строки, операция сравнения, значение
      #Note: Поддерживаются все проверки аналогично переменным
      Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        100
        example
        """
      Тогда я открываю файл 'artifacts/tmp/example.txt'
      Тогда содержимое файла:
        | 1 | равно         | 100        |
        | 2 | равно         | example    |
        | 1 | соответствует | ^\d+       |
        | 2 | соответствует | ^[A-Za-z]+ |
        | 1 | содержит      | 10         |
        | 2 | содержит      | ex         |
        | 1 | не равно      | 10         |
        | 2 | не равно      | ex         |
        | 1 | больше        | 10         |
        | 1 | меньше        | 101        |
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример чтения из файла и сравнения количества строк на равенство
      Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        HELLO WORLD
        EXAMPLE
        """
      Тогда я открываю файл 'artifacts/tmp/example.txt'
      И файл содержит '2' строки
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример чтения из файла и сравнения количества строк на минимальное количество
      Когда я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        HELLO WORLD
        EXAMPLE
        """
      Тогда я открываю файл 'artifacts/tmp/example.txt'
      И файл содержит не менее '1' строки
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример сравнения содержимого файлов
      Пусть я создаю пустой файл 'artifacts/tmp/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example_second.txt'
      Тогда содержимое файлов 'artifacts/tmp/example_first.txt' и 'artifacts/tmp/example_second.txt' идентично
      Затем я удаляю файл 'artifacts/tmp/example_first.txt'
      И я удаляю файл 'artifacts/tmp/example_second.txt'

    Сценарий: Пример копирования файла
      Пусть я создаю директорию 'artifacts/tmp/tree'
      Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
      Когда я копирую файл 'artifacts/tmp/example.txt' в 'artifacts/tmp/tree/example.txt'
      Тогда файл 'artifacts/tmp/tree/example.txt' существует
      Затем я удаляю директорию 'artifacts/tmp/tree'
      И я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример перемещения файла
      Пусть я создаю директорию 'artifacts/tmp/tree'
      Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
      Когда я перемещаю файл 'artifacts/tmp/example.txt' в 'artifacts/tmp/tree/example.txt'
      Тогда файл 'artifacts/tmp/example.txt' не существует
      И файл 'artifacts/tmp/tree/example.txt' существует
      Затем я удаляю директорию 'artifacts/tmp/tree'

    Сценарий: Пример перемещения файла в директорию
      Пусть я создаю директорию 'artifacts/tmp/tree'
      Пусть я создаю пустой файл 'artifacts/tmp/example.txt'
      Когда я перемещаю файл 'artifacts/tmp/example.txt' в директорию 'artifacts/tmp/tree'
      Тогда файл 'artifacts/tmp/example.txt' не существует
      И файл 'artifacts/tmp/tree/example.txt' существует
      Затем я удаляю директорию 'artifacts/tmp/tree'

  Правило: Сравнение результата
    #Note: Поддерживаются все операции сравнения

    Сценарий: Пример сравнения оригинала результата
      Пусть я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        Example
        """
      Когда я открываю файл 'artifacts/tmp/example.txt'
      Тогда результат равен 'Example'
      И результат не равен 'undefined'
      И результат содержит 'Ex'
      Затем я удаляю файл 'artifacts/tmp/example.txt'

    Сценарий: Пример сравнения оригинала результата, вариант 2
      Пусть я создаю файл 'artifacts/tmp/example.txt' содержащий:
        """
        Example
        """
      Когда я открываю файл 'artifacts/tmp/example.txt'
      Тогда результат равен:
        """
        Example
        """
      И результат не равен:
        """
        undefined
        """
      И результат содержит:
        """
        Ex
        """
      Затем я удаляю файл 'artifacts/tmp/example.txt'

  Правило: DirectoryTreeConverter

    Сценарий: Пример проверки количества файлов и директорий в директории
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда результат содержит '9' записей
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример проверки количества файлов и директорий в директории на минимальное количество
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда результат содержит не менее '2' записей
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример проверки содержимого директории по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда в результате:
        | $.[0]  | равно         | example/first/third/example_second.txt |
        | $.[1]  | соответствует | ^.*\/\.black-radish$                  |
        | $.[2]  | содержит      | example_first                          |
      Затем я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример сохранения содержимого директории в переменную как json
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда я сохраняю результат в переменной 'JSON'
      Затем '${JSON}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-tree.json}'
      И я удаляю директорию 'artifacts/tmp/example'

    Сценарий: Пример сохранения содержимого директории по json path в переменную
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Пусть я создаю директорию 'artifacts/tmp/example/first/second'
      Пусть я создаю директорию 'artifacts/tmp/example/first/third'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/second/example_first.txt'
      Пусть я создаю пустой файл 'artifacts/tmp/example/first/third/example_second.txt'
      Когда я получаю содержимое директории 'artifacts/tmp' рекурсивно
      Тогда я сохраняю результат '$.[4]' в переменной 'PATH'
      Затем '${PATH}' равно '.gitkeep'
      И я удаляю директорию 'artifacts/tmp/example'

  Правило: JsonConverter

    Сценарий: Пример проверки количество записей в json на равенство
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в json на минимальное количество
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации json по json схеме
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-json.json'

    Сценарий: Пример суммирования значений в json по json path
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования json по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Сценарий: Пример сохранения результата преобразования json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Сценарий: Пример сохранения результата преобразования json по json path в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю файл 'artifacts/files/example.json'
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: YamlConverter

    Сценарий: Пример проверки количество записей в yaml на равенство
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в yaml на минимальное количество
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации yaml по json схеме
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-yaml.json'

    Сценарий: Пример суммирования значений в yaml по json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования yaml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Сценарий: Пример сохранения результата преобразования yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Сценарий: Пример сохранения результата преобразования yaml по json path в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю файл 'artifacts/files/example.yaml'
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: XmlConverter

    Сценарий: Пример валидации xml по xsd схеме
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-xml.xsd'

    Сценарий: Пример суммирования значений в xml по json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда сумма '$.root.element.*.remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования xml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда в результате:
        | $.root.element.[0].localAS  | равно         | 65551 |
        | $.root.element.[0].remoteAS | больше        | 65550 |
        | $.root.element.[1].localAS  | соответствует | ^\d+$ |
        | $.root.element.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Сценарий: Пример сохранения результата преобразования xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Сценарий: Пример сохранения результата преобразования xml по json path в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю файл 'artifacts/files/example.xml'
      Тогда я сохраняю результат '$.root.element.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: TemplateConverter

    Сценарий: Пример проверки количество записей преобразованных по шаблону на равенство
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит '2' записи

    Сценарий: Пример проверки количество записей преобразованных по шаблону на минимальное количество
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в преобразованных данных по шаблону и json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования данных по шаблону и json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю оригинал в переменной 'ORIGINAL'
      И '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат в переменной 'RESULT'
      И '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону и json path в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю файл 'artifacts/files/example.txt'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
