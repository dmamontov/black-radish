# language: en

@examples @module:core @database @converter
Feature: Core

  This module is the main one and provides the implementation of the basic functionality.

  Rule: Waiting

    Scenario: Wait example
      Given i wait '1' second

  Rule: Variables

    Scenario: An example of saving a value to a variable
      Given i save 'example' in variable 'VAR'

    Scenario: Examples of checking for equality
      Then '100' is '100'
      And 'example' is 'example'

    Scenario: Examples of checking for inequality
      Then '100' is different from '200'
      And 'example' is different from 'ex'

    Scenario: Examples of checking by regular expression
      Then '200' matches '^\d+'
      And 'example' matches '^[A-Za-z]+'

    Scenario: Examples of checking the occurrence of a substring
      Then '100' contains '10'
      And 'example' contains 'ex'

    Scenario: Examples of comparing numbers
      Then '100.1' is higher than '100'
      And '200' is lower than '200.2'
      And '0xAF' is lower than '176'

    Scenario: Example of getting a variable from common.properties
      #Note: For example, to get a variable from test.properties, run ``env=test ./gradlew cleanTest core:test``
      #File:ini: src/test/resources/configuration/common.properties
      Then '${FOUR}' is '4'

  Rule: Interpolators
    #Note: More: `StringSubstitutor <https://commons.apache.org/proper/commons-text/apidocs/org/apache/commons/text/StringSubstitutor.html>`_

    Scenario: An example of using base64 encoder
      When i save '${base64Encoder:example}' in variable 'VAR'
      Then '${VAR}' is 'ZXhhbXBsZQ=='

    Scenario: An example of using a java constant
      When i save '${const:java.awt.event.KeyEvent.VK_ENTER}' in variable 'VAR'
      Then '${VAR}' is '10'

    Scenario: An example of using the current date
      When i save '${date:yyyy-MM-dd}' in variable 'VAR'
      Then '${VAR}' matches '^\d{4}-\d{2}-\d{2}$'

    Scenario: An example of using the system env
      When i save '${env:PATH}' in variable 'VAR'
      Then '${VAR}' is different from 'undefined'

    Scenario: An example of using the contents of a file
      #File: src/test/resources/artifacts/files/hello.txt
      When i save '${file:UTF-8:src/test/resources/artifacts/files/hello.txt}' in variable 'VAR'
      Then '${VAR}' is 'Hello World!'

    Scenario: An example of using java
      When i save '${java:version}' in variable 'VAR'
      Then '${VAR}' contains 'version'

    Scenario: An example of using localhost
      When i save '${localhost:canonical-name}' in variable 'VAR'
      Then '${VAR}' is different from '0.0.0.0'

    Scenario: An example of using a variable from the properties file
      #File:ini: src/test/resources/configuration/test.properties
      When i save '${properties:src/test/resources/configuration/test.properties::FIVE}' in variable 'VAR'
      Then '${VAR}' is '5'

    Scenario: An example of using a system variable
      When i save '${sys:user.dir}' in variable 'VAR'
      Then '${VAR}' is different from '/etc'

    Scenario: An example of using the URL decoder
      When i save '${urlDecoder:my+example%21}' in variable 'VAR'
      Then '${VAR}' is 'my example!'

    Scenario: An example of using the URL encoder
      When i save '${urlEncoder:my example!}' in variable 'VAR'
      Then '${VAR}' is 'my+example%21'

    Scenario: An example of using XML XPath
      #File:xml: src/test/resources/artifacts/files/example.xml
      When i save '${xml:src/test/resources/artifacts/files/example.xml:/root/element[1]/localAS}' in variable 'VAR'
      Then '${VAR}' is '65551'

    Scenario: An example of using uppercase string conversion
      When i save '${upper:example}' in variable 'VAR'
      Then '${VAR}' is 'EXAMPLE'

    Scenario: An example of using lowercase string conversion
      When i save '${lower:EXAMPLE}' in variable 'VAR'
      Then '${VAR}' is 'example'

    Scenario: An example of using a mathematical formula
      #Note: More `exp4j <https://www.objecthunter.net/exp4j/>`_
      When i save '${math:3*sin(3.14)-2/(2.3-2)}' in variable 'VAR'
      Then '${VAR}' is '-6.66188870791721'

    Scenario: An example of using a fake data generator
      #Note: More `faker <https://github.com/DiUS/java-faker>`_
      When i save '${faker:internet.email_address}' in variable 'VAR'
      Then '${VAR}' matches '^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$'

    Scenario: An example of using kotlin evaluate
      #Warning: Expressions are written in the kotlin language
      When i save '${eval:listOf(1, 2).joinToString("-")}' in variable 'VAR'
      Then '${VAR}' is '1-2'

    Scenario: An example of using recursive replacement
      When i save 'example' in variable 'VAR'
      Then '${upper:${VAR}}' is 'EXAMPLE'

  Rule: Conditional operator
    #Warning: Expressions are written in the kotlin language

    Scenario: Usage example - If, End If
      Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
      * If '${NUMBER} == 1.0':
        Then '${NUMBER}' is '1.0'
      * End If

    Scenario: Usage example - If, Else If, End If
      Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
      * If '${NUMBER} == 1.0':
        Then '${NUMBER}' is '1.0'
      * Else If '${NUMBER} == 2.0':
        Then '${NUMBER}' is '2.0'
      * End If

    Scenario: Usage example - If, Else, End If
      Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
        * If '${NUMBER} == 1.0':
      Then '${NUMBER}' is '1.0'
        * Else:
      Then '${NUMBER}' is higher than '1.0'
        * End If

    Scenario: Usage example - If, Else If, Else, End If
      Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'
      * If '${NUMBER} == 1.0':
        Then '${NUMBER}' is '1.0'
      * Else If '${NUMBER} == 2.0':
        Then '${NUMBER}' is '2.0'
      * Else:
        Then '${NUMBER}' is higher than '2.0'
      * End If

  Rule: Loops

    Scenario: An example of cyclic generation from a list
      * Loop in 'first,second,three':
        * i save '${loop.value}' in variable 'VAR'
      * End Loop

    Scenario: An example of cyclic generation from a list
      Given i save '1' in variable 'NUMBER'
      * Loop in '${NUMBER},first,second':
        * i save '${loop.value}' in variable 'VAR'
      * End Loop

    Scenario: An example of cyclic generation of steps by a cycle with a counter
      * Loop from '1' to '3':
        * i save '${loop.value}' in variable 'NUMBER'
      * End Loop

    Scenario: An example of cyclic generation of steps by a loop with a counter in reverse order
      * Loop from '3' to '1':
        * i save '${loop.value}' in variable 'NUMBER'
      * End Loop

  Rule: Include scenario

    Scenario: An example of include a scenario from the current feature
      Given i save '1' in variable 'VAR'
      Then '${VAR}' is '1'
      Given i include scenario 'An example of saving a value to a variable'
      Then '${VAR}' is 'example'

    Scenario: An example of include a scenario from another feature
      #File:gherkin: src/test/resources/features/en/include.feature
      Given i save '1' in variable 'VAR'
      Then '${VAR}' is '1'
      Given i include scenario 'An example of creating a variable' from feature 'classpath:features/en/include.feature'
      Then '${VAR}' is '3'

  Rule: Databases
    #Warning: For popular databases, there are separate modules. Use them.
    #Note: The database connection is closed automatically when the scenarios ends.

    Scenario: An example of a database connection
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'

    Scenario: An example of connecting to a database with parameters
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:' with parameters:
        | encoding | utf8 |

    Scenario: An example of closing a connection to a database
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      Then close database connection

    Scenario: An example of executing a database query
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of executing a database query, variant 2
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query:
        """
        CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT);
        DROP TABLE IF EXISTS example;
        """

    Scenario: An example of executing database queries from a file
      #Warning: When executing queries from a file, the result will only be converted for the last query in the file.
      #File:sql: src/test/resources/artifacts/sql/example.sql
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of setting the maximum query execution time
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And i set the maximum query execution time to '2' seconds
      And execute query 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of checking a table for existence
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      Then execute query 'DROP TABLE IF EXISTS example'
      And table 'example' does not exists
      And execute query 'CREATE TABLE IF NOT EXISTS example (localAS INT, remoteAS INT)'
      And table 'example' exists
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of checking if the result of a query matches
      #File:sql: src/test/resources/artifacts/sql/example.sql
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      Then query result matches:
        | localAS | remoteAS |
        | 65551   | 65551    |
        | 65551   | 65552    |
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of checking whether the result of a write request matches
      #File:sql: src/test/resources/artifacts/sql/example.sql
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'INSERT INTO example (localAS, remoteAS) VALUES (65552, 65553), (65553, 65554)'
      Then query result matches:
        | updateCount |
        | 2           |
      And execute query 'DROP TABLE IF EXISTS example'

  Rule: Comparing the result
    #Note: All comparison operations are supported

    Scenario: An example of comparing the original result
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then result is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'
      And result is different from 'undefined'
      And result contains 'localAS'

    Scenario: Example of comparison of the original result, variant 2
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then result is:
        """
        ${file:UTF-8:src/test/resources/artifacts/files/example.json}
        """
      And result is different from:
        """
        undefined
        """
      And result contains:
        """
        localAS
        """

  Rule: JsonConverter

    Scenario: An example of checking the number of records in json for equality
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then result contains '2' records

    Scenario: An example of checking the number of records in json for the minimum number
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then result contains at least '2' records

    Scenario: An example of json validation against json schema
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then i check the content against the schema 'artifacts/files/schema-json.json'

    Scenario: An example of summing values in json by json path
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of json conversion by json path
      #Note: All checks are supported similarly to variables
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original json to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Scenario: An example of saving the result of json conversion to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Scenario: An example of saving the result of converting json by json path to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.json}' in variable 'JSON'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: YamlConverter

    Scenario: An example of checking the number of records in yaml for equality
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then result contains '2' records

    Scenario: An example of checking the number of records in yaml for the minimum number
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then result contains at least '2' records

    Scenario: An example of yaml validation against json schema
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then i check the content against the schema 'artifacts/files/schema-yaml.json'

    Scenario: An example of summing values in yaml by json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of the yaml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original yaml to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Scenario: An example of saving the result of yaml conversion to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Scenario: An example of saving the result of converting yaml by json path to a variable.
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}' in variable 'YAML'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: XmlConverter

    Scenario: Example of xml validation according to xsd schema
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then i check the content against the schema 'artifacts/files/schema-xml.xsd'

    Scenario: An example of summing values in xml by json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then sum of '$.root.element.*.remoteAS' results in '131103.0'

    Scenario: An example of checking the result of xml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then as a result:
        | $.root.element.[0].localAS  | is             | 65551 |
        | $.root.element.[0].remoteAS | is higher than | 65550 |
        | $.root.element.[1].localAS  | matches        | ^\d+$ |
        | $.root.element.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original xml to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Scenario: An example of saving the result of xml transformation into a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Scenario: An example of saving the result of converting xml by json path to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.xml}' in variable 'XML'
      Then i save the result '$.root.element.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: TemplateConverter

    Scenario: An example of checking the number of records converted by template for equality
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains '2' records

    Scenario: An example of checking the number of records converted by template for the minimum number
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains at least '2' records

    Scenario: An example of summing values in converted data by template and json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of data conversion by template and json path
      #Note: All checks are supported similarly to variables
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save original in a variable 'ORIGINAL'
      And '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Scenario: An example of saving the result of converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result in a variable 'RESULT'
      And '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Scenario: An example of saving the result of converted data by template and json path into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i save '${file:UTF-8:src/test/resources/artifacts/files/example.txt}' in variable 'TXT'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'

  Rule: DatabaseConverter

    Scenario: An example of checking the number of records in the result of a query
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      And result contains '2' records
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of checking the number of records in the result of a query for the minimum number
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      And result contains at least '2' records
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of summarizing values as a result of a request by json path
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      And sum of '$..remoteAS' results in '131103.0'
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of checking the result of transforming the query result by json path
      #Note: All checks are supported similarly to variables
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      And as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of saving the query result to a variable as json
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      Then i save the result in a variable 'JSON'
      And '${JSON}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-sql.json}'
      And execute query 'DROP TABLE IF EXISTS example'

    Scenario: An example of saving the result of a request by json path to a variable
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i load jdbc driver 'org.sqlite.JDBC'
      Then i am connecting to database 'jdbc:sqlite::memory:'
      And execute query from 'artifacts/sql/example.sql'
      And execute query 'SELECT * FROM example'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'
      And execute query 'DROP TABLE IF EXISTS example'
