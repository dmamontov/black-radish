# language: en

@example
Feature: Console commands

  This function is an example of executing commands locally

  Rule: Command execution

    Scenario: Example of running a command
      When i run command 'echo "example"' locally

    Scenario: An example of running a command with a set maximum execution time
      #Note: after the command is executed, the maximum execution time is reset to the default (module.command.timeout). can be configured in the setting.properties file
      When i set the maximum command execution time to '10' seconds
      Then i run command 'echo "example"' locally

    Scenario: An example of running a command locally every second for the specified time until the content appears
      Then in less than '10' seconds the output of the local command 'echo "example"' contains 'example'

  Rule: Result of command execution

    Scenario: An example of saving the result of a command execution to a variable
      When i run command 'echo "first"' locally
      Then i save the result of the command execution in the variable 'RESULT'
      Then '${RESULT}' is 'first'

    Scenario: An example of checking the exit code of a command
      When i run command 'echo "first"' locally
      Then i check that the exit code of the command is '0'
