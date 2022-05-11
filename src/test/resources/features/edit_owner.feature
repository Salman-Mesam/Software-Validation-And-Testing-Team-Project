Feature: Edit Owner information

  Scenario Outline: Edit a specific Owner (normal flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user edits the information with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    Then the owner's information should be updated accordingly with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    And reset the owner's information back to default
    Examples:
      | firstName | lastName | newFirstName | newLastName | address | city | telephone |
      | George  | Franklin | George | Franklin | 110 W. Liberty St. | Madison | 1112223333 |
      | Eduardo | Rodriquez| John   | Rodriquez | 2693 Commerce St. | McFarland | 6085558763 |
      | Carlos | Estaban    | Lebron   | James | 4444 Commerce St. | Brossard | 33333333 |

  Scenario Outline: Edit a specific Owner with a common last name (alternate flow)
    When the user searches for an owner with first name "<firstName>" and a common last name "<lastName>"
    And the user edits the information with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    Then the owner's information should be updated accordingly with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    And reset the owner's information back to default
    Examples:
      | firstName | lastName | newFirstName | newLastName | address | city | telephone |
      | Betty   | Davis    | Betty  | Davis | 222 Cardinal Ave. | Montreal | 5556667777 |
      | Harold   | Davis    | Harold  | Davis | 111 Cardinal Ave. | Laval | 355511111 |

  Scenario Outline: Edit a specific Owner using no input (alternate flow)
    When the user searches for an owner called "<firstName>" "<lastName>" with no input
    And the user edits the information with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    Then the owner's information should be updated accordingly with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    And reset the owner's information back to default
    Examples:
      | firstName | lastName | newFirstName | newLastName | address | city | telephone |
      | Jeff   | Black    | John  | Bob | 112 Avenue McGill | Montreal | 622211111 |
      | Betty   | Davis    | John  | Bob | 555 Avenue McGill | Montreal | 222222222 |

  Scenario Outline: Edit a invalid Owner (error flow)
    When the user searches for an owner with invalid last name "<lastName>"
    Then the message "has not been found" should be displayed
    Examples:
      | lastName |
      | fasafsfsa |
      | 1212124 |
      | '''''`` |

  Scenario Outline: Edit an Owner with invalid input (error flow)
    When the user searches for an owner with first name "<firstName>" and last name "<lastName>"
    And the user edits the information with "<newFirstName>", "<newLastName>", "<address>", "<city>" and "<telephone>"
    Then it should display "<errorMessage>" under the "<inputIndex>"th input
    Examples:
      | firstName | lastName | newFirstName | newLastName | address           | city      | telephone     | errorMessage                                                                    | inputIndex  |
      | George  | Franklin   | George       | Franklin    | 110 W. Liberty St.| Madison   | test          | numeric value out of bounds (<10 digits>.<0 digits> expected)                   | 4           |
      | Eduardo | Rodriquez  | Eduardo      | Rodriquez   | 2693 Commerce St. | McFarland | 012345678912  | numeric value out of bounds (<10 digits>.<0 digits> expected)                   | 4           |
      | Carlos  | Estaban    | Carlos       | Estaban     | 4444 Commerce St. | Brossard  | 112233'111    | numeric value out of bounds (<10 digits>.<0 digits> expected)                   | 4           |
      | George  | Franklin   |              | Franklin    | 110 W. Liberty St.| Madison   | 111111        | must not be empty                                                               | 0           |
      | George  | Franklin   | George       |             | 110 W. Liberty St.| Madison   | 111111        | must not be empty                                                               | 1           |
      | George  | Franklin   | George       | Franklin    |                   | Madison   | 111111        | must not be empty                                                               | 2           |
      | George  | Franklin   | George       | Franklin    | 110 W. Liberty St.|           | 111111        | must not be empty                                                               | 3           |
