# language: {{cookiecutter.language}}
{% if cookiecutter.language == 'ru' %}
Функция: Базовый функционал

  Сценарий: Пример ожидания
    Допустим я жду '1' секунду
{% else %}
Feature: Basic functionality

  Scenario: Wait example
    Given i wait '1' second
{% endif %}