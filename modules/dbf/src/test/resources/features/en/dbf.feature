# language: en

@examples @module:dbf @module:core @database @file
Feature: DbfDatabase

  This module provides the ability to work with a dbf database.

  .. note:: The module uses a driver `CsvJdbc <https://github.com/simoc/csvjdbc>`_

  .. note:: The database connection is closed automatically when the script ends.

  .. warning:: The database is read-only.

  Rule: Database

    Scenario: Database connection example
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'

    Scenario: An example of connecting to a database in a zip archive
      #File:bin: src/test/resources/artifacts/database/example.zip
      When i connect to database 'dbf' by path 'artifacts/database/example.zip'

    Scenario: An example of connecting to a database with parameters
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | charset | UTF-8 |

    Scenario: An example of closing a connection to a database
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'
      Then close database connection

    Scenario: An example of executing a database query
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'
      Then execute query 'SELECT * FROM example;'

    Scenario: An example of executing a database query, variant 2
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'
      Then execute query:
        """
        SELECT * FROM example;
        """

    Scenario: An example of executing database queries from a file
      #Warning: When executing queries from a file, the result will only be converted for the last query in the file.
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      When i connect to database 'dbf' by path 'artifacts/database'
      Then execute query from 'artifacts/sql/example.sql'

    Scenario: An example of setting the maximum query execution time
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'
      Then i set the maximum query execution time to '2' seconds
      And execute query 'SELECT * FROM example;'

    Scenario: An example of checking a table for existence
      #File:bin: src/test/resources/artifacts/database/example.dbf
      When i connect to database 'dbf' by path 'artifacts/database'
      Then table 'undefined' does not exists
      And table 'example' exists

    Scenario: An example of checking if the result of a query matches
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      And query result matches:
        | LOCALAS | REMOTEAS | REMOTEIP    | ROUTERID  | STATUS | UPTIME   |
        | 65551   | 65551    | 10.10.10.10 | 192.0.2.1 | 5      | 10:37:12 |
        | 65551   | 65552    | 10.10.100.1 | 192.0.2.1 | 0      | 10:38:27 |
        | 65551   | 65553    | 10.100.10.9 | 192.0.2.1 | 1      | 07:55:38 |

  Rule: DatabaseConverter

    Scenario: An example of checking the number of records in the result of a query
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      And result contains '3' records

    Scenario: An example of checking the number of records in the result of a query for the minimum number
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      And result contains at least '2' records

    Scenario: An example of summarizing values as a result of a request by json path
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      And sum of '$..REMOTEAS' results in '196656.0'

    Scenario: An example of checking the result of transforming the query result by json path
      #Note: All checks are supported similarly to variables
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      And as a result:
        | $.[0].LOCALAS  | is             | 65551 |
        | $.[0].REMOTEAS | is higher than | 65550 |
        | $.[1].LOCALAS  | matches        | ^\d+$ |
        | $.[1].REMOTEAS | contains       | 65    |

    Scenario: An example of saving the query result to a variable as json
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      Then i save the result in a variable 'JSON'
      And '${JSON}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-sql.json}'

    Scenario: An example of saving the result of a request by json path to a variable
      #File:bin: src/test/resources/artifacts/database/example.dbf
      #File:sql: src/test/resources/artifacts/sql/example.sql
      #File:json: src/test/resources/artifacts/files/converted-sql.json
      When i connect to database 'dbf' by path 'artifacts/database' with the parameters:
        | columnTypes | Int,Int,String,String,Int,String |
      Then execute query from 'artifacts/sql/example.sql'
      Then i save the result '$.[0].LOCALAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'
