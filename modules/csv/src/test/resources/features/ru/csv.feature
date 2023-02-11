# language: ru

@examples @module:csv @module:core @module:filesystem @file
Функция: Csv

  Данный модуль предоставляет возможность работы с csv файлами.

  .. note:: Больше примеров в модуле ``Filesystem``

  Правило: Создание

    Сценарий: Пример создания csv файла
      Если я создаю csv файл 'artifacts/tmp/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда файл 'artifacts/tmp/example.csv' существует
      Затем я удаляю файл 'artifacts/tmp/example.csv'

    Сценарий: Пример создания csv файла со своими разделителями
      Пусть я устанавливаю для csv разделитель ';'
      Пусть я устанавливаю для csv межстрочный разделитель '\r\n'
      Если я создаю csv файл 'artifacts/tmp/example.csv' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда файл 'artifacts/tmp/example.csv' существует
      Затем я удаляю файл 'artifacts/tmp/example.csv'

  Правило: Чтение

    Сценарий: Пример чтения из файла
      #File: src/test/resources/artifacts/files/example_first.csv
      Когда я открываю csv файл 'artifacts/files/example_first.csv'

    Сценарий: Пример чтения из файла с заголовками в первой строке
      #File: src/test/resources/artifacts/files/example_second.csv
      Когда я открываю csv файл 'artifacts/files/example_second.csv' с заголовками в первой строке

    Сценарий: Пример чтения из файла с установленными заголовками
      #File: src/test/resources/artifacts/files/example_first.csv
      Когда я открываю csv файл 'artifacts/files/example_first.csv' с заголовками:
        | localAS | remoteAS | remoteIp | routerId | status | uptime |

  Правило: Сравнение

    Сценарий: Пример чтения из файла и сравнения содержимого
      #File: src/test/resources/artifacts/files/example_first.csv
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Тогда содержимое csv файла соответствует:
        | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
        | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
        | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |
      И содержимое csv файла не соответствует:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |

    Сценарий: Пример чтения csv файла со своими разделителями
      #File: src/test/resources/artifacts/files/example_third.csv
      Пусть я устанавливаю для csv разделитель '\t'
      Пусть я устанавливаю для csv межстрочный разделитель '\r\n'
      Когда я открываю csv файл 'artifacts/files/example_third.csv'
      Тогда содержимое csv файла соответствует:
        | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
        | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
        | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |

  Правило: CsvConverter

    Сценарий: Пример проверки количество записей в csv
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Тогда результат содержит '3' записи

    Сценарий: Пример проверки количество записей в csv на минимальное количество
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в csv без заголовков по json path
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Тогда сумма '$.*.[1]' в результате равна '196656.0'

    Сценарий: Пример суммирования значений в csv с заголовками по json path
      #File:bin: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-second.json
      Когда я открываю csv файл 'artifacts/files/example_second.csv' с заголовками в первой строке
      Тогда сумма '$..remoteAS' в результате равна '196656.0'

    Сценарий: Пример проверки значений в csv без заголовков по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      И в результате:
        | $.[0].[0] | равно         | 65551                |
        | $.[0].[1] | больше        | 65550                |
        | $.[0].[2] | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].[4] | меньше        | 1                    |
        | $.[2].[4] | не равно      | 0                    |
        | $.[2].[3] | содержит      | 192                  |

    Сценарий: Пример проверки значений в csv с заголовками по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-second.json
      Когда я открываю csv файл 'artifacts/files/example_second.csv' с заголовками в первой строке
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |

    Сценарий: Пример сохранения значений csv в переменную как json
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Затем я сохраняю результат в переменной 'JSON'
      И '${JSON}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-first.json}'

    Сценарий: Пример сохранения результата значений csv без заголовков по json path в переменную
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_first.csv'
      Затем я сохраняю результат '$.[0].[0]' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'

    Сценарий: Пример сохранения результата значений csv с заголовками по json path в переменную
      #File:bin: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      Когда я открываю csv файл 'artifacts/files/example_second.csv' с заголовками в первой строке
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
