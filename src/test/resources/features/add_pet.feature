Feature: Add Pet

  Scenario Outline: Add a pet for a specific Owner (normal flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user adds the pet's information "<name>", "<birthDate>" and "<type>"
    Then the pet should appear in the list of pets with the right information "<name>", "<birthDate>" and "<type>"
    And edit added pet's name to not collide with future pets
    Examples:
      | firstName | lastName | name | birthDate | type |
      | George | Franklin | Snow | 2000-03-05 | dog |
      | Peter | McTavish | Kitty | 1998-11-25 | dog |

  Scenario Outline: Add a pet for a specific Owner with common last name (alternate flow)
    When the user searches for an owner with first name "<firstName>" and a common last name "<lastName>"
    And the user adds the pet's information "<name>", "<birthDate>" and "<type>"
    Then the pet should appear in the list of pets with the right information "<name>", "<birthDate>" and "<type>"
    And edit added pet's name to not collide with future pets
    Examples:
      | firstName | lastName | name | birthDate | type |
      | Betty | Davis | John | 2000-02-05 | dog |
      | Harold | Davis | Kat | 2000-01-01 | cat |

  Scenario Outline: Add a pet for a specific Owner using no input (alternate flow)
    When the user searches for an owner called "<firstName>" "<lastName>" with no input
    And the user adds the pet's information "<name>", "<birthDate>" and "<type>"
    Then the pet should appear in the list of pets with the right information "<name>", "<birthDate>" and "<type>"
    And edit added pet's name to not collide with future pets
    Examples:
      | firstName | lastName | name | birthDate | type |
      | Jeff | Black | Mike | 2000-01-05 | hamster |
      | Maria | Escobito | John | 2000-12-05 | snake |


  Scenario Outline: Add a pet for an invalid Owner (error flow)
    When the user searches for an owner with invalid last name "<lastName>"
    Then the message "has not been found" should be displayed
    Examples:
      | lastName |
      | fasafsfsa |
      | 1212124 |
      | '''''`` |

  Scenario Outline: Add a pet for a specific Owner with an invalid form input (error flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user adds the pet's information "<name>", "<birthDate>" and "<type>"
    Then it should display "<errorMessage>" under the "<inputIndex>"th input
    Examples:
      | firstName | lastName | name     | birthDate   | type  | errorMessage            | inputIndex |
      | George    | Franklin | Test     | hello       | bird  | invalid dateis required | 2          |
      | George    | Franklin | Test2    | 20200301    | cat   | invalid dateis required | 2          |
      | George    | Franklin | Test3    | 2020-14-01  | cat   | invalid dateis required | 2          |
      | George    | Franklin | Test4    | 2020-01-50  | cat   | invalid dateis required | 2          |
      | George    | Franklin | Leo      | 2000-01-12  | cat   | is already in use       | 1          |
      | Jean      | Coleman  | Max      | 2000-01-12  | cat   | is already in use       | 1          |
      | Jean      | Coleman  | Samantha | 2000-01-12  | cat   | is already in use       | 1          |

