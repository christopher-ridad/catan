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

- **TC11: getPlayerCount_withTwoPlayers_returnsTwo** ( implemented )
    - State of the system: Game constructed with 2 valid players
    - Expected output: 2