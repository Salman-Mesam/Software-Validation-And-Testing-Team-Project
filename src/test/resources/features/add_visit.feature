Feature: Add visit

  Scenario Outline: Add a visit for a specific pet (normal flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user adds a visit for the pet "<petName>" the information with "<date>" and "<description>"
    Then the previous visits table should have the "<date>" and "<description>" of the newly added visit
    Examples:
      | firstName | lastName | petName | date | description |
      | George | Franklin | Leo | 2022-03-07 | shot |
      | George | Franklin | Leo | 2022-03-10 | sick |
      | Peter | McTavish | George | 2022-03-16 | sick |

  Scenario Outline: Add a visit for a specific pet with Owner that has common last name (alternate flow)
    When the user searches for an owner with first name "<firstName>" and a common last name "<lastName>"
    And the user adds a visit for the pet "<petName>" the information with "<date>" and "<description>"
    Then the previous visits table should have the "<date>" and "<description>" of the newly added visit
    Examples:
      | firstName | lastName | petName | date | description |
      | Betty | Davis | Basil | 2022-04-07 | shot |
      | Harold | Davis | Iggy | 2022-04-10 | sick |

  Scenario Outline: Add a visit for a specific pet with Owner using no input (alternate flow)
    When the user searches for an owner called "<firstName>" "<lastName>" with no input
    And the user adds a visit for the pet "<petName>" the information with "<date>" and "<description>"
    Then the previous visits table should have the "<date>" and "<description>" of the newly added visit
    Examples:
      | firstName | lastName | petName | date | description |
      | Carlos | Estaban | Sly | 2022-05-07 | shot |
      | Maria | Escobito | Mulligan | 2022-05-10 | sick |
      | Harold | Davis | Iggy | 2022-05-16 | sick |

  Scenario Outline:  Add a visit for an invalid Owner (error flow)
    When the user searches for an owner with invalid last name "<lastName>"
    Then the message "has not been found" should be displayed
    Examples:
      | lastName |
      | fasafsfsa |
      | 1212124 |
      | '''''`` |

  Scenario Outline: Add a visit for a specific pet with invalid date (error flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user adds a visit for the pet "<petName>" the information with "<date>" and "<description>"
    Then it should display "<errorMessage>" under the "<inputIndex>"th input
    Examples:
      | firstName | lastName | petName | date       | description | errorMessage      | inputIndex |
      | George    | Franklin | Leo     | hello      | shot        | invalid date      | 0          |
      | Peter     | McTavish | George  | 2020-03-a  | sick        | invalid date      | 0          |
      | Jeff      | Black    | Lucky   | 2020-11-01 |             | must not be empty | 1          |
      | David     | Schroeder| Freddy  |            | sick        | must not be empty | 0          |
