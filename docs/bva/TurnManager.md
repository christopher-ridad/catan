# BVA Analysis: TurnManager

## Method under test: `TurnManager(Game game, Bank bank, DiceRoll dice)`

- **TC1: Constructor_WithNullGame_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: `null` passed as the `game` argument, valid `Bank` and `DiceRoll` provided
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithNullBank_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid `Game` provided, `null` passed as the `bank` argument, valid `DiceRoll` provided
    - Expected output: `IllegalArgumentException`