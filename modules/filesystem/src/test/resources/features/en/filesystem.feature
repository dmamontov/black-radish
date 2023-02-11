# language: en

@examples @module:filesystem @module:core @file @converter
Feature: Filesystem

  This module provides the ability to work with the file system.

  Rule: Directories
        
    Scenario: An example of creating a directory
      When i create directory 'artifacts/tmp/example/first/second'
        
    Scenario: An example of deleting a directory
      When i create directory 'artifacts/tmp/example/first/second'
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of checking the existence of a directory in resources
      When i create directory 'artifacts/tmp/example/first/second'
      Then directory 'artifacts/tmp/example/first/second' exist
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of checking the existence of a directory relative to the package root
      Given directory 'src/test/resources/artifacts/tmp' exist
        
    Scenario: An example of checking if a directory does not exist
      Given directory 'src/undefined' does not exist
        
    Scenario: An example of checking with deleting directories
      When i create directory 'artifacts/tmp/example/first/second'
      Then directory 'artifacts/tmp/example/first/second' exist
      Then i delete the directory 'artifacts/tmp/example'
      And directory 'artifacts/tmp/example/first/second' does not exist
        
    Scenario: An example of copying a directory
      Given i create directory 'artifacts/tmp/example/first/second'
      When i copy directory 'artifacts/tmp/example/first/second' to 'artifacts/tmp/example/first/copy'
      Then directory 'artifacts/tmp/example/first/copy' exist
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of moving a directory
      Given i create directory 'artifacts/tmp/example/first/second'
      When i move directory 'artifacts/tmp/example/first/second' to 'artifacts/tmp/example/first/move'
      Then directory 'artifacts/tmp/example/first/second' does not exist
      And directory 'artifacts/tmp/example/first/move' exist
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of moving a directory to another directory
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/move'
      When i move directory 'artifacts/tmp/example/first/second' to directory 'artifacts/tmp/example/first/move'
      Then directory 'artifacts/tmp/example/first/second' does not exist
      And directory 'artifacts/tmp/example/first/move/second' exist
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of getting and comparing a list of files and directories
      Given i create an empty file 'artifacts/tmp/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example_second.txt'
      When i get a directory tree 'artifacts/tmp'
      Then directory contain:
        | .gitkeep           |
        | example_first.txt   |
        | example_second.txt |
      Then i delete the file 'artifacts/tmp/example_first.txt'
      And i delete the file 'artifacts/tmp/example_second.txt'
        
    Scenario: An example of getting and comparing a list of files and directories recursively
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then directory contain:
        | example/first/second/.black-radish    |
        | example/first/second/example_first.txt |
        | example/first/third/.black-radish     |
        | example/first/second                  |
        | example/first/third                   |
        | example/first                         |
        | example                              |
      And directory does not contain:
        | undefined |
      Then i delete the directory 'artifacts/tmp/example'
        
  Rule: Files
        
    Scenario: An example of creating an empty file
      When i create an empty file 'artifacts/tmp/example.txt'
        
    Scenario: An example of deleting a file.
      When i create an empty file 'artifacts/tmp/example.txt'
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of creating a file with custom content
      When i create a file 'artifacts/tmp/hello.txt' containing:
        """
        HELLO WORLD
        """
        Then i delete the file 'artifacts/tmp/hello.txt'
        
    Scenario: An example of checking the existence of a file in resources
      When i create an empty file 'artifacts/tmp/example.txt'
      Then file 'artifacts/tmp/example.txt' exist
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of checking the existence of a file relative to the package root
      Given file 'src/test/resources/artifacts/tmp/.gitkeep' exist
        
    Scenario: An example of checking the non-existence of a file
      Given file 'src/undefined.txt' does not exist
        
    Scenario: An example of checking with deleting a file
      When i create an empty file 'artifacts/tmp/example.txt'
      Then file 'artifacts/tmp/example.txt' exist
      Then i delete the file 'artifacts/tmp/example.txt'
      Then file 'artifacts/tmp/example.txt' does not exist
    
    Scenario: An example of reading from a file
      When i create a file 'artifacts/tmp/example.txt' containing:
        """
        Example
        """
      Then i open file 'artifacts/tmp/example.txt'
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of reading from a file and comparing the contents line by line
      #Note: For line-by-line comparison of a file, the following order is used: line number, comparison operation, value
      #Note: All checks are supported similarly to variables
      When i create a file 'artifacts/tmp/example.txt' containing:
        """
        100
        example
        """
      Then i open file 'artifacts/tmp/example.txt'
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
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of reading from a file and comparing the number of lines for equality
      When i create a file 'artifacts/tmp/example.txt' containing:
        """
        HELLO WORLD
        EXAMPLE
        """
      Then i open file 'artifacts/tmp/example.txt'
      And file contains '2' lines
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of reading from a file and comparing the number of lines to the minimum number
      When i create a file 'artifacts/tmp/example.txt' containing:
        """
        HELLO WORLD
        EXAMPLE
        """
      Then i open file 'artifacts/tmp/example.txt'
      And file contains at least '1' lines
      Then i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of comparing the contents of files
      Given i create an empty file 'artifacts/tmp/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example_second.txt'
      Then contents of files 'artifacts/tmp/example_first.txt' and 'artifacts/tmp/example_second.txt' are identical
      Then i delete the file 'artifacts/tmp/example_first.txt'
      And i delete the file 'artifacts/tmp/example_second.txt'
        
    Scenario: An example of copying a file
      Given i create directory 'artifacts/tmp/tree'
      Given i create an empty file 'artifacts/tmp/example.txt'
      When i copy file 'artifacts/tmp/example.txt' to 'artifacts/tmp/tree/example.txt'
      Then file 'artifacts/tmp/tree/example.txt' exist
      Then i delete the directory 'artifacts/tmp/tree'
      And i delete the file 'artifacts/tmp/example.txt'
        
    Scenario: An example of moving a file
      Given i create directory 'artifacts/tmp/tree'
      Given i create an empty file 'artifacts/tmp/example.txt'
      When i move file 'artifacts/tmp/example.txt' to 'artifacts/tmp/tree/example.txt'
      Then file 'artifacts/tmp/example.txt' does not exist
      And file 'artifacts/tmp/tree/example.txt' exist
      Then i delete the directory 'artifacts/tmp/tree'
        
    Scenario: An example of moving a file to a directory
      Given i create directory 'artifacts/tmp/tree'
      Given i create an empty file 'artifacts/tmp/example.txt'
      When i move file 'artifacts/tmp/example.txt' to directory 'artifacts/tmp/tree'
      Then file 'artifacts/tmp/example.txt' does not exist
      And file 'artifacts/tmp/tree/example.txt' exist
      Then i delete the directory 'artifacts/tmp/tree'

  Rule: Comparing the result
    #Note: All comparison operations are supported

    Scenario: An example of comparing the original result
      Given i create a file 'artifacts/tmp/example.txt' containing:
        """
        Example
        """
      When i open file 'artifacts/tmp/example.txt'
      Then result is 'Example'
      And result is different from 'undefined'
      And result contains 'Ex'
      And i delete the file 'artifacts/tmp/example.txt'

    Scenario: Example of comparison of the original result, variant 2
      Given i create a file 'artifacts/tmp/example.txt' containing:
        """
        Example
        """
      When i open file 'artifacts/tmp/example.txt'
      Then result is:
        """
        Example
        """
      And result is different from:
        """
        undefined
        """
      And result contains:
        """
        Ex
        """
      And i delete the file 'artifacts/tmp/example.txt'

  Rule: DirectoryTreeConverter
        
    Scenario: An example of checking the number of files and directories in a directory
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then result contains '9' records
      Then i delete the directory 'artifacts/tmp/example'

    Scenario: An example of checking the number of files and directories in a directory for the minimum number
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then result contains at least '2' records
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of checking the contents of a directory by json path
      #Note: All checks are supported similarly to variables
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then as a result:
        | $.[0]  | is       | example/first/third/example_second.txt |
        | $.[1]  | matches  | ^.*\/\.black-radish$                  |
        | $.[2]  | contains | example_first                          |
      Then i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of saving the contents of a directory to a variable as json
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then i save the result in a variable 'JSON'
      Then '${JSON}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-tree.json}'
      And i delete the directory 'artifacts/tmp/example'
        
    Scenario: An example of saving the contents of a directory by json path to a variable
      #File:json: src/test/resources/artifacts/files/converted-tree.json
      Given i create directory 'artifacts/tmp/example/first/second'
      Given i create directory 'artifacts/tmp/example/first/third'
      Given i create an empty file 'artifacts/tmp/example/first/second/example_first.txt'
      Given i create an empty file 'artifacts/tmp/example/first/third/example_second.txt'
      When i get a directory tree 'artifacts/tmp' recursively
      Then i save the result '$.[4]' in a variable 'PATH'
      Then '${PATH}' is '.gitkeep'
      And i delete the directory 'artifacts/tmp/example'

  Rule: JsonConverter

    Scenario: An example of checking the number of records in json for equality
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then result contains '2' records

    Scenario: An example of checking the number of records in json for the minimum number
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then result contains at least '2' records

    Scenario: An example of json validation against json schema
      #File:json: src/test/resources/artifacts/files/example.json
      #File:json: src/test/resources/artifacts/files/schema-json.json
      When i open file 'artifacts/files/example.json'
      Then i check the content against the schema 'artifacts/files/schema-json.json'

    Scenario: An example of summing values in json by json path
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of json conversion by json path
      #Note: All checks are supported similarly to variables
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original json to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.json}'

    Scenario: An example of saving the result of json conversion to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-json.json}'

    Scenario: An example of saving the result of converting json by json path to a variable
      #File:json: src/test/resources/artifacts/files/example.json
      When i open file 'artifacts/files/example.json'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: YamlConverter

    Scenario: An example of checking the number of records in yaml for equality
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then result contains '2' records

    Scenario: An example of checking the number of records in yaml for the minimum number
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then result contains at least '2' records

    Scenario: An example of yaml validation against json schema
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      #File:json: src/test/resources/artifacts/files/schema-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then i check the content against the schema 'artifacts/files/schema-yaml.json'

    Scenario: An example of summing values in yaml by json path
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of the yaml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original yaml to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      When i open file 'artifacts/files/example.yaml'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.yaml}'

    Scenario: An example of saving the result of yaml conversion to a variable
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-yaml.json}'

    Scenario: An example of saving the result of converting yaml by json path to a variable.
      #File:yaml: src/test/resources/artifacts/files/example.yaml
      #File:json: src/test/resources/artifacts/files/converted-yaml.json
      When i open file 'artifacts/files/example.yaml'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: XmlConverter

    Scenario: Example of xml validation according to xsd schema
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      #File:bin: src/test/resources/artifacts/files/schema-xml.xsd
      When i open file 'artifacts/files/example.xml'
      Then i check the content against the schema 'artifacts/files/schema-xml.xsd'

    Scenario: An example of summing values in xml by json path
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i open file 'artifacts/files/example.xml'
      Then sum of '$.root.element.*.remoteAS' results in '131103.0'

    Scenario: An example of checking the result of xml transformation by json path
      #Note: All checks are supported similarly to variables
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i open file 'artifacts/files/example.xml'
      Then as a result:
        | $.root.element.[0].localAS  | is             | 65551 |
        | $.root.element.[0].remoteAS | is higher than | 65550 |
        | $.root.element.[1].localAS  | matches        | ^\d+$ |
        | $.root.element.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original xml to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      When i open file 'artifacts/files/example.xml'
      Then i save original in a variable 'ORIGINAL'
      Then '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.xml}'

    Scenario: An example of saving the result of xml transformation into a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i open file 'artifacts/files/example.xml'
      Then i save the result in a variable 'RESULT'
      Then '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-xml.json}'

    Scenario: An example of saving the result of converting xml by json path to a variable
      #File:xml: src/test/resources/artifacts/files/example.xml
      #File:json: src/test/resources/artifacts/files/converted-xml.json
      When i open file 'artifacts/files/example.xml'
      Then i save the result '$.root.element.[0].localAS' in a variable 'LOCAL_AS'
      Then '${LOCAL_AS}' is '65551'

  Rule: TemplateConverter

    Scenario: An example of checking the number of records converted by template for equality
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains '2' records

    Scenario: An example of checking the number of records converted by template for the minimum number
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then result contains at least '2' records

    Scenario: An example of summing values in converted data by template and json path
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then sum of '$..remoteAS' results in '131103.0'

    Scenario: An example of checking the result of data conversion by template and json path
      #Note: All checks are supported similarly to variables
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then as a result:
        | $.[0].localAS  | is             | 65551 |
        | $.[0].remoteAS | is higher than | 65550 |
        | $.[1].localAS  | matches        | ^\d+$ |
        | $.[1].remoteAS | contains       | 65    |

    Scenario: An example of saving the original converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save original in a variable 'ORIGINAL'
      And '${ORIGINAL}' is '${file:UTF-8:src/test/resources/artifacts/files/example.txt}'

    Scenario: An example of saving the result of converted data by template into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result in a variable 'RESULT'
      And '${RESULT}' is '${file:UTF-8:src/test/resources/artifacts/files/converted-txt.json}'

    Scenario: An example of saving the result of converted data by template and json path into a variable
      #File:bin: src/test/resources/artifacts/files/example.txt
      #File:bin: src/test/resources/artifacts/files/template.xml
      #File:bin: src/test/resources/artifacts/files/converted-txt.json
      When i open file 'artifacts/files/example.txt'
      Then i convert the result by template 'artifacts/files/template.xml'
      Then i save the result '$.[0].localAS' in a variable 'LOCAL_AS'
      And '${LOCAL_AS}' is '65551'