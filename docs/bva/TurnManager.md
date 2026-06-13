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


- **TC7: Constructor_GameIsNotOverInitially** ( implemented )
    - State of the system: `TurnManager` constructed with a valid `Game`, no turns taken
    - Expected output: `isGameOver()` returns `false` and `getWinner()` returns `Optional.empty()`

- **TC8: Constructor_InitializesPlayerTurnCountsToZero** ( implemented )
    - State of the system: `TurnManager` constructed with a valid 4-player `Game`, no turns taken
    - Expected output: `getPlayerTurnCount(player)` returns `0` for every player in the game

---

## Method under test: `startNextTurn()`

- **TC9: StartNextTurn_ReturnsTurnForCurrentPlayer** ( implemented )
    - State of the system: `TurnManager` freshly constructed, no previous turn in progress
    - Expected output: returned `Turn` is non-null and its phase is `PRODUCTION`

- **TC10: StartNextTurn_StoresTurnAsCurrentTurn** ( implemented )
    - State of the system: `TurnManager` freshly constructed, no previous turn in progress
    - Expected output: `getCurrentTurn()` is present and returns the same `Turn` object that was returned by `startNextTurn()`

- **TC11: StartNextTurn_WhilePreviousTurnIncomplete_ThrowsIllegalStateException** ( implemented )
    - State of the system: a turn has been started but not yet ended (still in `PRODUCTION` phase)
    - Expected output: `IllegalStateException`

- **TC12: StartNextTurn_AfterPreviousTurnCompleted_Succeeds** ( implemented )
    - State of the system: a full turn has been started, advanced to `BUILD`, and ended via `endCurrentTurn()`
    - Expected output: a new non-null `Turn` is returned, distinct from the previous one