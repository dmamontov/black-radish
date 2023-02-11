# language: en

@examples @module:ssh @module:core @module:command @module:filesystem
Feature: Ssh

  This module provides the ability to run commands via ssh and work with files via sftp.

  Rule: Connection
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: Example ssh connection with password
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'

    Scenario: An example of connecting via ssh with a key
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'

    Scenario: An example of connecting via ssh with a key and passphrase
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PASS_PEM}' and passphrase '${SSH_PASSPHRASE}'

    Scenario: An example of closing a connection
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      Then i close the ssh connection

  Rule: Running
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of running a command
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run command 'echo "example"' over ssh

    Scenario: An example of running a command with a set maximum execution time
      #Note: After the command is executed, the maximum execution time is reset to the default (module.command.timeout).
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i set the maximum command execution time to '10' seconds
      Then i run command 'echo "example"' over ssh

    Scenario: An example of running a command until the content appears within the specified time
      #Warning: If the command completed before the specified time, it will be restarted.
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      Then in less than '10' seconds the output of ssh command 'sleep 1 && printf "hello\nworld" && sleep 10' contains 'world'

  @force
  Rule: Running in the background
    #Note: At the end of the script execution, the command will be completed automatically.
    #Note: When using the ``@force`` tag, the command will be force-completed when the script ends.
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of running a command in the background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background

    Scenario: An example of completing a command launched in background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "hello" && sleep 1 && echo "world"' in background

    Scenario: An example of running multiple commands in background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "hello" && sleep 2 && echo "world"' in background
      Then save the command ID in the variable 'FIRST'

      When i run ssh command 'sleep 1 && echo "hello" && sleep 5 && echo "world"' in background
      Then save the command ID in the variable 'SECOND'

      Then i terminate a command running in background with id '${FIRST}'
      Then i terminate a command running in background with id '${SECOND}'

  Rule: Comparing the result
    #Note: All comparison operations are supported
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of checking the exit code of a command
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run command 'echo "first"' over ssh
      Then command exit code must be '0'

    Scenario: An example of comparing the original result
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run command 'echo "200"' over ssh
      Then result is '200'
      And result is different from '100'
      And result matches '^\d+'
      And result contains '20'
      And result is higher than '100'
      And result is lower than '300'

    Scenario: An example of comparing the original result, variant 2
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run command 'echo "200"' over ssh
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
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of checking the result of executing the in background command without interrupting the process
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "first"' in background
      Then i wait '2' second
      Then i terminate a command running in background
      Then command exit code must be '0'
      And result is:
        """
        first
        """

    Scenario: An example of checking the result of executing the in background command with interrupting the process after 1 second
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '143'
      And result is:
        """
        first
        """

    Scenario: An example of checking the result of several commands in background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      When i run ssh command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run ssh command 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' in background
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

  Rule: SFTP
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of uploading a file via sftp
      #File src/test/resources/artifacts/sftp/upload.txt
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      Then i upload file 'artifacts/sftp/upload.txt' by sftp as '/app/upload.txt'
      Then i run command 'cat /app/upload.txt' over ssh
      And result is 'UPLOAD'
      Then i run command 'rm -rf /app/upload.txt' over ssh

    Scenario: An example of downloading a file via sftp
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      Then i run command 'echo "DOWNLOAD" > /app/example.txt' over ssh
      Then i download file '/app/example.txt' by sftp as 'artifacts/sftp/download.txt'
      And file 'artifacts/sftp/download.txt' exist
      And i delete the file 'artifacts/sftp/download.txt'
      Then i run command 'rm -rf /app/download.txt' over ssh

  Rule: JsonConverter
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of checking the number of records in json for equality
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then result contains '2' records

    Scenario: An example of checking the number of records in json for the minimum number
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then result contains at least '2' records

    Scenario: An example of json validation against json schema
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then i check the content against the schema 'artifacts/files/schema-json.json'

    Scenario: An example of summing values in json by json path
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of json conversion by json path
      #Note: All checks are supported similarly to variables
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original json to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Scenario: An example of saving the result of json conversion to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Scenario: An example of saving the result of converting json by json path to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.json}'' over ssh
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: YamlConverter
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of checking the number of records in yaml for equality
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then result contains '2' records

    Scenario: An example of checking the number of records in yaml for the minimum number
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then result contains at least '2' records

    Scenario: An example of yaml validation against json schema
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then i check the content against the schema 'artifacts/files/schema-yaml.json'

    Scenario: An example of summing values in yaml by json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of the yaml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original yaml to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Scenario: An example of saving the result of yaml conversion to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Scenario: An example of saving the result of converting yaml by json path to a variable.
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'' over ssh
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: XmlConverter
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: Example of xml validation according to xsd schema
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then i check the content against the schema 'artifacts/files/schema-xml.xsd'

    Scenario: An example of summing values in xml by json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then sum of '$.root.element.*.remoteAS' results in '131103.0'

    Scenario: An example of checking the result of xml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then as a result:
        | $.root.element.[0].localAS  | is             | 65551 |
        | $.root.element.[0].remoteAS | is higher than | 65550 |
        | $.root.element.[1].localAS  | matches        | ^\d+$ |
        | $.root.element.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original xml to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Scenario: An example of saving the result of xml transformation into a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Scenario: An example of saving the result of converting xml by json path to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'' over ssh
      Then i save the result '$.root.element.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: TemplateConverter
    #File:ini: src/test/resources/configuration/common.properties

    Scenario: An example of checking the number of records converted by template for equality
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains '2' records

    Scenario: An example of checking the number of records converted by template for the minimum number
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains at least '2' records

    Scenario: An example of summing values in converted data by template and json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of data conversion by template and json path
      #Note: All checks are supported similarly to variables
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save original in a variable 'ORIGINAL'
      And '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Scenario: An example of saving the result of converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result in a variable 'RESULT'
      And '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Scenario: An example of saving the result of converted data by template and json path into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_DEFAULT_PEM}'
      When i run command 'printf -- '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'' over ssh
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'