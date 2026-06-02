# BVA Analysis: Turn

## Method under test: `Turn(Game game, Player activePlayer, Dice dice, Bank bank)`

- **TC1: Constructor_WithNullGame_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: `game = null`
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithNullActivePlayer_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: `activePlayer = null`
    - Expected output: `IllegalArgumentException`

- **TC3: Constructor_WithNullDice_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: `dice = null`
    - Expected output: `IllegalArgumentException`

- **TC4: Constructor_WithNullBank_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: `bank = null`
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithValidArgs_SetsPhaseToProduction** ( :x: )
    - State of the system: valid `game`, valid `activePlayer = P1`, valid `dice`, valid `bank`
    - Expected output: `getPhase()` returns `PRODUCTION`

## Method under test: `buildRoad(int edgeId)`

- **TC6: BuildRoad_PlayerDoesNotHaveBrick_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, `activePlayer` does not have the required 1 brick
    - Expected output: `IllegalStateException`

- **TC7: BuildRoad_PlayerDoesNotHaveLumber_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, `activePlayer` does not have the required 1 lumber
    - Expected output: `IllegalStateException`

- **TC8: BuildRoad_PlayerHasExactlyOneBrickAndLumber_NoExceptionThrown** ( :x: )
    - State of the system: valid `edgeId`, `activePlayer` has exactly one of each required resource
    - Expected output: `getOwner` called on `edgeId` returns `activePlayer`, `getResources` called on `activePlayer` returns 0 for both `BRICK` and `LUMBER`

- **TC9: BuildRoad_EdgeIsOccupied_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, edge is occupied by a road already
    - Expected output: `hasRoad()` returns true, throws `IllegalStateException`

- **TC10: BuildRoad_PlayerHasFourteenRoads_NoExceptionThrown** ( :x: )
    - State of the system: valid `edgeId`, activePlayer has one less than the maximum number of roads allowed (14)
    - Expected output: `getOwner` called on `edgeId` returns `activePlayer`
  
- **TC10: BuildRoad_PlayerHasFifteenRoads_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, activePlayer already has the maximum number of roads allowed (15)
    - Expected output: `IllegalStateException`

- **TC11: BuildRoad_EdgeIsNotConnectedToExistingNetwork_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, edge is not connected to the player's existing network
    - Expected output: `IllegalStateException`

- **TC12: BuildRoad_RoadIsConnectedToRoad_NoExceptionThrown** ( :x: )
    - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it
    - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC13: BuildRoad_RoadIsConnectedToSettlement_NoExceptionThrown** ( :x: )
    - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's settlement built on it
    - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC14: BuildRoad_RoadIsConnectedToCity_NoExceptionThrown** ( :x: )
    - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's city built on it 
    - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC15: BuildRoad_ConnectedToRoadButBlockedByEnemySettlement_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it, but edge is connected to a vertex with an enemy settlement on it 
    - Expected output: `IllegalStateException`

- **TC16 BuildRoad_TurnPhaseNotBuilding_ThrowsIllegalStateException** ( :x: )
    - State of the system: valid `edgeId`, phase is set to `TRADING`
    - Expected output: `IllegalStateException`

  





