# language: en

@example
Feature: Files *.csv

  This function is an example of working with *.csv files

  Rule: File creation

    Scenario: An example of creating a *.csv file
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then file 'artifacts/files/example.csv' exist
      Then i delete the file 'artifacts/files/example.csv'

    Scenario: An example of creating a *.csv file with custom separators
      Given i set csv delimiter ';'
      Given i set csv line separator '\r\n'
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then file 'artifacts/files/example.csv' exist
      Then i delete the file 'artifacts/files/example.csv'

  Rule: Reading a file

    Scenario: Example of reading from a file
      #Note: more examples in the filesystem module
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i open csv file 'artifacts/files/example.csv'
      Then i delete the file 'artifacts/files/example.csv'

    Scenario: Example of reading from a file with headers on the first line
      #Note: more examples in the filesystem module
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #  | text     | number |
        | #1 | example1 | 101    |
        | #2 | example2 | 102    |
        | #3 | example3 | 103    |
        | #4 | example4 | 104    |
        | #5 | example5 | 105    |
      Then i open csv file 'artifacts/files/example.csv' with headers in first line
      Then i delete the file 'artifacts/files/example.csv'

    Scenario: Example of reading from a file with headers set
      #Note: more examples in the filesystem module
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i open csv file 'artifacts/files/example.csv' with headers:
        | # | text | number |
      Then i delete the file 'artifacts/files/example.csv'

    Scenario: Example of reading from a file and comparing contents
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i open csv file 'artifacts/files/example.csv' with headers:
        | # | text | number |
      And csv file content match:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      And csv file content does not match:
        | #1 | example1 | 101 |
        | #5 | example5 | 105 |
      Then i delete the file 'artifacts/files/example.csv'

    Scenario: An example of reading *.csv file with custom separators
      Given i set csv delimiter '\t'
      Given i set csv line separator '\r\n'
      #Note: more examples in the filesystem module
      When i create a csv file 'artifacts/files/example.csv' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i open csv file 'artifacts/files/example.csv'
      And csv file content match:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i delete the file 'artifacts/files/example.csv'

  Rule: Parsing

    Scenario: An example of parsing a csv file without headers
      #Note: more examples in the filesystem module
      When i open csv file 'artifacts/parsers/example_first.csv'
      Then result contains '3' records
      And sum of '$.*.[1]' results in '196656.0'
      And as a result:
        | $.[0].[0] | is                | 65551                |
        | $.[0].[1] | is higher than    | 65550                |
        | $.[0].[2] | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].[4] | is lower than     | 1                    |
        | $.[2].[4] | is different from | 0                    |
        | $.[2].[3] | contains          | 192                  |

    Scenario: An example of parsing a csv file with headers in first line
      #Note: more examples in the filesystem module
      When i open csv file 'artifacts/parsers/example_second.csv' with headers in first line
      Then i check the result according to the scheme 'artifacts/schemas/csv.json'
      And result contains '3' records
      And sum of '$..remoteAS' results in '196656.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |

    Scenario: An example of parsing a csv file with headers set
      #Note: more examples in the filesystem module
      When i open csv file 'artifacts/parsers/example_first.csv' with headers:
        | localAS | remoteAS | remoteIp | routerId | status | uptime |
      Then i check the result according to the scheme 'artifacts/schemas/csv.json'
      And result contains '3' records
      And sum of '$..remoteAS' results in '196656.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |
