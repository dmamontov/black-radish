# language: en

@example
Feature: File system

  This feature is an example of working with the file system

  Rule: Directories

    Scenario: An example of creating a directory
      When i create directory 'artifacts/directories/tree/first/second'

    Scenario: An example of deleting a directory
      When i create directory 'artifacts/directories/tree/first/second'
      Then i delete the directory 'artifacts/directories/tree'

    Scenario: An example of checking the existence of a directory in resources
      When i create directory 'artifacts/directories/tree/first/second'
      Then directory 'artifacts/directories/tree/first/second' exist
      Then i delete the directory 'artifacts/directories/tree'

    Scenario: An example of checking the existence of a directory relative to the package root
      When directory 'src/test/resources/artifacts/directories' exist

    Scenario: An example of checking if a directory does not exist
      When directory 'src/undefined' does not exist

    Scenario: An example of checking with deleting directories
      When i create directory 'artifacts/directories/tree/first/second'
      Then directory 'artifacts/directories/tree/first/second' exist
      Then i delete the directory 'artifacts/directories/tree'
      Then directory 'artifacts/directories/tree/first/second' does not exist

    Scenario: An example of copying a directory
      When i create directory 'artifacts/directories/tree/first/second'
      When i copy directory 'artifacts/directories/tree/first/second' to 'artifacts/directories/tree/first/three'
      Then directory 'artifacts/directories/tree/first/three' exist
      Then i delete the directory 'artifacts/directories/tree'

    Scenario: An example of moving a directory
      When i create directory 'artifacts/directories/tree/first/second'
      When i move directory 'artifacts/directories/tree/first/second' to 'artifacts/directories/tree/first/three'
      Then directory 'artifacts/directories/tree/first/second' does not exist
      And directory 'artifacts/directories/tree/first/three' exist
      Then i delete the directory 'artifacts/directories/tree'

    Scenario: An example of moving a directory to another directory
      When i create directory 'artifacts/directories/tree/first/second'
      When i create directory 'artifacts/directories/tree/first/three'
      When i move directory 'artifacts/directories/tree/first/second' to directory 'artifacts/directories/tree/first/three'
      Then directory 'artifacts/directories/tree/first/second' does not exist
      And directory 'artifacts/directories/tree/first/three/second' exist
      Then i delete the directory 'artifacts/directories/tree'

    Scenario: An example of getting and comparing a list of files
      When i create an empty file 'artifacts/files/example.txt'
      When i create an empty file 'artifacts/files/example2.txt'
      When i get the directory structure 'artifacts/files'
      Then directory contain:
        | .gitkeep     |
        | example.txt  |
        | example2.txt |
      Then i delete the file 'artifacts/files/example.txt'
      And i delete the file 'artifacts/files/example2.txt'

    Scenario: An example of getting and comparing a list of files recursively
      When i create directory 'artifacts/directories/tree/first/second'
      When i create directory 'artifacts/directories/tree/first/three'
      When i create an empty file 'artifacts/directories/tree/first/second/example.txt'
      When i create an empty file 'artifacts/directories/tree/first/three/example2.txt'
      When i get the directory structure 'artifacts/directories' recursively
      Then directory contain:
        | tree/first/second/.black-radish |
        | tree/first/second/example.txt   |
        | tree/first/three/.black-radish  |
        | tree/first/second               |
        | tree/first/three                |
        | tree/first                      |
        | tree                            |
      Then directory does not contain:
        | undefined |
      Then i delete the directory 'artifacts/directories/tree'

  Rule: Files

    Scenario: An example of creating an empty file
      When i create an empty file 'artifacts/files/example.txt'

    Scenario: An example of deleting a file
      When i create an empty file 'artifacts/files/example.txt'
      Then i delete the file 'artifacts/files/example.txt'

    Scenario: An example of creating a file with arbitrary content
      When i create a file 'artifacts/files/hello.txt' containing:
        """
        HELLO WORLD
        """
      Then i delete the file 'artifacts/files/hello.txt'

    Scenario: An example of checking the existence of a file in resources
      When i create an empty file 'artifacts/files/example.txt'
      Then file 'artifacts/files/example.txt' exist
      Then i delete the file 'artifacts/files/example.txt'

    Scenario: An example of checking the existence of a file relative to the package root
      When file 'src/test/resources/artifacts/directories/.gitkeep' exist

    Scenario: An example of checking the non-existence of a file
      When file 'src/undefined.txt' does not exist

    Scenario: An example of checking with deleting a file
      When i create an empty file 'artifacts/files/example.txt'
      Then file 'artifacts/files/example.txt' exist
      Then i delete the file 'artifacts/files/example.txt'
      Then file 'artifacts/files/example.txt' does not exist

    Scenario: An example of reading from a file
      When i create a file 'artifacts/files/hello.txt' containing:
        """
        HELLO WORLD
        """
      Then i open file 'artifacts/files/hello.txt'
      Then i delete the file 'artifacts/files/hello.txt'

    Scenario: An example of reading from a file and comparing the contents
      When i create a file 'artifacts/files/hello.txt' containing:
        """
        HELLO WORLD
        """
      Then i open file 'artifacts/files/hello.txt'
      Then file content match:
        """
        HELLO WORLD
        """
      Then file content does not match:
        """
        EXAMPLE
        """
      Then i delete the file 'artifacts/files/hello.txt'

    Scenario: An example of reading from a file and comparing the contents line by line
      When i create a file 'artifacts/files/hello.txt' containing:
        """
        100
        example
        """
      Then i open file 'artifacts/files/hello.txt'
      #Note: | line number | comparison operation | value |
      Then file contents:
        | 1 | is                | 100        |
        | 2 | is                | example    |
        | 1 | matches           | ^\d+       |
        | 2 | matches           | ^[A-Za-z]+ |
        | 1 | contains          | 10         |
        | 2 | contains          | ex         |
        | 1 | is different from | 10         |
        | 2 | is different from | ex         |
        | 1 | is higher than    | 10         |
        | 1 | is lower than     | 101        |
      Then i delete the file 'artifacts/files/hello.txt'

    Scenario: An example of reading from a file and comparing the number of lines
      When i create a file 'artifacts/files/hello.txt' containing:
        """
        HELLO WORLD
        EXAMPLE
        """
      Then i open file 'artifacts/files/hello.txt'
      And file contains '2' lines
      Then i delete the file 'artifacts/files/hello.txt'

    Scenario: An example of comparing the contents of files
      When i create an empty file 'artifacts/files/example.txt'
      When i create an empty file 'artifacts/files/example2.txt'
      Then contents of files 'artifacts/files/example.txt' and 'artifacts/files/example2.txt' are identical
      Then i delete the file 'artifacts/files/example.txt'
      And i delete the file 'artifacts/files/example2.txt'

    Scenario: An example of copying a file
      When i create directory 'artifacts/directories/tree/first/second'
      When i create an empty file 'artifacts/files/example.txt'
      When i copy file 'artifacts/files/example.txt' to 'artifacts/directories/tree/first/second/example.txt'
      Then file 'artifacts/directories/tree/first/second/example.txt' exist
      Then i delete the directory 'artifacts/directories/tree/first/second'
      And i delete the file 'artifacts/files/example.txt'

    Scenario: An example of moving a file
      When i create directory 'artifacts/directories/tree/first/second'
      When i create an empty file 'artifacts/files/example.txt'
      When i move file 'artifacts/files/example.txt' to 'artifacts/directories/tree/first/second/example.txt'
      Then file 'artifacts/files/example.txt' does not exist
      And file 'artifacts/directories/tree/first/second/example.txt' exist
      Then i delete the directory 'artifacts/directories/tree/first/second'

    Scenario: An example of moving a file to a directory
      When i create directory 'artifacts/directories/tree/first/second'
      When i create an empty file 'artifacts/files/example.txt'
      When i move file 'artifacts/files/example.txt' to directory 'artifacts/directories/tree/first/second'
      Then file 'artifacts/files/example.txt' does not exist
      And file 'artifacts/directories/tree/first/second/example.txt' exist
      Then i delete the directory 'artifacts/directories/tree/first/second'

  Rule: Parsing

    Scenario: An example of parsing a json file
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.json'
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

    Scenario: An example of parsing an xml file
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.xml'
      Then i check the result according to the scheme 'artifacts/schemas/xml.xsd'
      And sum of '$.root.element.*.remoteAS' results in '196656.0'
      And as a result:
        | $.root.element.[0].localAS  | is                | 65551                |
        | $.root.element.[0].remoteAS | is higher than    | 65550                |
        | $.root.element.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.root.element.[1].status   | is lower than     | 1                    |
        | $.root.element.[2].status   | is different from | 0                    |
        | $.root.element.[2].routerId | contains          | 192                  |

    Scenario: An example of parsing a yaml file
      #Note: more https://github.com/json-path/JsonPath
      When i open file 'artifacts/parsers/example.yaml'
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

    Scenario: An example of parsing a text file using a template
      #Note: more https://github.com/sonalake/utah-parser, https://github.com/json-path/JsonPath
      When i parse the file 'artifacts/parsers/example.txt' using the template 'artifacts/templates/parser.xml'
      Then result contains '3' records
      And sum of '$..localAS' results in '196653.0'
      And as a result:
        | $.[0].localAS  | is                | 65551                |
        | $.[0].remoteAS | is higher than    | 65550                |
        | $.[0].remoteIp | matches           | ^\d+\.\d+\.\d+\.\d+$ |
        | $.[1].status   | is lower than     | 1                    |
        | $.[2].status   | is different from | 0                    |
        | $.[2].routerId | contains          | 192                  |
