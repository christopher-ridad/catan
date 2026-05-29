# BVA Analysis: Game

## Method under test: `Game(List<Player> players, Board board)`

- **TC1: gameConstructor_withTwoPlayers_doesNotThrow** ( implemented )
    - State of the system: constructor is passed a list of 2 valid Player objects
    - Expected output: no exception thrown

- **TC2: gameConstructor_withThreePlayers_doesNotThrow** ( implemented )
    - State of the system: constructor is passed a list of 3 valid Player objects
    - Expected output: no exception thrown

- **TC3: gameConstructor_withFourPlayers_doesNotThrow** ( implemented )
    - State of the system: constructor is passed a list of 4 valid Player objects
    - Expected output: no exception thrown

- **TC4: gameConstructor_withNoPlayers_throwsIllegalArgument** ( implemented )
    - State of the system: constructor is passed an empty list
    - Expected output: `IllegalArgumentException`

- **TC5: gameConstructor_withOnePlayer_throwsIllegalArgument** ( implemented )
    - State of the system: constructor is passed a list of 1 valid Player object
    - Expected output: `IllegalArgumentException`

- **TC6: gameConstructor_withFivePlayers_throwsIllegalArgument** ( implemented )
    - State of the system: constructor is passed a list of 5 valid Player objects
    - Expected output: `IllegalArgumentException`

- **TC7: gameConstructor_withNullList_throwsNullPointer** ( implemented )
    - State of the system: constructor is passed null instead of a list
    - Expected output: `NullPointerException`

- **TC8: gameConstructor_withNullPlayerInList_throwsNullPointer** ( implemented )
    - State of the system: constructor is passed a list of 2 elements where one entry is null
    - Expected output: `NullPointerException`

- **TC9: gameConstructor_withNullBoard_throwsNullPointer** ( implemented )
    - State of the system: constructor is passed a null Board object
    - Expected output: `NullPointerException`

## Method under test: `getPlayers()`

- **TC10: getPlayers_returnsPlayersInOriginalOrder** ( implemented )
    - State of the system: Game constructed with 3 valid players
    - Expected output: list of players in the same order as provided to the constructor

- **TC11: getPlayers_returnsUnmodifiableList** ( implemented )
    - State of the system: Game constructed with 2 valid players, caller attempts to add to the returned list
    - Expected output: `UnsupportedOperationException`

## Method under test: `getPlayerCount()`

- **TC12: getPlayerCount_withTwoPlayers_returnsTwo** ( implemented )
    - State of the system: Game constructed with 2 valid players
    - Expected output: 2

- **TC13: getPlayerCount_withFourPlayers_returnsFour** ( implemented )
    - State of the system: Game constructed with 4 valid players
    - Expected output: 4

## Method under test: `getPlayer(int index)`

- **TC16: getPlayer_withIndexZero_returnsFirstPlayer** ( implemented )
  - State of the system: Game constructed with 3 valid players, index 0 passed to getPlayer()
  - Expected output: the first player passed to the constructor

- **TC17: getPlayer_withLastIndex_returnsLastPlayer** ( implemented )
  - State of the system: Game constructed with 3 valid players, index 2 (last valid index) passed to getPlayer()
  - Expected output: the third player passed to the constructor

## Method under test: `getBoard()`

- **TC15: getBoard_returnsCorrectBoard** ( implemented )
    - State of the system: Game constructed with valid board
    - Expected output: the board object used to instantiate the game

## Method under test: `Game(List<Object> players, Board board)` — duplicate color validation

- **TC18: gameConstructor_withTwoPlayersOfSameColor_throwsIllegalArgument** ( implemented )
  - State of the system: 2 players both assigned PlayerColor.RED passed to constructor
  - Expected output: IllegalArgumentException thrown