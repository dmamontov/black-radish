# language: ru

@examples @module:ssh @module:core @module:command @module:filesystem
Функция: Ssh

  Данный модуль предоставляет возможность запускать команд по ssh и работы с файлами по sftp.

  Правило: Подключение
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример подключение по ssh c паролем
      Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'

    Сценарий: Пример подключение по ssh c ключом
      Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с ключом '${SSH_DEFAULT_PEM}'

    Сценарий: Пример подключение по ssh c ключом и паролем для ключа
      Пусть я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с ключом '${SSH_PASS_PEM}' и паролем '${SSH_PASSPHRASE}'

    Сценарий: Пример закрытия соединения
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я закрываю ssh соединение

  Правило: Запуск
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример запуска команды
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'echo "example"' по ssh

    Сценарий: Пример запуска команды с установленным максимальным временем выполнения
      #Note: После выполнения команды максимальное время выполнения сбрасывается по умолчанию(module.command.timeout).
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я устанавливаю максимальное время выполнения команды '10' секунд
      Затем я запускаю команду 'echo "example"' по ssh

    Сценарий: Пример запуска команды до появления содержимого в течении указанного времени
      #Warning: Если команда завершилась раньше указанного времени, она будет перезапущена.
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда менее чем через '10' секунд вывод ssh команды 'sleep 1 && printf "hello\nworld" && sleep 10' содержит 'world'

  @force
  Правило: Запуск в фоне
    #Note: По окончанию выполнения сценария команда будет завершена автоматически.
    #Note: При использовании тега ``@force``, по окончанию выполнения сценария команда будет завершена принудительно.
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример запуска команды в фоне
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне

    Сценарий: Пример завершения команды запущенной в фоне
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "hello" && sleep 1 && echo "world"' в фоне
      Затем я завершаю команду запущенную в фоне

    Сценарий: Пример запуска нескольких команд в фоне
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "hello" && sleep 2 && echo "world"' в фоне
      И сохраняю идентификатор команды в переменную 'FIRST'

      Когда я запускаю ssh команду 'sleep 1 && echo "hello" && sleep 5 && echo "world"' в фоне
      Тогда сохраняю идентификатор команды в переменную 'SECOND'

      Затем я завершаю команду запущенную в фоне с идентификатором '${FIRST}'
      Затем я завершаю команду запущенную в фоне с идентификатором '${SECOND}'

  Правило: Сравнение результата
    #Note: Поддерживаются все операции сравнения
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример проверки кода выхода команды
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'echo "first"' по ssh
      Затем код выхода команды должен быть '0'

    Сценарий: Пример сравнения оригинала результата
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'echo "200"' по ssh
      Затем результат равен '200'
      И результат не равен '100'
      И результат соответствует '^\d+'
      И результат содержит '20'
      И результат больше '100'
      И результат меньше '300'

    Сценарий: Пример сравнения оригинала результата, вариант 2
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'echo "200"' по ssh
      Затем результат равен:
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
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример проверки результата выполнения команды в фоне без прерывания процесса
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "first"' в фоне
      И я жду '2' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '0'
      И результат равен:
        """
        first
        """

    Сценарий: Пример проверки результата выполнения команды в фоне с прерыванием процесса через 1 секунду
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "first" && sleep 1 && echo "second"' в фоне
      И я жду '1' секунды
      Затем я завершаю команду запущенную в фоне
      Тогда код выхода команды должен быть '143'
      И результат равен:
        """
        first
        """

    Сценарий: Пример проверки результата нескольких команд в фоне
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю ssh команду 'sleep 1 && echo "first" && sleep 2 && echo "second"' в фоне
      И сохраняю идентификатор команды в переменную 'FIRST'

      Тогда я запускаю ssh команду 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' в фоне
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

  Правило: Sftp
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример загрузки файла через sftp
      #File src/test/resources/artifacts/sftp/upload.txt
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я загружаю файл 'artifacts/sftp/upload.txt' по sftp как '/app/upload.txt'
      Когда я запускаю команду 'cat /app/upload.txt' по ssh
      Тогда результат равен 'UPLOAD'
      Затем я запускаю команду 'rm -rf /app/upload.txt' по ssh

    Сценарий: Пример скачивания файла через sftp
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Затем я запускаю команду 'echo "DOWNLOAD" > /app/download.txt' по ssh
      Затем я скачиваю файл '/app/download.txt' по sftp как 'artifacts/sftp/download.txt'
      Тогда файл 'artifacts/sftp/download.txt' существует
      Затем я удаляю файл 'artifacts/sftp/download.txt'
      Затем я запускаю команду 'rm -rf /app/download.txt' по ssh

  Правило: JsonConverter
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример проверки количество записей в json на равенство
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И результат содержит '2' записи

    Сценарий: Пример проверки количество записей в json на минимальное количество
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И результат содержит не менее '2' записей

    Сценарий: Пример валидации json по json схеме
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И я проверяю содержимое по схеме 'artifacts/files/schema-json.json'

    Сценарий: Пример суммирования значений в json по json path
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования json по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Сценарий: Пример сохранения результата преобразования json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Сценарий: Пример сохранения результата преобразования json по json path в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' по ssh
      И я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: YamlConverter
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример проверки количество записей в yaml на равенство
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И результат содержит '2' записи

    Сценарий: Пример проверки количество записей в yaml на минимальное количество
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И результат содержит не менее '2' записей

    Сценарий: Пример валидации yaml по json схеме
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И я проверяю содержимое по схеме 'artifacts/files/schema-yaml.json'

    Сценарий: Пример суммирования значений в yaml по json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования yaml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Сценарий: Пример сохранения результата преобразования yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Сценарий: Пример сохранения результата преобразования yaml по json path в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' по ssh
      И я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: XmlConverter
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример валидации xml по xsd схеме
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И я проверяю содержимое по схеме 'artifacts/files/schema-xml.xsd'

    Сценарий: Пример суммирования значений в xml по json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И сумма '$.root.element.*.remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования xml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И в результате:
        | $.root.element.[0].localAS  | равно         | 65551 |
        | $.root.element.[0].remoteAS | больше        | 65550 |
        | $.root.element.[1].localAS  | соответствует | ^\d+$ |
        | $.root.element.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Сценарий: Пример сохранения результата преобразования xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Сценарий: Пример сохранения результата преобразования xml по json path в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' по ssh
      И я сохраняю результат '$.root.element.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: TemplateConverter
    #File:ini: src/test/resources/configuration/common.properties

    Сценарий: Пример проверки количество записей преобразованных по шаблону на равенство
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит '2' записи

    Сценарий: Пример проверки количество записей преобразованных по шаблону на минимальное количество
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в преобразованных данных по шаблону и json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования данных по шаблону и json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю оригинал в переменной 'ORIGINAL'
      И '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат в переменной 'RESULT'
      И '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону и json path в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Когда я открываю ssh соединение к '${SSH_HOST}:${SSH_PORT}' от имени '${SSH_USERNAME}' с паролем '${SSH_PASSWORD}'
      Тогда я запускаю команду 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' по ssh
      И я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
