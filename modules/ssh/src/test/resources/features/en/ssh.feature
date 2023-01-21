# language: en

@example @ssh
Feature: SSH

  This feature is an example of executing commands via ssh

  Rule: Connection

    Scenario: Example ssh connection with password
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with password '${SSH_PASSWORD}'
      Then i close the ssh connection

    Scenario: An example of connecting via ssh with a key
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then i close the ssh connection

  Rule: Command execution

    Scenario: Example of running a command
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then i run command 'echo "example"' over ssh
      Then i close the ssh connection

    Scenario: An example of running a command over ssh every second for the specified time until the content appears
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then in less than '10' seconds the output of ssh command 'echo "example"' contains 'example'
      Then i close the ssh connection

    @force
    Scenario: An example of running a command in the background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      When i run ssh command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i terminate a command running in background

    @force
    Scenario: An example of running several commands in the background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'

      When i run ssh command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run ssh command 'sleep 1 && echo "three" && sleep 5 && echo "four"' in background
      And save the command ID in the variable 'SECOND'

      Then i terminate a command running in background with id '${FIRST}'
      Then i terminate a command running in background with id '${SECOND}'

  Rule: The result of executing commands

    Scenario: An example of saving the result of a command execution to a variable
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then i run command 'echo "first"' over ssh
      And save the result of the command execution in the variable 'RESULT'
      And '${RESULT}' is 'first'
      Then i close the ssh connection

    Scenario: An example of checking the exit code of a command
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then i run command 'echo "first"' over ssh
      And command exit code must be '0'
      Then i close the ssh connection

    Scenario: An example of checking the result of a command
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      When i run command 'ls -a' over ssh
      Then command result is:
        """
        .
        ..
        .ssh
        logs
        ssh_host_keys
        sshd.pid
        """

    @force
    Scenario: An example of checking the result of running a command in the background without interrupting the process.
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      When i run ssh command 'sleep 1 && echo "first"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '0'
      And command result is:
        """
        first
        """

    @force
    Scenario: An example of checking the result of a command execution in the background with the process interrupting after 1 second
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      When i run ssh command 'sleep 1 && echo "first" && sleep 1 && echo "second"' in background
      Then i wait '1' second
      Then i terminate a command running in background
      Then command exit code must be '143'
      And command result is:
        """
        first
        """

    @force
    Scenario: An example of checking the result of several commands in the background
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'

      When i run ssh command 'sleep 1 && echo "first" && sleep 2 && echo "second"' in background
      And save the command ID in the variable 'FIRST'

      Then i run ssh command 'sleep 2 && printf "three\nfour" && sleep 5 && echo "five"' in background
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

  Rule: SFTP

    Scenario: An example of uploading a file via sftp
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'

      Then i upload file 'artifacts/upload/example.txt' by sftp as '/app/example.txt'

      Then i run command '[ -f "/app/example.txt" ] && echo true || echo false' over ssh
      And save the result of the command execution in the variable 'RESULT'
      Then '${RESULT}' is 'true'

      Then i run command 'rm -rf /app/example.txt' over ssh

      Then i close the ssh connection

    Scenario: An example of downloading a file via sftp
      Given i open ssh connection to '${SSH_HOST}:${SSH_PORT}' as '${SSH_USERNAME}' with key '${SSH_PEM}'
      Then i run command 'echo "DOWNLOAD" > /app/example.txt' over ssh

      Then i download file '/app/example.txt' by sftp as 'artifacts/download/example.txt'

      Then i run command 'rm -rf /app/download.txt' over ssh

      Then i close the ssh connection