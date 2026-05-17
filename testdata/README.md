# Test Data

Place your Excel test data files here.

## testdata.xlsx — Required sheets

### LoginData
| username | password | expectedResult |
|---|---|---|
| standard_user | secret_sauce | pass |
| locked_out_user | secret_sauce | fail |
| invalid_user | wrong_pass | fail |

### ProductData
| productName | expectedPrice |
|---|---|
| Sauce Labs Backpack | $29.99 |
| Sauce Labs Bike Light | $9.99 |

> Note: The actual `.xlsx` file must be created manually with the above structure.
> It is not committed to source control to avoid binary conflicts.
> Generate it once and place it in this directory.
