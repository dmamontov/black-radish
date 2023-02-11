# language: ru

@examples @module:csv:db @module:core @database @file
Функция: CsvDatabase

  Данный модуль предоставляет возможность работы с csv файлами как с базой данных.

  .. note:: Модуль использует драйвер `CsvJdbc <https://github.com/simoc/csvjdbc>`_

  .. note:: Соединение с базой данных закрывается автоматически по завершению выполнения сценария.

  .. warning:: База данных доступна только для чтения.

  Правило: База данных

    Сценарий: Пример подключения к базе данных
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'

    Сценарий: Пример подключения к базе данных в zip архиве
      #File:bin: src/test/resources/artifacts/database/example.zip
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database/example.zip'

    Сценарий: Пример подключения к базе данных с параметрами
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | charset | UTF-8 |

    Сценарий: Пример закрытия соединения к базе данных
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда закрываю соединение с базой данных

    Сценарий: Пример выполнения запроса к базе данных
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда выполняю запрос 'SELECT * FROM example;'

    Сценарий: Пример выполнения запроса к базе данных, вариант 2
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда выполняю запрос:
        """
        SELECT * FROM example;
        """

    Сценарий: Пример выполнения запросов к базе данных из файла
      #Warning: При выполнении запросов из файла результат будет преобразован только для последнего запроса в файле
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда выполняю запрос из 'artifacts/sql/example.sql'

    Сценарий: Пример установки максимального времени выполнения запроса
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда устанавливаю максимальное время выполнения запроса '2' секунды
      И выполняю запрос 'SELECT * FROM example;'

    Сценарий: Пример проверки таблицы на существование
      #File:bin: src/test/resources/artifacts/database/example.csv
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database'
      Тогда таблица 'undefined' не существует
      И таблица 'example' существует

    Сценарий: Пример проверки соответствия результата запроса
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      И результат запроса соответствует:
        | localAS | remoteAS | remoteIp    | routerId  | status | uptime   |
        | 65551   | 65551    | 10.10.10.10 | 192.0.2.1 | 5      | 10:37:12 |
        | 65551   | 65552    | 10.10.100.1 | 192.0.2.1 | 0      | 10:38:27 |
        | 65551   | 65553    | 10.100.10.9 | 192.0.2.1 | 1      | 07:55:38 |

  Правило: DatabaseConverter

    Сценарий: Пример проверки количество записей в результате запроса
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      И результат содержит '3' записи

    Сценарий: Пример проверки количество записей в результате запроса на минимальное количество
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      И результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в результате запроса по json path
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      И сумма '$..remoteAS' в результате равна '196656.0'

    Сценарий: Пример проверки результата преобразования результата запроса по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      И в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения результата запроса в переменную как json
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      Затем я сохраняю результат в переменной 'JSON'
      И '${JSON}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-sql.json}'

    Сценарий: Пример сохранения результата запроса по json path в переменную
      #File:bin: src/test/resources/artifacts/database/example.csv
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаюсь к базе данных 'csv' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |
      Тогда выполняю запрос из 'artifacts/sql/example.sql'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
