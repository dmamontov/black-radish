# language: en

@example @sql
Feature: Database *.csv

  This function is an example of working with a database based on *.csv.
  more details https://github.com/simoc/csvjdbc/blob/master/docs/doc.md

  Rule: Connection

    Scenario: Connection example
      When i connect to database 'csv' by path 'artifacts/database'
      Then close database connection

    Scenario: An example of connecting to a database in a zip archive
      When i connect to database 'csv' by path 'artifacts/database/example.zip'
      Then close database connection

    Scenario: Connection example with the parameters
      When i connect to database 'csv' by path 'artifacts/database' with the parameters:
        | charset | UTF-8 |
      Then close database connection

  Rule: Queries

    Scenario: Query execution example
      When i connect to database 'csv' by path 'artifacts/database'
      Then execute database query 'SELECT * FROM example;'
      Then close database connection

    Scenario: An example of executing a request from a file
      When i connect to database 'csv' by path 'artifacts/database'
      Then execute database query from 'artifacts/sql/select.sql'
      Then close database connection

    Scenario: An example of checking the existence of a table
      When i connect to database 'csv' by path 'artifacts/database'
      Then table 'example' exists in database
      And table 'test' does not exists in database
      Then close database connection

  Rule: Result

    Scenario: Example of query result comparison
      When i connect to database 'csv' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |

      Then execute database query 'SELECT * FROM example;'
      And result of the database query is:
        | localAS | remoteAS | remoteIp    | routerId  | status | uptime   |
        | 65551   | 65551    | 10.10.10.10 | 192.0.2.1 | 5      | 10:37:12 |
        | 65551   | 65552    | 10.10.100.1 | 192.0.2.1 | 0      | 10:38:27 |
        | 65551   | 65553    | 10.100.10.9 | 192.0.2.1 | 1      | 07:55:38 |

      Then execute database query 'SELECT localAS, remoteIp, status FROM example WHERE status=0;'
      And result of the database query is:
        | localAS | remoteIp    | status |
        | 65551   | 10.10.100.1 | 0      |

      Then close database connection

    Scenario: An example of parsing the result of a query
      When i connect to database 'csv' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |

      Then execute database query 'SELECT * FROM example;'
      And result contains '3' records
      And sum of '$..remoteAS' results in '196656.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |

      Then close database connection

    Scenario: An example of parsing the result of a query and saving it to a variable
      When i connect to database 'csv' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |

      Then execute database query 'SELECT * FROM example;'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' равно '65551'

      Then close database connection



