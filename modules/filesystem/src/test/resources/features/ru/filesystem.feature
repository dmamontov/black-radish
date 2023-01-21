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
        | tree                           |
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
      Тогда я читаю файл 'artifacts/files/hello.txt'
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример чтения из файла и сравнивания содержимого
      Когда я создаю файл 'artifacts/files/hello.txt' содержащий:
        """
        HELLO WORLD
        """
      Тогда я читаю файл 'artifacts/files/hello.txt'
      Тогда содержимое файла соответствует:
        """
        HELLO WORLD
        """
      Тогда содержимое файла не соответствует:
        """
        EXAMPLE
        """
      Затем я удаляю файл 'artifacts/files/hello.txt'

    Сценарий: Пример сравнения содержимого файлов
      Пусть я создаю пустой файл 'artifacts/files/example.txt'
      Пусть я создаю пустой файл 'artifacts/files/example2.txt'
      Тогда содержимое файлов 'artifacts/files/example.txt' и 'artifacts/files/example2.txt' равно
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