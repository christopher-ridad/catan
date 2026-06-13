# BVA Analysis: TurnManager

## Method under test: `TurnManager(Game game, Bank bank, DiceRoll dice)`

- **TC1: Constructor_WithNullGame_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: `null` passed as the `game` argument, valid `Bank` and `DiceRoll` provided
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithNullBank_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid `Game` provided, `null` passed as the `bank` argument, valid `DiceRoll` provided
    - Expected output: `IllegalArgumentException`

- **TC3: Constructor_WithNullDice_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid `Game` and `Bank` provided, `null` passed as the `dice` argument
    - Expected output: `IllegalArgumentException`

- **TC4: Constructor_InitializesCurrentPlayerToFirstPlayer** ( implemented )
    - State of the system: `TurnManager` constructed with a valid 4-player `Game`, no turns started
    - Expected output: `getCurrentPlayer()` returns the first player in the game's player list

- **TC5: Constructor_InitializesTurnNumberToZero** ( implemented )
    - State of the system: `TurnManager` constructed with a valid `Game`, no turns started
    - Expected output: `getCurrentTurnNumber()` returns `0`

- **TC6: Constructor_DoesNotStartFirstTurnAutomatically** ( implemented )
    - State of the system: `TurnManager` constructed with a valid `Game`, no call to `startNextTurn()` made
    - Expected output: `getCurrentTurn()` returns `Optional.empty()`