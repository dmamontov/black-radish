# language: ru

@examples
Функция: Файлы *.xls, *.xlsx

  Данная функция представляет собой примеры работы с файлами *.xls, *.xlsx

  Правило: Создание файла

    Сценарий: Пример создания *.xls файла
      Когда я создаю пустой xls файл 'artifacts/files/example.xls'
      Тогда файл 'artifacts/files/example.xls' существует
      Затем я удаляю файл 'artifacts/files/example.xls'

    Сценарий: Пример создания *.xlsx файла
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда файл 'artifacts/files/example.xlsx' существует
      Затем я удаляю файл 'artifacts/files/example.xlsx'

  Правило: Чтение файла

    Сценарий: Пример чтения из файла
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда в xlsx добавляю новый лист 'one' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Тогда я открываю xlsx файл 'artifacts/files/example.xlsx'
      Затем я удаляю файл 'artifacts/files/example.xlsx'

  Правило: Модификация файла

    Сценарий: Добавление листа с содержимым
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда в xlsx добавляю новый лист 'one' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      И в xlsx добавляю новый лист 'two' содержащий:
        | #5 | example5 | 105 |
        | #4 | example4 | 104 |
        | #3 | example3 | 103 |
        | #2 | example2 | 102 |
        | #1 | example1 | 101 |
      Затем файл 'artifacts/files/example.xlsx' существует
      И я удаляю файл 'artifacts/files/example.xlsx'

    Сценарий: Добавление листа с содержимым и указанием типов
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда в xlsx добавляю новый лист 'one' содержащий:
        | #1 | example1 | 101{type=Int} | true{type=Boolean}  |
        | #2 | example2 | 102{type=Int} | false{type=Boolean} |
        | #3 | example3 | 103{type=Int} | false{type=Boolean} |
        | #4 | example4 | 104{type=Int} | true{type=Boolean}  |
        | #5 | example5 | 105{type=Int} | false{type=Boolean} |
      Затем файл 'artifacts/files/example.xlsx' существует
      И я удаляю файл 'artifacts/files/example.xlsx'

    Сценарий: Запись значения в ячейку
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда в xlsx добавляю новый лист 'one' содержащий:
        | #1 | example1 | 101{type=Int} | true{type=Boolean}  |
        | #2 | example2 | 102{type=Int} | false{type=Boolean} |
        | #3 | example3 | 103{type=Int} | false{type=Boolean} |
        | #4 | example4 | 104{type=Int} | true{type=Boolean}  |
        | #5 | example5 | 105{type=Int} | false{type=Boolean} |
      И в xlsx листе 'one' записываю 'one' в ячейку 'D6'
      И в xlsx листе 'one' записываю '500{type=Int}' в ячейку 'D7'
      И в xlsx листе 'one' записываю формулу 'SUM(C1:C5)' в ячейку 'C6'
      Тогда файл 'artifacts/files/example.xlsx' существует
      Затем я удаляю файл 'artifacts/files/example.xlsx'

    Сценарий: Объединение ячеек
      Когда я создаю пустой xlsx файл 'artifacts/files/example.xlsx'
      Тогда в xlsx добавляю новый лист 'one' содержащий:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      И в xlsx листе 'one' объединяю ячейки от 'A4' до 'C5'
      Тогда файл 'artifacts/files/example.xlsx' существует
      Затем я удаляю файл 'artifacts/files/example.xlsx'


  Правило: Парсинг

    Сценарий: Пример парсинга xlsx файла
      #Note: больше примеров в модуле filesystem
      Когда я открываю xlsx файл 'artifacts/parsers/example.xlsx'
      Тогда сумма '$.[0]..B' в результате равна '196656.0'
      #Note: $.[list].[row].column
      И в результате:
        | $.[0].[0].A | равно         | 65551.0              |
        | $.[0].[0].B | больше        | 65550.0              |
        | $.[0].[0].C | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[0].[1].E | меньше        | 1                    |
        | $.[0].[2].E | не равно      | 0                    |
        | $.[0].[2].D | содержит      | 192                  |