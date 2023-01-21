# language: ru

@example
Функция: Консольные команды

  Данная функция представляет собой примеры выполнения команд локально

  Правило: Запуск команд

    Сценарий: Пример запуска команды
      Когда я запускаю команду 'echo "example"' локально

    Сценарий: Пример запуска команды с установленным максимальным временем выполнения
      #Note: после выполнения команды максимальное время выполнения сбрасывается по умолчанию(module.command.timeout). можно сконфигурировать в файле setting.properties
      Когда я устанавливаю максимальное время выполнения команды '10' секунд
      Тогда я запускаю команду 'echo "example"' локально

    Сценарий: Пример запуска команды каждую секунду в течении указанного времени до появления содержимого
      Затем менее чем через '10' секунд вывод локальной команды 'sleep 1 && printf "first\nsecond" && sleep 10 && echo "four"' содержит 'second'

    @force
    Сценарий: Пример запуска команды в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 1 && echo "second"' в фоне
      Тогда я завершаю команду запущенную в фоне

    @force
    Сценарий: Пример запуска нескольких команд в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 2 && echo "second"' в фоне
      И сохраняю идентификатор команды в переменную 'FIRST'

      Тогда я запускаю локальную команду 'sleep 1 && echo "three" && sleep 5 && echo "four"' в фоне
      И сохраняю идентификатор команды в переменную 'SECOND'

      Затем я завершаю команду запущенную в фоне с идентификатором '${FIRST}'
      Затем я завершаю команду запущенную в фоне с идентификатором '${SECOND}'

  Правило: Результат выполнения команд

    Сценарий: Пример сохранения результата выполнения команды в переменную
      Когда я запускаю команду 'echo "first"' локально
      Тогда сохраняю результат выполнения команды в переменную 'RESULT'
      Тогда '${RESULT}' равно 'first'

    Сценарий: Пример проверки кода выхода команды
      Когда я запускаю команду 'echo "first"' локально
      Тогда код выхода команды должен быть '0'

    Сценарий: Пример проверки результата выполнения команды
      Когда я запускаю команду 'printf "first\nsecond"' локально
      Тогда результат выполнения команды соответствует:
        """
        first
        second
        """

    @force
    Сценарий: Пример проверки результата выполнения команды в фоне без прерывания процесса
      Когда я запускаю локальную команду 'sleep 1 && echo "first"' в фоне
      Тогда я жду '1' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '0'
      И результат выполнения команды соответствует:
        """
        first
        """

    @force
    Сценарий: Пример проверки результата выполнения команды в фоне с прерыванием процесса через 1 секунду
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 1 && echo "second"' в фоне
      Тогда я жду '1' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '143'
      И результат выполнения команды соответствует:
        """
        first
        """

    @force
    Сценарий: Пример проверки результата нескольких команд в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 2 && echo "second"' в фоне
      И сохраняю идентификатор команды в переменную 'FIRST'

      Тогда я запускаю локальную команду 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' в фоне
      И сохраняю идентификатор команды в переменную 'SECOND'

      Пусть я жду '3' секунды

      Затем я завершаю команду запущенную в фоне с идентификатором '${FIRST}'
      Тогда код выхода команды должен быть '0'
      И результат выполнения команды соответствует:
        """
        first
        second
        """

      Затем я завершаю команду запущенную в фоне с идентификатором '${SECOND}'
      Тогда код выхода команды должен быть '143'
      И результат выполнения команды соответствует:
        """
        three
        four
        """
