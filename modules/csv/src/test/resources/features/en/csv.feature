# language: en

@examples @module:csv @module:core @module:filesystem @file
Feature: Csv

  This module provides the ability to work with csv files.

  .. note:: More examples in the ``Filesystem`` module

  Rule: Creation

    Scenario: An example of creating a csv file
      When i create a csv file 'artifacts/tmp/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then file 'artifacts/tmp/example.csv' exist
      Then i delete the file 'artifacts/tmp/example.csv'

    Scenario: An example of creating a csv file with custom separators
      Given i set csv delimiter ';'
      Given i set csv line separator '\r\n'
      When i create a csv file 'artifacts/tmp/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then file 'artifacts/tmp/example.csv' exist
      Then i delete the file 'artifacts/tmp/example.csv'

  Rule: Reading

    Scenario: Example of reading from a file
      #File: src/test/resources/artifacts/files/example_first.csv
      When i open csv file 'artifacts/files/example_first.csv'

    Scenario: Example of reading from a file with headers on the first line
      #File: src/test/resources/artifacts/files/example_second.csv
      When i open csv file 'artifacts/files/example_second.csv' with headers in first line

    Scenario: Example of reading from a file with headers set
      #File: src/test/resources/artifacts/files/example_first.csv
      When i open csv file 'artifacts/files/example_first.csv' with headers:
        | localAS | remoteAS | remoteIp | routerId | status | uptime |

  Rule: Comparison

    Scenario: Example of reading from a file and comparing contents
      #File: src/test/resources/artifacts/files/example_first.csv
      When i open csv file 'artifacts/files/example_first.csv'
      Then csv file content match:
        | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
        | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
        | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |
      And csv file content does not match:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |

    Scenario: An example of reading csv file with custom separators
      #File: src/test/resources/artifacts/files/example_third.csv
      Given i set csv delimiter '\t'
      Given i set csv line separator '\r\n'
      When i open csv file 'artifacts/files/example_third.csv'
      Then csv file content match:
        | 65551 | 65551 | 10.10.10.10 | 192.0.2.1 | 5 | 10:37:12 |
        | 65551 | 65552 | 10.10.100.1 | 192.0.2.1 | 0 | 10:38:27 |
        | 65551 | 65553 | 10.100.10.9 | 192.0.2.1 | 1 | 07:55:38 |

  Rule: CsvConverter

    Scenario: An example of checking the number of records in csv
      #File: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      Then result contains '3' records

    Scenario: An example of checking the number of records in csv for the minimum number
      #File: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      Then result contains at least '2' records

    Scenario: An example of summing values in csv without headers by json path
      #File: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      Then sum of '$.*.[1]' results in '196656.0'

    Scenario: An example of summing values in csv with headers by json path
      #File: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-second.json
      When i open csv file 'artifacts/files/example_second.csv' with headers in first line
      Then sum of '$..remoteAS' results in '196656.0'

    Scenario: An example of checking values in csv without headers by json path
      #Note: All checks are supported similarly to variables
      #File: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      And as a result:
        | $.[0].[0] | is                | 65551                |
        | $.[0].[1] | is higher than    | 65550                |
        | $.[0].[2] | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].[4] | is lower than     | 1                    |
        | $.[2].[4] | is different from | 0                    |
        | $.[2].[3] | contains          | 192                  |

    Scenario: An example of checking values in csv with headers by json path
      #Note: All checks are supported similarly to variables
      #File: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-second.json
      When i open csv file 'artifacts/files/example_second.csv' with headers in first line
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |

    Scenario: An example of saving csv values to a variable as json
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      Then i save the result in a variable 'JSON'
      And '${JSON}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-first.json}'

    Scenario: An example of saving the result of csv values without headers by json path to a variable
      #File:bin: src/test/resources/artifacts/files/example_first.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_first.csv'
      Then i save the result '$.[0].[0]' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'

    Scenario: An example of saving the result of csv values with headers by json path to a variable
      #File:bin: src/test/resources/artifacts/files/example_second.csv
      #File:bin: src/test/resources/artifacts/files/converted-first.json
      When i open csv file 'artifacts/files/example_second.csv' with headers in first line
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'