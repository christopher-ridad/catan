# BVA Analysis: Turn

## Method under test: `Turn(Game game, Player activePlayer, Dice dice, Bank bank)`

- **TC1: Constructor_WithNullGame_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: `game = null`
  - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithNullActivePlayer_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: `activePlayer = null`
  - Expected output: `IllegalArgumentException`

- **TC3: Constructor_WithNullDice_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: `dice = null`
  - Expected output: `IllegalArgumentException`

- **TC4: Constructor_WithNullBank_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: `bank = null`
  - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithValidArgs_SetsPhaseToProduction** ( :white_check_mark: )
  - State of the system: valid `game`, valid `activePlayer = P1`, valid `dice`, valid `bank`
  - Expected output: `getPhase()` returns `PRODUCTION`

## Method under test: `buildRoad(int edgeId)`

- **TC6: BuildRoad_PlayerDoesNotHaveBrick_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, `activePlayer` does not have the required 1 brick
  - Expected output: `IllegalStateException`

- **TC7: BuildRoad_PlayerDoesNotHaveLumber_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, `activePlayer` does not have the required 1 lumber
  - Expected output: `IllegalStateException`

- **TC8: BuildRoad_PlayerHasExactlyOneBrickAndLumber_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `edgeId`, `activePlayer` has exactly one of each required resource
  - Expected output: `getResources` called on `activePlayer` returns 0 for both `BRICK` and `LUMBER`

- **TC9: BuildRoad_EdgeIsOccupied_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is occupied by a road already
  - Expected output: `IllegalStateException`

- **TC10: BuildRoad_PlayerHasFourteenRoads_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `edgeId`, activePlayer has one less than the maximum number of roads allowed (14)
  - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC10: BuildRoad_PlayerHasFifteenRoads_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, activePlayer already has the maximum number of roads allowed (15)
  - Expected output: `IllegalStateException`

- **TC11: BuildRoad_EdgeIsNotConnectedToExistingNetwork_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is not connected to the player's existing network
  - Expected output: `IllegalStateException`

- **TC12: BuildRoad_RoadIsConnectedToRoad_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it
  - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC13: BuildRoad_RoadIsConnectedToSettlement_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's settlement built on it
  - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC14: BuildRoad_RoadIsConnectedToCity_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's city built on it
  - Expected output: `getOwner` called on `edgeId` returns `activePlayer`

- **TC15: BuildRoad_ConnectedToRoadButBlockedByEnemySettlement_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it, but edge is connected to a vertex with an enemy settlement on it
  - Expected output: `IllegalStateException`

## Method under test: `buildSettlement(int vertexId)`
Validates player can afford a settlement (1 Brick + 1 Lumber + 1 Wool + 1 Grain), Distance Rule, and road connection; deducts cost; throws if invalid
- **TC16: BuildSettlement_PlayerDoesNotHaveBrick_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `BRICK`
  - Expected output: `IllegalStateException`

- **TC17: BuildSettlement_PlayerDoesNotHaveLumber_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `LUMBER`
  - Expected output: `IllegalStateException`

- **TC18: BuildSettlement_PlayerDoesNotHaveWool_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `WOOL`
  - Expected output: `IllegalStateException`

- **TC19: BuildSettlement_PlayerDoesNotHaveGrain_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `GRAIN`
  - Expected output: `IllegalStateException`

- **TC20: BuildSettlement_PlayerHasExactlyOneOfEachRequiredResource_NoExceptionThrown** ( :x: )
  - State of the system: valid `vertexId`, `activePlayer` has exactly one `BRICK`, `LUMBER`, `WOOL`, and `GRAIN`
  - Expected output: `getResources` called on `activePlayer` returns 0 for each required resource

- **TC21: BuildSettlement_VertexIsNotConnectedToExistingRoad_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, vertex is surrounded by empty edges
  - Expected output: `IllegalStateException`

- **TC22: BuildSettlement_VertexIsSurroundedByEnemyRoads_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, vertex is surrounded by enemy roads
  - Expected output: `IllegalStateException`

- **TC23: BuildSettlement_VertexIsConnectedToExistingRoad_NoExceptionThrown** ( :x: )
  - State of the system: valid `vertexId`, vertex is connected to activePlayer's existing road
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

- **TC24: BuildSettlement_DoesNotSatisfyDistanceRuleSettlement_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, adjacent vertex already has a settlement
  - Expected output: `IllegalStateException`

- **TC25: BuildSettlement_DoesNotSatisfyDistanceRuleCity_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, adjacent vertex already has a city
  - Expected output: `IllegalStateException`

- **TC26: BuildSettlement_SatisfiesDistanceRuleSettlement_NoExceptionThrown** ( :x: )
  - State of the system: valid `vertexId`, satisfies distance rule, adjacent vertices are empty
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

- **TC27: BuildSettlement_VertexAlreadyOccupied_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, already has an existing settlement or city 
  - Expected output: `IllegalStateException`

- **TC28: BuildSettlement_PlayerHasFiveSettlements_ThrowsIllegalStateException** ( :x: )
  - State of the system: valid `vertexId`, player already has the maximum number of settlements
  - Expected output: `IllegalStateException`

- **TC29: BuildSettlement_PlayerHasFourSettlements_NoExceptionThrown** ( :x: )
  - State of the system: valid `vertexId`, player already has one less than the maximum number of settlements
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

  





