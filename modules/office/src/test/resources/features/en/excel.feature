# language: en

@examples
Feature: Files *.xls, *.xlsx

  This feature is an example of working with files *.xls, *.xlsx

  Rule: File creation

    Scenario: An example of creating *.xls file
      When i create an empty xls file 'artifacts/files/example.xls'
      Then file 'artifacts/files/example.xls' exist
      Then i delete the file 'artifacts/files/example.xls'

    Scenario: Пример создания *.xlsx fileа
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then file 'artifacts/files/example.xlsx' exist
      Then i delete the file 'artifacts/files/example.xlsx'

  Rule: Reading a file

    Scenario: Example of reading from a file
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then in xlsx add a new sheet 'one' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      Then i open xlsx file 'artifacts/files/example.xlsx'
      Then i delete the file 'artifacts/files/example.xlsx'

  Rule: File modification

    Scenario: Adding a sheet of content
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then in xlsx add a new sheet 'one' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      And in xlsx add a new sheet 'two' containing:
        | #5 | example5 | 105 |
        | #4 | example4 | 104 |
        | #3 | example3 | 103 |
        | #2 | example2 | 102 |
        | #1 | example1 | 101 |
      Then file 'artifacts/files/example.xlsx' exist
      And i delete the file 'artifacts/files/example.xlsx'

    Scenario: Adding a sheet with content and specifying types
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then in xlsx add a new sheet 'one' containing:
        | #1 | example1 | 101{:Int} | true{:Boolean}  |
        | #2 | example2 | 102{:Int} | false{:Boolean} |
        | #3 | example3 | 103{:Int} | false{:Boolean} |
        | #4 | example4 | 104{:Int} | true{:Boolean}  |
        | #5 | example5 | 105{:Int} | false{:Boolean} |
      Then file 'artifacts/files/example.xlsx' exist
      And i delete the file 'artifacts/files/example.xlsx'

    Scenario: Writing a value to a cell
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then in xlsx add a new sheet 'one' containing:
        | #1 | example1 | 101{:Int} | true{:Boolean}  |
        | #2 | example2 | 102{:Int} | false{:Boolean} |
        | #3 | example3 | 103{:Int} | false{:Boolean} |
        | #4 | example4 | 104{:Int} | true{:Boolean}  |
        | #5 | example5 | 105{:Int} | false{:Boolean} |
      And in xlsx sheet 'one' write 'one' in the cell 'D6'
      And in xlsx sheet 'one' write '500{:Int}' in the cell 'D7'
      And in xlsx sheet 'one' write the formula 'SUM(C1:C5)' in the cell 'C6'
      Then file 'artifacts/files/example.xlsx' exist
      Then i delete the file 'artifacts/files/example.xlsx'

    Scenario: Merging cells
      When i create an empty xlsx file 'artifacts/files/example.xlsx'
      Then in xlsx add a new sheet 'one' containing:
        | #1 | example1 | 101 |
        | #2 | example2 | 102 |
        | #3 | example3 | 103 |
        | #4 | example4 | 104 |
        | #5 | example5 | 105 |
      And in xlsx sheet 'one' merge cells from 'A4' to 'C5'
      Then file 'artifacts/files/example.xlsx' exist
      Then i delete the file 'artifacts/files/example.xlsx'


  Rule: Parsing

    Scenario: An example of parsing an xlsx file
      #Note: more examples in the filesystem module
      When i open xlsx file 'artifacts/parsers/example.xlsx'
      Then sum of '$.[0]..B' results in '196656.0'
      #Note: $.[list].[row].column
      And as a result:
        | $.[0].[0].A | is                | 65551.0              |
        | $.[0].[0].B | is higher than    | 65550.0              |
        | $.[0].[0].C | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[0].[1].E | is lower than     | 1                    |
        | $.[0].[2].E | is different from | 0                    |
        | $.[0].[2].D | contains          | 192                  |
      