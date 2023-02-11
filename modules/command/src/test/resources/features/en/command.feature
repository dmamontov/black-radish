# language: en

@examples @module:command @module:core
Feature: Command

  This module provides the ability to work with local commands.

  Rule: Running

    Scenario: An example of running a command
      When i run command 'echo "example"' locally

    Scenario: An example of running a command with a set maximum execution time
      #Note: After the command is executed, the maximum execution time is reset to the default (module.command.timeout).
      When i set the maximum command execution time to '10' seconds
      Then i run command 'echo "example"' locally

    Scenario: An example of running a command until the content appears within the specified time
      #Warning: If the command completed before the specified time, it will be restarted.
      Then in less than '10' seconds the output of the local command 'sleep 1 && printf "hello\nworld" && sleep 10' contains 'world'

  @force
  Rule: Running in the background
    #Note: At the end of the script execution, the command will be completed automatically.
    #Note: When using the ``@force`` tag, the command will be force-completed when the script ends.

    Scenario: An example of running a command in the background
      When i run local command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background

    Scenario: An example of completing a command launched in background
      When i run local command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background
      Then i terminate a command running in background

    Scenario: An example of running multiple commands in background
      When i run local command 'sleep 1 && echo "hello" && sleep 2 && echo "world"' in background
      Then save the command ID in the variable 'FIRST'

      When i run local command 'sleep 1 && echo "hello" && sleep 5 && echo "world"' in background
      Then save the command ID in the variable 'SECOND'

      Then i terminate a command running in background with id '${FIRST}'
      Then i terminate a command running in background with id '${SECOND}'

  Rule: Comparing the result
    #Note: All comparison operations are supported

    Scenario: An example of checking the exit code of a command
      When i run command 'echo "first"' locally
      Then command exit code must be '0'

    Scenario: An example of comparing the original result
      When i run command 'echo "200"' locally
      Then result is '200'
      And result is different from '100'
      And result matches '^\d+'
      And result contains '20'
      And result is higher than '100'
      And result is lower than '300'

    Scenario: An example of comparing the original result, variant 2
      When i run command 'echo "200"' locally
      Then result is:
        """
        200
        """
      And result is different from:
        """
        100
        """
      And result matches:
        """
        ^\d+
        """
      And result contains:
        """
        20
        """
      And result is higher than:
        """
        100
        """
      And result is lower than:
        """
        300
        """

  @force
  Rule: Comparing the result of commands run in background
    #Note: All comparison operations are supported

    Scenario: An example of checking the result of executing the in background command without interrupting the process
      When i run local command 'sleep 1 && echo "first"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '0'
      And result is:
        """
        first
        """

    Scenario: An example of checking the result of executing the in background command with interrupting the process after 1 second
      When i run local command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '143'
      And result is:
        """
        first
        """

    Scenario: An example of checking the result of several commands in background
      When i run local command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run local command 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' in background
      And save the command ID in the variable 'SECOND'

      Given i wait '3' seconds

      Then i terminate a command running in background with id '${FIRST}'
      Then command exit code must be '0'
      And result is:
        """
        first
        second
        """

      Then i terminate a command running in background with id '${SECOND}'
      Then command exit code must be '143'
      And result is:
        """
        three
        four
        """

  Rule: JsonConverter

    Scenario: An example of checking the number of records in json for equality
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then result contains '2' records

    Scenario: An example of checking the number of records in json for the minimum number
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then result contains at least '2' records

    Scenario: An example of json validation against json schema
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then i check the content against the schema 'artifacts/files/schema-json.json'

    Scenario: An example of summing values in json by json path
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of json conversion by json path
      #Note: All checks are supported similarly to variables
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original json to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Scenario: An example of saving the result of json conversion to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Scenario: An example of saving the result of converting json by json path to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' locally
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: YamlConverter

    Scenario: An example of checking the number of records in yaml for equality
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then result contains '2' records

    Scenario: An example of checking the number of records in yaml for the minimum number
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then result contains at least '2' records

    Scenario: An example of yaml validation against json schema
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then i check the content against the schema 'artifacts/files/schema-yaml.json'

    Scenario: An example of summing values in yaml by json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of the yaml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original yaml to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Scenario: An example of saving the result of yaml conversion to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Scenario: An example of saving the result of converting yaml by json path to a variable.
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' locally
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: XmlConverter

    Scenario: Example of xml validation according to xsd schema
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then i check the content against the schema 'artifacts/files/schema-xml.xsd'

    Scenario: An example of summing values in xml by json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then sum of '$.root.element.*.remoteAS' results in '131103.0'

    Scenario: An example of checking the result of xml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then as a result:
        | $.root.element.[0].localAS  | is             | 65551 |
        | $.root.element.[0].remoteAS | is higher than | 65550 |
        | $.root.element.[1].localAS  | matches        | ^\d+$ |
        | $.root.element.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original xml to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Scenario: An example of saving the result of xml transformation into a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Scenario: An example of saving the result of converting xml by json path to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' locally
      Then i save the result '$.root.element.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: TemplateConverter

    Scenario: An example of checking the number of records converted by template for equality
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains '2' records

    Scenario: An example of checking the number of records converted by template for the minimum number
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains at least '2' records

    Scenario: An example of summing values in converted data by template and json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of data conversion by template and json path
      #Note: All checks are supported similarly to variables
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save original in a variable 'ORIGINAL'
      And '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Scenario: An example of saving the result of converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result in a variable 'RESULT'
      And '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Scenario: An example of saving the result of converted data by template and json path into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' locally
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'