# language: en

@ignore
Feature: Example of an included file

  Scenario: Included scenario
    Given i save 'include' in variable 'ONE'

  Scenario: Included scenario with a nested scenario
    Given i include scenario 'Included scenario'
    Then '${ONE}' is 'include'
    Then i save 'depth' in variable 'ONE'