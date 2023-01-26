# language: en

@example
Feature: Console commands

  This feature is an example of executing commands locally

  Rule: Command execution

    Scenario: Example of running a command
      When i run command 'echo "example"' locally

    Scenario: An example of running a command with a set maximum execution time
      #Note: after the command is executed, the maximum execution time is reset to the default (module.command.timeout). can be configured in the setting.properties file
      When i set the maximum command execution time to '10' seconds
      Then i run command 'echo "example"' locally

    Scenario: An example of running a command locally every second for the specified time until the content appears
      Then in less than '10' seconds the output of the local command 'sleep 1 && printf "first\nsecond" && sleep 10 && echo "four"' contains 'second'

    @force
    Scenario: An example of running a command in the background
      When i run local command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i terminate a command running in background

    @force
    Scenario: An example of running several commands in the background
      When i run local command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run local command 'sleep 1 && echo "three" && sleep 5 && echo "four"' in background
      And save the command ID in the variable 'SECOND'

      Then i terminate a command running in background with id '${FIRST}'
      Then i terminate a command running in background with id '${SECOND}'

  Rule: Result of command execution

    Scenario: An example of saving the result of a command execution to a variable
      When i run command 'echo "first"' locally
      Then save the result of the command execution in the variable 'RESULT'
      Then '${RESULT}' is 'first'

    Scenario: An example of checking the exit code of a command
      When i run command 'echo "first"' locally
      Then command exit code must be '0'

    Scenario: An example of checking the result of a command
      When i run command 'printf "first\nsecond"' locally
      Then command result is:
        """
        first
        second
        """

    @force
    Scenario: An example of checking the result of running a command in the background without interrupting the process.
      When i run local command 'sleep 1 && echo "first"' in background
      Then i wait '2' second
      Then i terminate a command running in background
      Then command exit code must be '0'
      And command result is:
        """
        first
        """

    @force
    Scenario: An example of checking the result of a command execution in the background with the process interrupting after 1 second
      When i run local command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '143'
      And command result is:
        """
        first
        """

    @force
    Scenario: An example of checking the result of several commands in the background
      When i run local command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run local command 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' in background
      And save the command ID in the variable 'SECOND'

      Given i wait '3' seconds

      Then i terminate a command running in background with id '${FIRST}'
      Then command exit code must be '0'
      And command result is:
        """
        first
        second
        """

      Then i terminate a command running in background with id '${SECOND}'
      Then command exit code must be '143'
      And command result is:
        """
        three
        four
        """

  Rule: Result parsing

    Scenario: An example of parsing json
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.json'
      Then i save the result in a variable 'JSON'
      Then i run command 'printf '${JSON}'' locally
      Then i check the result according to the scheme 'artifacts/schemas/json.json'
      And result contains '3' records
      And sum of '$..remoteAS' results in '196656.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |

    Scenario: An example of parsing xml
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.xml'
      Then i save the result in a variable 'XML'
      Then i run command 'printf '${XML}'' locally
      Then i check the result according to the scheme 'artifacts/schemas/xml.xsd'
      And sum of '$.root.element.*.remoteAS' results in '196656.0'
      And as a result:
        | $.root.element.[0].localAS  | is                | 65551                |
        | $.root.element.[0].remoteAS | is higher than    | 65550                |
        | $.root.element.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.root.element.[1].status   | is lower than     | 1                    |
        | $.root.element.[2].status   | is different from | 0                    |
        | $.root.element.[2].routerId | contains          | 192                  |

    Scenario: An example of parsing yaml
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.yaml'
      Then i save the result in a variable 'YAML'
      Then i run command 'printf '\"${YAML}"\'' locally
      Then i check the result according to the scheme 'artifacts/schemas/yaml.json'
      And result contains '3' records
      And sum of '$..remoteAS' results in '196656.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |

    Scenario: An example of parsing a result by a template
      #Note: more https://github.com/sonalake/utah-parser, https://github.com/json-path/JsonPath
      When i parse the file 'artifacts/parsers/example.txt' using the template 'artifacts/templates/parser.xml'
      Then i save the result in a variable 'TXT'
      Then i run command 'printf '${TXT}'' locally
      And parse the result of the command using a template 'artifacts/templates/parser.xml'
      Then result contains '3' records
      And sum of '$..localAS' results in '196653.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |