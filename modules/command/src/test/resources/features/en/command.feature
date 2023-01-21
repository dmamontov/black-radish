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
      Then i wait '1' second
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