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

- **TC13: StartNextTurn_AfterWinnerFound_ThrowsIllegalStateException** ( NOT implemented )
    - State of the system: the active player has been given 10 VP, a full turn has been completed triggering `isGameOver() == true`
    - Expected output: `IllegalStateException`

---

## Method under test: `endCurrentTurn()`

- **TC14: EndCurrentTurn_WithNoTurnInProgress_ThrowsIllegalStateException** ( implemented )
    - State of the system: `TurnManager` freshly constructed, `startNextTurn()` has not been called
    - Expected output: `IllegalStateException`

- **TC15: EndCurrentTurn_DuringProductionPhase_ThrowsIllegalStateException** ( implemented )
    - State of the system: a turn has been started but dice have not been rolled (phase is `PRODUCTION`)
    - Expected output: `IllegalStateException`

- **TC16: EndCurrentTurn_DuringTradePhase_ThrowsIllegalStateException** ( implemented )
    - State of the system: a turn has been started and dice rolled (phase is `TRADE`), but `advanceToBuild()` has not been called
    - Expected output: `IllegalStateException`

- **TC17: EndCurrentTurn_DuringBuildPhase_CompletesTheTurn** ( implemented )
    - State of the system: a turn has been started and advanced to `BUILD` phase via `rollDice()` then `advanceToBuild()`
    - Expected output: `endCurrentTurn()` does not throw; the turn's phase transitions to `DONE`

- **TC18: EndCurrentTurn_WhenTurnAlreadyDone_DoesNotThrow** ( implemented )
    - State of the system: a turn has been started, advanced to `BUILD`, and `Turn.endTurn()` called directly so the phase is already `DONE`
    - Expected output: `endCurrentTurn()` does not throw

- **TC19: EndCurrentTurn_AdvancesToNextPlayer** ( implemented )
    - State of the system: p1's turn has been started and ended via `endCurrentTurn()`
    - Expected output: `getCurrentPlayer()` returns p2

- **TC20: EndCurrentTurn_WrapsAroundToFirstPlayerAfterLastPlayer** ( implemented )
    - State of the system: all 4 players have each completed exactly one turn
    - Expected output: `getCurrentPlayer()` returns p1 (back to the first player)

- **TC21: EndCurrentTurn_IncrementsCurrentTurnNumber** ( implemented )
    - State of the system: one full turn has been completed via `endCurrentTurn()`
    - Expected output: `getCurrentTurnNumber()` returns `1`