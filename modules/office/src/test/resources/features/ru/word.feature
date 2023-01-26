# language: ru

@example
Функция: Файлы *.docx

  Данная функция представляет собой примеры работы с файлами *.docx

  Правило: Создание файла

    Сценарий: Пример создания *.docx файла
      Когда я создаю docx файл 'artifacts/files/example.docx' содержащий:
        """
        EXAMPLE
        """
      Тогда файл 'artifacts/files/example.docx' существует
      Затем я удаляю файл 'artifacts/files/example.docx'

    Сценарий: Пример создания *.docx файла на основе другого файла
      Когда я создаю docx файл 'artifacts/files/example.docx' на основе 'artifacts/files/example.txt'
      Тогда файл 'artifacts/files/example.docx' существует
      Затем я удаляю файл 'artifacts/files/example.docx'

  Правило: Чтение файла

    Сценарий: Пример чтения из файла
      Когда я открываю docx файл 'artifacts/parsers/example.docx'

    Сценарий: Пример чтения из файла и сравнения содержимого
      Когда я создаю docx файл 'artifacts/files/example.docx' содержащий:
        """
        HELLO WORLD
        """
      Затем я открываю docx файл 'artifacts/files/example.docx'
      Тогда содержимое файла соответствует:
        """
        HELLO WORLD
        """
      Тогда содержимое файла не соответствует:
        """
        EXAMPLE
        """
      Затем я удаляю файл 'artifacts/files/example.docx'

  Правило: Парсинг

    Сценарий: Пример парсинга docx файла по шаблону
      #Note: больше примеров в модуле filesystem
      Когда я разбираю docx файл 'artifacts/parsers/example.docx' используя шаблон 'artifacts/templates/parser.xml'
      Тогда результат содержит '3' записи
      И сумма '$..localAS' в результате равна '196653.0'
      И в результате:
        | $.[0].localAS  | равно         | 65551                |
        | $.[0].remoteAS | больше        | 65550                |
        | $.[0].remoteIp | соответствует | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | меньше        | 1                    |
        | $.[2].status   | не равно      | 0                    |
        | $.[2].routerId | содержит      | 192                  |