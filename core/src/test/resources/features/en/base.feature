# language: en

@example
Feature: Basic functionality

  This feature is an example of the basic functionality

  Rule: Thread

    Scenario: Wait example
      Given i wait '1' second

  Rule: Variables

    Scenario: Examples of comparison operations
      Then '100' is '100'
      And 'example' is 'example'

      And '200' matches '^\d+'
      And 'example' matches '^[A-Za-z]+'

      And '100' contains '10'
      And 'example' contains 'ex'

      And '100' is different from '200'
      And 'example' is different from 'ex'

      And '100.1' is higher than '100'
      And '200' is lower than '200.2'
      And '0xAF' is lower than '176'

    Scenario: An example of creating a variable
      Given i save 'example' in variable 'ONE'
      Then '${ONE}' is 'example'

    Scenario: Examples of converting strings to uppercase
      Given i save 'example_one' in variable 'ONE'
      Then '${upper:${ONE}}' is 'EXAMPLE_ONE'

      Then '${upper:example_two}' is 'EXAMPLE_TWO'

    Scenario: Examples of converting strings to lowercase
      Given i save 'EXAMPLE_ONE' in variable 'ONE'
      Then '${lower:${ONE}}' is 'example_one'

      Then '${lower:EXAMPLE_TWO}' is 'example_two'

    Scenario: Example of getting a variable from common.properties
      #Note: for example, to get a variable from test.properties add for example -Denv=test
      Then '${FOUR}' is '4'

    Scenario: Examples of using mathematical operations
      #Note: more https://www.objecthunter.net/exp4j/
      Then '${math:2+2*2-2}' is '4.0'
      And '${math:3*sin(3.14)-2/(2.3-2)}' is '-6.66188870791721'
      And '${math:3log(3.14)/(2.3+1)}' is '1.0402025453819654'

    Scenario: Examples of generating fake data
      #Note: more https://github.com/DiUS/java-faker
      Then '${faker:internet.email_address}' matches '^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$'

      And '${faker:number.number_between '1','10'}' is lower than '11'
      And '${faker:lorem.characters '6','true'}' matches '^[\w\d]{6}$'

      And '${faker:Name.first_name}' matches '^[A-Za-z]+$'
      And '${faker:es:Name.first_name}' matches '^[A-Za-zÁáÉéÍíÓóÚúÂâÊêÔôÃãÕõÇçÜüÑñ ]+$'

    Scenario: Examples expression evaluation
      #Note: expressions are written in kotlin
      Then '${eval:1>2}' is 'false'
      And '${eval:1+2}' is '3'
      And '${eval:listOf(1, 2).joinToString("-")}' is '1-2'

  Rule: Conditional operator

    Scenario: Examples of using if, elseif, else conditional statements
      #Note: condition is written in kotlin language
      Given i save '${faker:number.random_double '0','1','5'}' in variable 'NUMBER'

      * If '${NUMBER} == 1.0':
      Then '${NUMBER}' is '1.0'
      * Else If '${NUMBER} == 2.0':
      Then '${NUMBER}' is '2.0'
      * Else If '${NUMBER} == 3.0':
      Then '${NUMBER}' is '3.0'
      * Else:
      Then '${NUMBER}' is higher than '3.0'
      * End If

      * If '${NUMBER} == 1.0':
      Then '${NUMBER}' is '1.0'
      * Else If '${NUMBER} == 2.0':
      Then '${NUMBER}' is '2.0'
      * Else:
      Then '${NUMBER}' is higher than '2.0'
      * End If

      * If '${NUMBER} == 1.0':
      Then '${NUMBER}' is '1.0'
      * Else:
      Then '${NUMBER}' is higher than '1.0'
      * End If

      * If '${NUMBER} == 1.0':
      Then '${NUMBER}' is '1.0'
      * Else If '${NUMBER} == 2.0':
      Then '${NUMBER}' is '2.0'
      * End If

  Rule: Step generation

    Scenario: An example of included a scenario from the current feature
      Given i save 'example_two' in variable 'ONE'
      Then '${ONE}' is 'example_two'

      Given i include scenario 'An example of creating a variable'
      Then '${ONE}' is 'example'

    Scenario: An example of included a scenario from another feature
      Given i save 'example_two' in variable 'ONE'
      Then '${ONE}' is 'example_two'

      Given i include scenario 'Included scenario' from feature 'classpath:features/en/include.feature'
      Then '${ONE}' is 'include'

    Scenario: An example of included a scenario with a nested scenario
      #Note: maximum depth(aspect.include.depth) can be configured in the setting.properties file
      Given i save 'example_two' in variable 'ONE'
      Then '${ONE}' is 'example_two'

      Given i include scenario 'Included scenario with a nested scenario' from feature 'classpath:features/en/include.feature'
      Then '${ONE}' is 'depth'

    Scenario: An example of cyclic code generation from and to
      * Loop from '1' to '5':
      * If '${loop.value} == 1':
      Then '${loop.value}' is '1'
      * Else If '${loop.value} == 2':
      Then '${loop.value}' is '2'
      * Else If '${loop.value} == 3':
      Then '${loop.value}' is '3'
      * Else:
      Then '${loop.value}' is higher than '3'
      * End If
      * End Loop

    Scenario: An example of cyclic code generation from and to in reverse order
      * Loop from '10' to '5':
      * If '${loop.value} == 10':
      Then '${loop.value}' is '10'
      * Else If '${loop.value} == 9':
      Then '${loop.value}' is '9'
      * Else If '${loop.value} == 8':
      Then '${loop.value}' is '8'
      * Else:
      Then '${loop.value}' is lower than '8'
      * End If
      * End Loop

    Scenario: An example of cyclic generation from a list
      * Loop in 'first,second,three':
      * If '"${loop.value}" == "first"':
      Then '${loop.value}' is 'first'
      * Else If '"${loop.value}" == "second"':
      Then '${loop.value}' is 'second'
      * Else If '"${loop.value}" == "three"':
      Then '${loop.value}' is 'three'
      * End If
      * End Loop

    Scenario: An example of cyclic generation from a list using a variable
      Given i save 'example' in variable 'ONE'
      * Loop in '${ONE},second,three':
      * If '"${loop.value}" == "example"':
      Then '${loop.value}' is 'example'
      * Else If '"${loop.value}" == "second"':
      Then '${loop.value}' is 'second'
      * Else If '"${loop.value}" == "three"':
      Then '${loop.value}' is 'three'
      * End If
      * End Loop
