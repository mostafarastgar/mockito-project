Feature: the version can be retrieved

  Scenario Outline: client makes call to GET /version
    Given the application is up on port <port>
    When the client calls <path>
    Then the client receives status code of <status>
    And the client receives server version <version>
    Examples:
      | port | path     | status | version |
      | 8080 | /version | 200    | 1.0     |
