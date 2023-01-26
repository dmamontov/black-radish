# language: ru

@example
Функция: Файлы *.csv

  Данная функция представляет собой примеры работы с файлами *.csv

  Правило: Создание файла

    Сценарий: Пример создания *.csv файла
      Если я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда файл 'artifacts/files/example.csv' существует
      Затем я удаляю файл 'artifacts/files/example.csv'

    Сценарий: Пример создания *.csv файла со своими разделителями
      Пусть я устанавливаю для csv разделитель ';'
      Пусть я устанавливаю для csv межстрочный разделитель '\r\n'
      Если я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда файл 'artifacts/files/example.csv' существует
      Затем я удаляю файл 'artifacts/files/example.csv'

  Правило: Чтение файла

    Сценарий: Пример чтения из файла
      #Note: больше примеров в модуле filesystem
      Когда я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда я открываю csv файл 'artifacts/files/example.csv'
      Затем я удаляю файл 'artifacts/files/example.csv'

    Сценарий: Пример чтения из файла с заголовками в первой строке
      #Note: больше примеров в модуле filesystem
      Когда я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #  | text     | number |
        | #1 | example1 | 101    |
        | #2 | example2 | 102    |
        | #3 | example3 | 103    |
        | #4 | example4 | 104    |
        | #5 | example5 | 105    |
      Тогда я открываю csv файл 'artifacts/files/example.csv' с заголовками в первой строке
      Затем я удаляю файл 'artifacts/files/example.csv'

    Сценарий: Пример чтения из файла с установленными заголовками
      #Note: больше примеров в модуле filesystem
      Когда я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда я открываю csv файл 'artifacts/files/example.csv' с заголовками:
        | # | text | number |
      Затем я удаляю файл 'artifacts/files/example.csv'

    Сценарий: Пример чтения из файла и сравнения содержимого
      Когда я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда я открываю csv файл 'artifacts/files/example.csv' с заголовками:
        | # | text | number |
      И содержимое csv файла соответствует:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      И содержимое csv файла не соответствует:
        | #1 | example1 | 101 |
        | #5 | example5 | 105 |
      Затем я удаляю файл 'artifacts/files/example.csv'

    Сценарий: Пример чтения *.csv файла со своими разделителями
      Пусть я устанавливаю для csv разделитель '\t'
      Пусть я устанавливаю для csv межстрочный разделитель '\r\n'
      #Note: больше примеров в модуле filesystem
      Если я создаю csv файл 'artifacts/files/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда я открываю csv файл 'artifacts/files/example.csv'
      И содержимое csv файла соответствует:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Затем я удаляю файл 'artifacts/files/example.csv'

  Правило: Парсинг

    Сценарий: Пример парсинга csv файла без заголовков
      #Note: больше примеров в модуле filesystem
      Когда я открываю csv файл 'artifacts/parsers/example_first.csv'
      Тогда результат содержит '3' записи
      И сумма '$.*.[1]' в результате равна '196656.0'
      И в результате:
        | $.[0].[0] | равно         | 65551                |
        | $.[0].[1] | больше        | 65550                |
        | $.[0].[2] | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].[4] | меньше        | 1                    |
        | $.[2].[4] | не равно      | 0                    |
        | $.[2].[3] | содержит      | 192                  |

    Сценарий: Пример парсинга csv файла с заголовками в первой строке
      #Note: больше примеров в модуле filesystem
      Когда я открываю csv файл 'artifacts/parsers/example_second.csv' с заголовками в первой строке
      Тогда я проверяю результат по схеме 'artifacts/schemas/csv.json'
      И результат содержит '3' записи
      И сумма '$..remoteAS' в результате равна '196656.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |

    Сценарий: Пример парсинга csv файла с установленными заголовками
      #Note: больше примеров в модуле filesystem
      Когда я открываю csv файл 'artifacts/parsers/example_first.csv' с заголовками:
        | localAS | remoteAS | remoteIp | routerId | status | uptime |
      Тогда я проверяю результат по схеме 'artifacts/schemas/csv.json'
      И результат содержит '3' записи
      И сумма '$..remoteAS' в результате равна '196656.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |
