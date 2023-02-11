# language: ru

@examples @module:command @module:core
Функция: Command

  Данный модуль предоставляет возможность работы с локальными командами.

  Правило: Запуск

    Сценарий: Пример запуска команды
      Когда я запускаю команду 'echo "example"' локально

    Сценарий: Пример запуска команды с установленным максимальным временем выполнения
      #Note: После выполнения команды максимальное время выполнения сбрасывается по умолчанию(module.command.timeout).
      Когда я устанавливаю максимальное время выполнения команды '10' секунд
      Тогда я запускаю команду 'echo "example"' локально

    Сценарий: Пример запуска команды до появления содержимого в течении указанного времени
      #Warning: Если команда завершилась раньше указанного времени, она будет перезапущена.
      Затем менее чем через '10' секунд вывод локальной команды 'sleep 1 && printf "hello\nworld" && sleep 10' содержит 'world'

  @force
  Правило: Запуск в фоне
    #Note: По окончанию выполнения сценария команда будет завершена автоматически.
    #Note: При использовании тега ``@force``, по окончанию выполнения сценария команда будет завершена принудительно.

    Сценарий: Пример запуска команды в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне

    Сценарий: Пример завершения команды запущенной в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне
      Тогда я завершаю команду запущенную в фоне

    Сценарий: Пример запуска нескольких команд в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 2 && echo "world"' в фоне
      Тогда сохраняю идентификатор команды в переменную 'FIRST'

      Когда я запускаю локальную команду 'sleep 1 && echo "hello" && sleep 5 && echo "world"' в фоне
      Тогда сохраняю идентификатор команды в переменную 'SECOND'

      Затем я завершаю команду запущенную в фоне с идентификатором '${FIRST}'
      Затем я завершаю команду запущенную в фоне с идентификатором '${SECOND}'

  Правило: Сравнение результата
    #Note: Поддерживаются все операции сравнения

    Сценарий: Пример проверки кода выхода команды
      Когда я запускаю команду 'echo "first"' локально
      Тогда код выхода команды должен быть '0'

    Сценарий: Пример сравнения оригинала результата
      Когда я запускаю команду 'echo "200"' локально
      Тогда результат равен '200'
      И результат не равен '100'
      И результат соответствует '^\d+'
      И результат содержит '20'
      И результат больше '100'
      И результат меньше '300'

    Сценарий: Пример сравнения оригинала результата, вариант 2
      Когда я запускаю команду 'echo "200"' локально
      Тогда результат равен:
        """
        200
        """
      И результат не равен:
        """
        100
        """
      И результат соответствует:
        """
        ^\d+
        """
      И результат содержит:
        """
        20
        """
      И результат больше:
        """
        100
        """
      И результат меньше:
        """
        300
        """

  @force
  Правило: Сравнение результата команд запущенных в фоне
    #Note: Поддерживаются все операции сравнения

    Сценарий: Пример проверки результата выполнения команды в фоне без прерывания процесса
      Когда я запускаю локальную команду 'sleep 1 && echo "first"' в фоне
      Тогда я жду '1' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '0'
      И результат равен:
        """
        first
        """

    Сценарий: Пример проверки результата выполнения команды в фоне с прерыванием процесса через 1 секунду
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 1 && echo "second"' в фоне
      Тогда я жду '1' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '143'
      И результат равен:
        """
        first
        """

    Сценарий: Пример проверки результата нескольких команд в фоне
      Когда я запускаю локальную команду 'sleep 1 && echo "first" && sleep 2 && echo "second"' в фоне
      И сохраняю идентификатор команды в переменную 'FIRST'

      Тогда я запускаю локальную команду 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' в фоне
      И сохраняю идентификатор команды в переменную 'SECOND'

      Пусть я жду '3' секунды

      Затем я завершаю команду запущенную в фоне с идентификатором '${FIRST}'
      Тогда код выхода команды должен быть '0'
      И результат равен:
        """
        first
        second
        """

      Затем я завершаю команду запущенную в фоне с идентификатором '${SECOND}'
      Тогда код выхода команды должен быть '143'
      И результат равен:
        """
        three
        four
        """

  Правило: JsonConverter

    Сценарий: Пример проверки количество записей в json на равенство
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в json на минимальное количество
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации json по json схеме
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-json.json'

    Сценарий: Пример суммирования значений в json по json path
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования json по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Сценарий: Пример сохранения результата преобразования json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Сценарий: Пример сохранения результата преобразования json по json path в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' локально
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: YamlConverter

    Сценарий: Пример проверки количество записей в yaml на равенство
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в yaml на минимальное количество
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации yaml по json схеме
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-yaml.json'

    Сценарий: Пример суммирования значений в yaml по json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования yaml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Сценарий: Пример сохранения результата преобразования yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Сценарий: Пример сохранения результата преобразования yaml по json path в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' локально
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: XmlConverter

    Сценарий: Пример валидации xml по xsd схеме
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-xml.xsd'

    Сценарий: Пример суммирования значений в xml по json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда сумма '$.root.element.*.remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования xml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда в результате:
        | $.root.element.[0].localAS  | равно         | 65551 |
        | $.root.element.[0].remoteAS | больше        | 65550 |
        | $.root.element.[1].localAS  | соответствует | ^\d+$ |
        | $.root.element.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Сценарий: Пример сохранения результата преобразования xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Сценарий: Пример сохранения результата преобразования xml по json path в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' локально
      Тогда я сохраняю результат '$.root.element.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: TemplateConverter

    Сценарий: Пример проверки количество записей преобразованных по шаблону на равенство
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит '2' записи

    Сценарий: Пример проверки количество записей преобразованных по шаблону на минимальное количество
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в преобразованных данных по шаблону и json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования данных по шаблону и json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю оригинал в переменной 'ORIGINAL'
      И '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат в переменной 'RESULT'
      И '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону и json path в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' локально
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
