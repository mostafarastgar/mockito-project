Feature: Cash machine interaction
  Scenario Outline: a simple scenario of cash machine withdrawal
    Given the account balance is <accountBalance>
      And the card <cardNumber> is valid
      And the machine has <machineCashAmount> money
    When the account holder requests <requestAmount>
    Then the cashpoint should dispense <dispenseAmount>
      And the account balance should be <finalAccountBalance>
      And the card should be returned
    Examples:
      | accountBalance | cardNumber | machineCashAmount | requestAmount | dispenseAmount | finalAccountBalance |
      | 100            | 1527       | 200               | 20            | 20             | 80                  |
      | 200            | 1531       | 400               | 50            | 50             | 150                 |
