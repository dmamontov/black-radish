# language: ru

@examples @module:core @database @converter
Функция: Core

  Данный модуль является основным и предоставляет реализацию базового функционала.

  Правило: Ожидание

    Сценарий: Пример ожидания
      Допустим я жду '1' секунду

  Правило: Переменные

    Сценарий: Пример сохранения значения в переменную
      Если я сохраняю 'example' в переменной 'VAR'

    Сценарий: Примеры проверки на равенство
      Если '100' равно '100'
      И 'example' равно 'example'

    Сценарий: Примеры проверки на неравенство
      Если '100' не равно '200'
      И 'example' не равно 'ex'

    Сценарий: Примеры проверки по регулярному выражению
      Если '200' соответствует '^\d+'
      И 'example' соответствует '^[A-Za-z]+'

    Сценарий: Примеры проверки вхождения подстроки
      Если '100' содержит '10'
      И 'example' содержит 'ex'

    Сценарий: Примеры сравнения чисел
      Если '100.1' больше '100'
      И '200' меньше '200.2'
      И '0xAF' меньше '176'

    Сценарий: Пример получения переменной из common.properties
      #Note: К примеру для получения переменной из test.properties выполните ``env=test ./gradlew cleanTest core:test``
      #File:ini: src/test/resources/configuration/common.properties
      Если '${FOUR}' равно '4'

  Правило: Интерполяторы
    #Note: Подробнее: `StringSubstitutor <https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/StringSubstitutor.html>`_

    Сценарий: Пример использования base64 энкодера
      Если я сохраняю '${base64Encoder:example}' в переменной 'VAR'
      Тогда '${VAR}' равно 'ZXhhbXBsZQ=='

    Сценарий: Пример использования java константы
      Если я сохраняю '${const:java.awt.event.KeyEvent.VK_ENTER}' в переменной 'VAR'
      Тогда '${VAR}' равно '10'

    Сценарий: Пример использования текущей даты
      Если я сохраняю '${date:yyyy-MM-dd}' в переменной 'VAR'
      Тогда '${VAR}' соответствует '^\d{4}-\d{2}-\d{2}$'

    Сценарий: Пример использования системной env
      Если я сохраняю '${env:PATH}' в переменной 'VAR'
      Тогда '${VAR}' не равно 'undefined'

    Сценарий: Пример использования содержимого файла
      #File: src/test/resources/artifacts/files/hello.txt
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/hello.txt}' в переменной 'VAR'
      Тогда '${VAR}' равно 'Hello World!'

    Сценарий: Пример использования java
      Если я сохраняю '${java:version}' в переменной 'VAR'
      Тогда '${VAR}' содержит 'version'

    Сценарий: Пример использования localhost
      Если я сохраняю '${localhost:canonical-name}' в переменной 'VAR'
      Тогда '${VAR}' не равно '0.0.0.0'

    Сценарий: Пример использования переменной из properties файла
      #File:ini: src/test/resources/configuration/test.properties
      Если я сохраняю '${properties:src/test/resources/configuration/test.properties::FIVE}' в переменной 'VAR'
      Тогда '${VAR}' равно '5'

    Сценарий: Пример использования системной переменной
      Если я сохраняю '${sys:user.dir}' в переменной 'VAR'
      Тогда '${VAR}' не равно '/etc'

    Сценарий: Пример использования URL декодера
      Если я сохраняю '${urlDecoder:my+example%21}' в переменной 'VAR'
      Тогда '${VAR}' равно 'my example!'

    Сценарий: Пример использования URL энкодера
      Если я сохраняю '${urlEncoder:my example!}' в переменной 'VAR'
      Тогда '${VAR}' равно 'my+example%21'

    Сценарий: Пример использования XML XPath
      #File:xml: src/test/resources/artifacts/files/example.xml
      Если я сохраняю '${xml:src/test/resources/artifacts/files/example.xml:/root/element[1]/localAS}' в переменной 'VAR'
      Тогда '${VAR}' равно '65551'

    Сценарий: Пример использования приведения строки к верхнему регистру
      Если я сохраняю '${upper:example}' в переменной 'VAR'
      Тогда '${VAR}' равно 'EXAMPLE'

    Сценарий: Пример использования приведения строки к нижнему регистру
      Если я сохраняю '${lower:EXAMPLE}' в переменной 'VAR'
      Тогда '${VAR}' равно 'example'

    Сценарий: Пример использования математической формулы
      #Note: Подробнее: `exp4j <https://www.objecthunter.net/exp4j/>`_
      Если я сохраняю '${math:3*sin(3.14)-2/(2.3-2)}' в переменной 'VAR'
      Тогда '${VAR}' равно '-6.66188870791721'

    Сценарий: Пример использования генератора фейковых данных
      #Note: Подробнее: `faker <https://github.com/DiUS/java-faker>`_
      Если я сохраняю '${faker:internet.email_address}' в переменной 'VAR'
      Тогда '${VAR}' соответствует '^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$'

    Сценарий: Пример использования kotlin evaluate
      #Warning: Выражения пишутся на языке kotlin
      Если я сохраняю '${eval:listOf(1, 2).joinToString("-")}' в переменной 'VAR'
      Тогда '${VAR}' равно '1-2'

    Сценарий: Пример использования рекурсивной замены
      Если я сохраняю 'example' в переменной 'VAR'
      Тогда '${upper:${VAR}}' равно 'EXAMPLE'

  Правило: Условный оператор
    #Warning: Выражения пишутся на языке kotlin

    Сценарий: Пример использования - Если, Конец Если
      Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
      * Если '${NUMBER} == 1.0':
        Тогда '${NUMBER}' равно '1.0'
      * Конец Если

    Сценарий: Пример использования - Если, Иначе Если, Конец Если
      Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
      * Если '${NUMBER} == 1.0':
        Тогда '${NUMBER}' равно '1.0'
      * Иначе Если '${NUMBER} == 2.0':
        Тогда '${NUMBER}' равно '2.0'
      * Конец Если

    Сценарий: Пример использования - Если, Иначе, Конец Если
      Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
      * Если '${NUMBER} == 1.0':
        Тогда '${NUMBER}' равно '1.0'
      * Иначе:
        Тогда '${NUMBER}' больше '1.0'
      * Конец Если

    Сценарий: Пример использования - Если, Иначе Если, Иначе, Конец Если
      Допустим я сохраняю '${faker:number.random_double '0','1','5'}' в переменной 'NUMBER'
      * Если '${NUMBER} == 1.0':
        Тогда '${NUMBER}' равно '1.0'
      * Иначе Если '${NUMBER} == 2.0':
        Тогда '${NUMBER}' равно '2.0'
      * Иначе:
        Тогда '${NUMBER}' больше '2.0'
      * Конец Если

  Правило: Циклы

    Сценарий: Пример циклической генерации из списка
      * Цикл из 'first,second,three':
        * я сохраняю '${loop.value}' в переменной 'VAR'
      * Конец Цикла

    Сценарий: Пример циклической генерации из списка, включая переменную
      Когда я сохраняю '1' в переменной 'NUMBER'
      * Цикл из '${NUMBER},first,second':
        * я сохраняю '${loop.value}' в переменной 'VAR'
      * Конец Цикла

    Сценарий: Пример циклической генерации шагов циклом со счётчиком
      * Цикл от '1' до '3':
        * я сохраняю '${loop.value}' в переменной 'NUMBER'
      * Конец Цикла

    Сценарий: Пример циклической генерации шагов циклом со счётчиком в обратном порядке
      * Цикл от '3' до '1':
        * я сохраняю '${loop.value}' в переменной 'NUMBER'
      * Конец Цикла

  Правило: Подключение сценария

    Сценарий: Пример подключения сценария из текущей функции
      Если я сохраняю '1' в переменной 'VAR'
      Тогда '${VAR}' равно '1'
      Если я подключаю сценарий 'Пример сохранения значения в переменную'
      Тогда '${VAR}' равно 'example'

    Сценарий: Пример подключения сценария из другой функции
      #File:gherkin: src/test/resources/features/ru/include.feature
      Если я сохраняю '1' в переменной 'VAR'
      Тогда '${VAR}' равно '1'
      Если я подключаю сценарий 'Пример создания переменной' из функции 'classpath:features/ru/include.feature'
      Тогда '${VAR}' равно '3'

  Правило: Базы данных
    #Warning: Для популярных баз данных есть отдельные модули. Используйте их.
    #Note: Соединение с базой данных закрывается автоматически по завершению выполнения сценария.

    Сценарий: Пример подключения к базе данных
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'

    Сценарий: Пример подключения к базе данных с параметрами
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:' с параметрами:
        | encoding | utf8 |

    Сценарий: Пример закрытия соединения к базе данных
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      Затем закрываю соединение с базой данных

    Сценарий: Пример выполнения запроса к базе данных
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример выполнения запроса к базе данных, вариант 2
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос:
        """
        CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT);
        DROP TABLE IF EXISTS example;
        """

    Сценарий: Пример выполнения запросов к базе данных из файла
      #Warning: При выполнении запросов из файла результат будет преобразован только для последнего запроса в файле
      #File:sql: src/test/resources/artifacts/sql/example.sql
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример установки максимального времени выполнения запроса
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И устанавливаю максимальное время выполнения запроса '2' секунды
      И выполняю запрос 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример проверки таблицы на существование
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      Затем выполняю запрос 'DROP TABLE IF EXISTS example'
      И таблица 'example' не существует
      И выполняю запрос 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      И таблица 'example' существует
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример проверки соответствия результата запроса
      #File:sql: src/test/resources/artifacts/sql/example.sql
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      Тогда результат запроса соответствует:
        | localAS | remoteAS |
        | 65551   | 65551    |
        | 65551   | 65552    |
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример проверки соответствия результата запроса на запись
      #File:sql: src/test/resources/artifacts/sql/example.sql
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'INSERT INTO example (localAS, remoteAS) VALUES (65552, 65553), (65553, 65554)'
      Тогда результат запроса соответствует:
        | updateCount |
        | 2           |
      И выполняю запрос 'DROP TABLE IF EXISTS example'

  Правило: Сравнение результата
    #Note: Поддерживаются все операции сравнения

    Сценарий: Пример сравнения оригинала результата
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда результат равен '${file:UTF-8:src/test/resources/artifacts/files/example.json}'
      И результат не равен 'undefined'
      И результат содержит 'localAS'

    Сценарий: Пример сравнения оригинала результата, вариант 2
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда результат равен:
        """
        ${file:UTF-8:src/test/resources/artifacts/files/example.json}
        """
      И результат не равен:
        """
        undefined
        """
      И результат содержит:
        """
        localAS
        """

  Правило: JsonConverter

    Сценарий: Пример проверки количество записей в json на равенство
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в json на минимальное количество
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации json по json схеме
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-json.json'

    Сценарий: Пример суммирования значений в json по json path
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования json по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Сценарий: Пример сохранения результата преобразования json в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Сценарий: Пример сохранения результата преобразования json по json path в переменную
      #File:json: src/test/resources/artifacts/files/example.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.json}' в переменной 'JSON'
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: YamlConverter

    Сценарий: Пример проверки количество записей в yaml на равенство
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда результат содержит '2' записи

    Сценарий: Пример проверки количество записей в yaml на минимальное количество
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда результат содержит не менее '2' записей

    Сценарий: Пример валидации yaml по json схеме
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-yaml.json'

    Сценарий: Пример суммирования значений в yaml по json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования yaml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Сценарий: Пример сохранения результата преобразования yaml в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Сценарий: Пример сохранения результата преобразования yaml по json path в переменную
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' в переменной 'YAML'
      Тогда я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: XmlConverter

    Сценарий: Пример валидации xml по xsd схеме
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда я проверяю содержимое по схеме 'artifacts/files/schema-xml.xsd'

    Сценарий: Пример суммирования значений в xml по json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда сумма '$.root.element.*.remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования xml по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда в результате:
        | $.root.element.[0].localAS  | равно         | 65551 |
        | $.root.element.[0].remoteAS | больше        | 65550 |
        | $.root.element.[1].localAS  | соответствует | ^\d+$ |
        | $.root.element.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда я сохраняю оригинал в переменной 'ORIGINAL'
      Затем '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Сценарий: Пример сохранения результата преобразования xml в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда я сохраняю результат в переменной 'RESULT'
      Затем '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Сценарий: Пример сохранения результата преобразования xml по json path в переменную
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' в переменной 'XML'
      Тогда я сохраняю результат '$.root.element.[0].localAS' в переменной 'LOCAL_AS'
      Затем '${LOCAL_AS}' равно '65551'

  Правило: TemplateConverter

    Сценарий: Пример проверки количество записей преобразованных по шаблону на равенство
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит '2' записи

    Сценарий: Пример проверки количество записей преобразованных по шаблону на минимальное количество
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем результат содержит не менее '2' записей

    Сценарий: Пример суммирования значений в преобразованных данных по шаблону и json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем сумма '$..remoteAS' в результате равна '131103.0'

    Сценарий: Пример проверки результата преобразования данных по шаблону и json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |

    Сценарий: Пример сохранения оригинала преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю оригинал в переменной 'ORIGINAL'
      И '${ORIGINAL}' равно '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат в переменной 'RESULT'
      И '${RESULT}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Сценарий: Пример сохранения результата преобразованных данных по шаблону и json path в переменную
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Если я сохраняю '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' в переменной 'TXT'
      Тогда я преобразовываю результат по шаблону 'artifacts/files/template.xml'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'

  Правило: DatabaseConverter

    Сценарий: Пример проверки количество записей в результате запроса
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      И результат содержит '2' записи
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример проверки количество записей в результате запроса на минимальное количество
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      И результат содержит не менее '2' записей
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример суммирования значений в результате запроса по json path
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      И сумма '$..remoteAS' в результате равна '131103.0'
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример проверки результата преобразования результата запроса по json path
      #Note: Поддерживаются все проверки аналогично переменным
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      И в результате:
        | $.[0].localAS  | равно         | 65551 |
        | $.[0].remoteAS | больше        | 65550 |
        | $.[1].localAS  | соответствует | ^\d+$ |
        | $.[1].remoteAS | содержит      | 65    |
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример сохранения результата запроса в переменную как json
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      Затем я сохраняю результат в переменной 'JSON'
      И '${JSON}' равно '${file:UTF-8:src/test/resources/artifacts/files/converted-sql.json}'
      И выполняю запрос 'DROP TABLE IF EXISTS example'

    Сценарий: Пример сохранения результата запроса по json path в переменную
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      Когда я подключаю jdbc драйвер 'org.sqlite.JDBC'
      Тогда я подключаюсь к базе данных 'jdbc:sqlite::memory:'
      И выполняю запрос из 'artifacts/sql/example.sql'
      И выполняю запрос 'SELECT * FROM example'
      Затем я сохраняю результат '$.[0].localAS' в переменной 'LOCAL_AS'
      И '${LOCAL_AS}' равно '65551'
      И выполняю запрос 'DROP TABLE IF EXISTS example'
