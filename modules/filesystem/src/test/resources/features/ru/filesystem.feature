# language: ru

@example
Функция: Файловая система

  Данная функция представляет собой примеры работы с файловой системой

  Правило: Директории

    Сценарий: Пример создания директории
      Когда я создаю директорию 'artifacts/directories/tree/first/second'

    Сценарий: Пример удаления директории
      Когда я создаю директорию 'artifacts/directories/tree/first/second'
      Тогда я удаляю директорию 'artifacts/directories/tree'

    Сценарий: Пример проверки существования директории в ресурсах
      Когда я создаю директорию 'artifacts/directories/tree/first/second'
      Тогда директория 'artifacts/directories/tree/first/second' существует
      Затем я удаляю директорию 'artifacts/directories/tree'

    Сценарий: Пример проверки существования директории относительно корня пакета
      Допустим директория 'src/test/resources/artifacts/directories' существует

    Сценарий: Пример проверки не существования директории
      Допустим директория 'src/undefined' не существует

    Сценарий: Пример проверки с удалением директорий
      Когда я создаю директорию 'artifacts/directories/tree/first/second'
      Тогда директория 'artifacts/directories/tree/first/second' существует
      Затем я удаляю директорию 'artifacts/directories/tree'
      Тогда директория 'artifacts/directories/tree/first/second' не существует

    Сценарий: Пример копирования директории
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Когда я копирую директорию 'artifacts/directories/tree/first/second' в 'artifacts/directories/tree/first/three'
      Тогда директория 'artifacts/directories/tree/first/three' существует
      Затем я удаляю директорию 'artifacts/directories/tree'

    Сценарий: Пример перемещения директории
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Когда я перемещаю директорию 'artifacts/directories/tree/first/second' в 'artifacts/directories/tree/first/three'
      Тогда директория 'artifacts/directories/tree/first/second' не существует
      И директория 'artifacts/directories/tree/first/three' существует
      Затем я удаляю директорию 'artifacts/directories/tree'

    Сценарий: Пример перемещения директории в другую директорию
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Пусть я создаю директорию 'artifacts/directories/tree/first/three'
      Когда я перемещаю директорию 'artifacts/directories/tree/first/second' в директорию 'artifacts/directories/tree/first/three'
      Тогда директория 'artifacts/directories/tree/first/second' не существует
      И директория 'artifacts/directories/tree/first/three/second' существует
      Затем я удаляю директорию 'artifacts/directories/tree'

    Сценарий: Пример получения и сравнения списка файлов
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Пусть я создаю пустой файл 'artifacts/files/example2.txt'
      Когда я получаю структуру директории 'artifacts/files'
      Тогда директория содержит:
        | .gitkeep     |
        | example.txt  |
        | example2.txt |
      Затем я удаляю файл 'artifacts/files/example.txt'
      И я удаляю файл 'artifacts/files/example2.txt'

    Сценарий: Пример получения и сравнения списка файлов рекурсивно
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Пусть я создаю директорию 'artifacts/directories/tree/first/three'
      Пусть я создаю пустой файл 'artifacts/directories/tree/first/second/example.txt'
      Пусть я создаю пустой файл 'artifacts/directories/tree/first/three/example2.txt'
      Когда я получаю структуру директории 'artifacts/directories' рекурсивно
      Тогда директория содержит:
        | tree/first/second/.black-radish |
        | tree/first/second/example.txt   |
        | tree/first/three/.black-radish  |
        | tree/first/second               |
        | tree/first/three                |
        | tree/first                      |
        | tree                            |
      Тогда директория не содержит:
        | undefined |
      Затем я удаляю директорию 'artifacts/directories/tree'

  Правило: Файлы

    Сценарий: Пример создания пустого файла
      Когда я создаю пустой файл 'artifacts/files/example.txt'

    Сценарий: Пример удаления файла
      Когда я создаю пустой файл 'artifacts/files/example.txt'
      Тогда я удаляю файл 'artifacts/files/example.txt'

    Сценарий: Пример создания файла c произвольным содержимым
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        HELLO WORLD
        """
      Тогда я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример проверки существования файла в ресурсах
      Когда я создаю пустой файл 'artifacts/files/example.txt'
      Тогда файл 'artifacts/files/example.txt' существует
      Затем я удаляю файл 'artifacts/files/example.txt'

    Сценарий: Пример проверки существования файла относительно корня пакета
      Допустим файл 'src/test/resources/artifacts/directories/.gitkeep' существует

    Сценарий: Пример проверки не существования файла
      Допустим файл 'src/undefined.txt' не существует

    Сценарий: Пример проверки с удалением файла
      Когда я создаю пустой файл 'artifacts/files/example.txt'
      Тогда файл 'artifacts/files/example.txt' существует
      Затем я удаляю файл 'artifacts/files/example.txt'
      Тогда файл 'artifacts/files/example.txt' не существует

    Сценарий: Пример чтения из файла
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        HELLO WORLD
        """
      Тогда я открываю файл 'artifacts/files/hello.txt'
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример чтения из файла и сравнения содержимого
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        HELLO WORLD
        """
      Тогда я открываю файл 'artifacts/files/hello.txt'
      Тогда содержимое файла соответствует:
        """
        HELLO WORLD
        """
      Тогда содержимое файла не соответствует:
        """
        EXAMPLE
        """
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример чтения из файла и сравнения содержимого построчно
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        100
        example
        """
      Тогда я открываю файл 'artifacts/files/hello.txt'
      #Note: | номер строки | операция сравнения | значение |
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
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример чтения из файла и сравнения количества строк
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        HELLO WORLD
        EXAMPLE
        """
      Тогда я открываю файл 'artifacts/files/hello.txt'
      И файл содержит '2' строки
      И файл содержит не менее '1' строки
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример сравнения содержимого файлов
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Пусть я создаю пустой файл 'artifacts/files/example2.txt'
      Тогда содержимое файлов 'artifacts/files/example.txt' и 'artifacts/files/example2.txt' идентично
      Затем я удаляю файл 'artifacts/files/example.txt'
      И я удаляю файл 'artifacts/files/example2.txt'

    Сценарий: Пример копирования файла
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Когда я копирую файл 'artifacts/files/example.txt' в 'artifacts/directories/tree/first/second/example.txt'
      Тогда файл 'artifacts/directories/tree/first/second/example.txt' существует
      Затем я удаляю директорию 'artifacts/directories/tree/first/second'
      И я удаляю файл 'artifacts/files/example.txt'

    Сценарий: Пример перемещения файла
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Когда я перемещаю файл 'artifacts/files/example.txt' в 'artifacts/directories/tree/first/second/example.txt'
      Тогда файл 'artifacts/files/example.txt' не существует
      И файл 'artifacts/directories/tree/first/second/example.txt' существует
      Затем я удаляю директорию 'artifacts/directories/tree/first/second'

    Сценарий: Пример перемещения файла в директорию
      Пусть я создаю директорию 'artifacts/directories/tree/first/second'
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Когда я перемещаю файл 'artifacts/files/example.txt' в директорию 'artifacts/directories/tree/first/second'
      Тогда файл 'artifacts/files/example.txt' не существует
      И файл 'artifacts/directories/tree/first/second/example.txt' существует
      Затем я удаляю директорию 'artifacts/directories/tree/first/second'

  Правило: Парсинг

    Сценарий: Пример парсинга json файла
      #Note: подробнее https://github.com/json-path/JsonPath
      Когда я открываю файл 'artifacts/parsers/example.json'
      Тогда я проверяю результат по схеме 'artifacts/schemas/json.json'
      И результат содержит '3' записи
      И сумма '$..remoteAS' в результате равна '196656.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |

    Сценарий: Пример парсинга xml файла
      #Note: подробнее https://github.com/json-path/JsonPath
      Когда я открываю файл 'artifacts/parsers/example.xml'
      Тогда я проверяю результат по схеме 'artifacts/schemas/xml.xsd'
      И сумма '$.root.element.*.remoteAS' в результате равна '196656.0'
      И в результате:
        | $.root.element.[0].localAS  | равно         | 65551                |
        | $.root.element.[0].remoteAS | больше        | 65550                |
        | $.root.element.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.root.element.[1].status   | меньше        | 1                    |
        | $.root.element.[2].status   | не равно      | 0                    |
        | $.root.element.[2].routerId | содержит      | 192                  |

    Сценарий: Пример парсинга yaml файла
      #Note: подробнее https://github.com/json-path/JsonPath
      Когда я открываю файл 'artifacts/parsers/example.yaml'
      Тогда я проверяю результат по схеме 'artifacts/schemas/yaml.json'
      И результат содержит '3' записи
      И сумма '$..remoteAS' в результате равна '196656.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |

    Сценарий: Пример парсинга текстового файла по шаблону
      #Note: подробнее https://github.com/sonalake/utah-parser, https://github.com/json-path/JsonPath
      Когда я разбираю файл 'artifacts/parsers/example.txt' используя шаблон 'artifacts/templates/parser.xml'
      Тогда результат содержит '3' записи
      И сумма '$..localAS' в результате равна '196653.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |
