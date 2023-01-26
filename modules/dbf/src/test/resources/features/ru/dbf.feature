# language: ru

@example @sql
Функция: База данных *.dbf

  Данная функция представляет собой примеры работы с базой данных на основе *.dbf.
  подробнее https://github.com/simoc/csvjdbc/blob/master/docs/doc.md

  Правило: Подключение

    Сценарий: Пример подключения
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database'
      Тогда закрываю соединение с базой данных

    Сценарий: Пример подключения к базе данных в zip архиве
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database/example.zip'
      Тогда закрываю соединение с базой данных

    Сценарий: Пример подключения с параметрами
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database' с параметрами:
        | charset | UTF-8 |
      Тогда закрываю соединение с базой данных

  Правило: Запросы

    Сценарий: Пример выполнения запроса
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database'
      Тогда выполняю запрос к базе данных 'SELECT * FROM example;'
      Затем закрываю соединение с базой данных

    Сценарий: Пример выполнения запроса из файла
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database'
      Тогда выполняю запрос к базе данных из 'artifacts/sql/select.sql'
      Затем закрываю соединение с базой данных

    Сценарий: Пример проверки существования таблицы
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database'
      Тогда таблица 'example' существует в базе данных
      И таблица 'test' не существует в базе данных
      Затем закрываю соединение с базой данных

  Правило: Результат

    Сценарий: Пример сравнения результата запроса
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |

      Тогда выполняю запрос к базе данных 'SELECT * FROM example;'
      И результат запроса к базе данных соответствует:
        | LOCALAS | REMOTEAS | REMOTEIP    | ROUTERID  | STATUS | UPTIME   |
        | 65551   | 65551    | 10.10.10.10 | 192.0.2.1 | 5      | 10:37:12 |
        | 65551   | 65552    | 10.10.100.1 | 192.0.2.1 | 0      | 10:38:27 |
        | 65551   | 65553    | 10.100.10.9 | 192.0.2.1 | 1      | 07:55:38 |

      Затем выполняю запрос к базе данных 'SELECT LOCALAS, REMOTEIP, STATUS FROM example WHERE STATUS=0;'
      И результат запроса к базе данных соответствует:
        | LOCALAS | REMOTEIP    | STATUS |
        | 65551   | 10.10.100.1 | 0      |

      Затем закрываю соединение с базой данных

    Сценарий: Пример парсинга результата запроса
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |

      Тогда выполняю запрос к базе данных 'SELECT * FROM example;'
      И результат содержит '3' записи
      И сумма '$..REMOTEAS' в результате равна '196656.0'
      И в результате:
        | $.[0].LOCALAS  | равно         | 65551                |
        | $.[0].REMOTEAS | больше        | 65550                |
        | $.[0].REMOTEIP | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].STATUS   | меньше        | 1                    |
        | $.[2].STATUS   | не равно      | 0                    |
        | $.[2].ROUTERID | содержит      | 192                  |

      Затем закрываю соединение с базой данных

    Сценарий: Пример парсинга результата запроса и сохранения в переменную
      Когда я подключаюсь к базе данных 'dbf' по пути 'artifacts/database' с параметрами:
        | columnTypes | Int,Int,String,String,Int,String |

      Тогда выполняю запрос к базе данных 'SELECT * FROM example;'
      Затем я сохраняю результат '$.[0].LOCALAS' в переменной 'LOCAL_AS'
      Тогда '${LOCAL_AS}' равно '65551'

      Затем закрываю соединение с базой данных


