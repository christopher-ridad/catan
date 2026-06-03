# BVA for `SetupPhase`

### Method under test: `SetupPhase(Game)`

- **TC1: constructor_nullGame_throwsIllegalArgument** ( :white_check_mark: )
  - **State of the system**: `game = null`
  - **Expected output**: `IllegalArgumentException`

- **TC2: constructor_validGame_2Players_buildsCorrectPlacementOrder** ( :white_check_mark: )
  - **State of the system**: Valid `Game` with players `[P1, P2]`
  - **Expected output**: Phase created; `getPlacementOrder()` returns `[P1, P2, P2, P1]` (length = 4); `getCurrentPlayer()` returns `P1`; `isComplete()` returns `false`

- **TC3: constructor_validGame_4Players_buildsCorrectPlacementOrder** ( :white_check_mark: )
  - **State of the system**: Valid `Game` with players `[P1, P2, P3, P4]`
  - **Expected output**: Phase created; `getPlacementOrder()` returns `[P1, P2, P3, P4, P4, P3, P2, P1]` (length = 8); `getCurrentPlayer()` returns `P1`; `isComplete()` returns `false`

### Method under test: `placeSettlement(Player, int)`

- **TC4: placeSettlement_nullPlayer_throwsIllegalArgument** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; `player = null`, `vertexId = 0` (valid)
  - **Expected output**: `IllegalArgumentException`

- **TC5: placeSettlement_wrongPlayer_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; `player = P2` (not current), `vertexId = 0` (valid and unoccupied)
  - **Expected output**: `IllegalStateException`

- **TC6: placeSettlement_invalidVertexId_throwsIllegalArgument** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; `player = P1` (current), `vertexId = 999` (out of range 0-53)
  - **Expected output**: `IllegalArgumentException`

- **TC7: placeSettlement_occupiedVertex_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P2's turn; vertex 0 is already occupied by P1's settlement; `player = P2`, `vertexId = 0`
  - **Expected output**: `IllegalStateException`

- **TC8: placeSettlement_vertexAdjacentToExistingSettlement_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P2's turn; vertex 0 is occupied by P1; vertex 3 is adjacent to vertex 0 (distance rule violation); `player = P2`, `vertexId = 3`
  - **Expected output**: `IllegalStateException`

- **TC9: placeSettlement_validRound1_placesSuccessfully** ( :white_check_mark: )
  - **State of the system**: It is P1's turn (Round 1, index 0); vertex 0 is unoccupied and distance-compliant; `player = P1`, `vertexId = 0`
  - **Expected output**: Settlement placed at vertex 0; `getCurrentRound()` still returns 1; next action must be `placeRoad()`

- **TC10: placeSettlement_validRound2_placesSuccessfully** ( :white_check_mark: )
  - **State of the system**: It is P4's turn (Round 2, first in reverse order); all Round 1 placements complete; vertex 38 is unoccupied and distance-compliant; vertex 38 is adjacent to hex 12 (MOUNTAINS) producing ORE; `player = P4`, `vertexId = 38`
  - **Expected output**: Settlement placed at vertex 38; `getCurrentRound()` returns 2; resource distribution triggers after subsequent `placeRoad()`

### Method under test: `placeRoad(Player, int)`

- **TC11: placeRoad_nullPlayer_throwsIllegalArgument** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; P1 has placed a settlement; `player = null`, `edgeId = 0` (valid and adjacent)
  - **Expected output**: `IllegalArgumentException`

- **TC12: placeRoad_wrongPlayer_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; P1 has placed a settlement; `player = P2` (not current), `edgeId = 0` (valid and adjacent)
  - **Expected output**: `IllegalStateException`

- **TC13: placeRoad_beforeSettlementPlaced_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; P1 has NOT yet placed a settlement this turn; `player = P1`, `edgeId = 0`
  - **Expected output**: `IllegalStateException`

- **TC14: placeRoad_invalidEdgeId_throwsIllegalArgument** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; P1 has placed a settlement; `player = P1`, `edgeId = 999` (out of range 0-71)
  - **Expected output**: `IllegalArgumentException`

- **TC15: placeRoad_occupiedEdge_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P2's turn; P2 has placed a settlement; the edge adjacent to P1's settlement is already occupied by P1's road; `player = P2`, `edgeId` = that occupied edge
  - **Expected output**: `IllegalStateException`

- **TC16: placeRoad_notAdjacentToCurrentSettlement_throwsIllegalState** ( :white_check_mark: )
  - **State of the system**: It is P1's turn; P1 has placed a settlement at vertex 0; target edge is valid and unoccupied but not adjacent to vertex 0; `player = P1`
  - **Expected output**: `IllegalStateException`

- **TC17: placeRoad_validRound1_placesAndAdvancesClockwise** ( :white_check_mark: )
  - **State of the system**: It is P1's turn (Round 1, placement index 0); P1 has placed settlement at vertex 0; edge adjacent to vertex 0 is unoccupied; `player = P1`
  - **Expected output**: Road placed; `getCurrentPlayer()` returns P2; `getCurrentRound()` still returns 1

- **TC18: placeRoad_round1LastPlayer_transitionsToRound2** ( :white_check_mark: )
  - **State of the system**: All of Round 1 complete (placement index 3 of 4-player game); P4 placed last
  - **Expected output**: `getCurrentPlayer()` returns P4 (same player, starts Round 2); `getCurrentRound()` returns 2

- **TC19: placeRoad_round2_placesAndAdvancesCounterClockwise** ( :white_check_mark: )
  - **State of the system**: It is P4's turn (Round 2, placement index 4 of 8); P4 has placed settlement at vertex 38; edge adjacent to vertex 38 is unoccupied; `player = P4`
  - **Expected output**: Road placed; `getCurrentPlayer()` returns P3; `getCurrentRound()` still returns 2

- **TC20: placeRoad_round2LastPlayer_completesPhase** ( :white_check_mark: )
  - **State of the system**: It is P1's turn (Round 2, placement index 7 of 8); all other players have completed Round 2; P1 has placed second settlement; edge is valid and adjacent; `player = P1`
  - **Expected output**: Road placed; `isComplete()` returns `true`

### Method under test: `getPlacementOrder()`

- **TC21: getPlacementOrder_2Players_returnsCorrectSequence** ( :white_check_mark: )
  - **State of the system**: `SetupPhase` initialized with 2-player `Game` containing `[P1, P2]`
  - **Expected output**: Returns `[P1, P2, P2, P1]` (Round 1 clockwise, then Round 2 counter-clockwise)

- **TC22: getPlacementOrder_4Players_returnsCorrectSequence** ( :white_check_mark: )
  - **State of the system**: `SetupPhase` initialized with 4-player `Game` containing `[P1, P2, P3, P4]`
  - **Expected output**: Returns `[P1, P2, P3, P4, P4, P3, P2, P1]` (length = 8)

- **TC23: getPlacementOrder_lengthAlwaysEqualsDoublePlayerCount** ( :white_check_mark: )
  - **State of the system**: `SetupPhase` with any valid `Game`
  - **Expected output**: Length of placement order == 2 x (number of players)

### Method under test: `getCurrentRound()`

- **TC24: getCurrentRound_placementIndices0ToN_returnsRound1** ( :white_check_mark: )
  - **State of the system**: 4-player game; placements at indices 0, 1, 2, 3 have been made
  - **Expected output**: `getCurrentRound()` returns 1

- **TC24b: getCurrentRound_atIndexN_returnsRound2** ( :white_check_mark: )
  - **State of the system**: 4-player game (n=4); all Round 1 placements complete; about to start index 4
  - **Expected output**: `getCurrentRound()` returns 2

- **TC25: getCurrentRound_placementIndicesNTo2N_returnsRound2** ( :white_check_mark: )
  - **State of the system**: 4-player game; placements at indices 4, 5, 6, 7 in progress
  - **Expected output**: `getCurrentRound()` returns 2

### Method under test: `getCurrentPlayer()`

- **TC26: getCurrentPlayer_atStart_returnsFirstPlayerOfRound1** ( :white_check_mark: )
  - **State of the system**: Fresh `SetupPhase` with 4-player game; no placements made yet; placement index = 0
  - **Expected output**: `P1`

- **TC27: getCurrentPlayer_midRound1_returnsCorrectClockwisePlayer** ( :white_check_mark: )
  - **State of the system**: 4-player game; placements complete at indices 0 and 1; placement index = 2
  - **Expected output**: `P3`

- **TC28: getCurrentPlayer_lastOfRound1_returnsLastPlayer** ( :white_check_mark: )
  - **State of the system**: 4-player game; placements complete at indices 0, 1, 2; placement index = 3
  - **Expected output**: `P4`

- **TC29: getCurrentPlayer_firstOfRound2_returnsLastPlayer** ( :white_check_mark: )
  - **State of the system**: 4-player game; all Round 1 complete; placement index = 4 (first of Round 2)
  - **Expected output**: `P4`

- **TC30: getCurrentPlayer_midRound2_returnsCorrectCounterClockwisePlayer** ( :white_check_mark: )
  - **State of the system**: 4-player game; placements complete at indices 4, 5, 6; placement index = 7
  - **Expected output**: `P1`

### Method under test: `isComplete()`

- **TC31: isComplete_atStart_returnsFalse** ( :white_check_mark: )
  - **State of the system**: Fresh `SetupPhase`, no placements made; placement index = 0
  - **Expected output**: `false`

- **TC32: isComplete_afterRound1Complete_returnsFalse** ( :white_check_mark: )
  - **State of the system**: All 4 players have completed Round 1 placements; Round 2 has not yet begun
  - **Expected output**: `false`

- **TC33: isComplete_midRound2_returnsFalse** ( :white_check_mark: )
  - **State of the system**: Round 2 in progress; P4 has placed at vertex 38; P3, P2, P1 have not yet gone
  - **Expected output**: `false`

- **TC34: isComplete_afterAllRound2PlacementsFinished_returnsTrue** ( :white_check_mark: )
  - **State of the system**: All 4 players have completed Round 1 and Round 2 placements
  - **Expected output**: `true`

### Method under test: `distributeStartingResources(Player)`

- **TC35: resourceDistribution_afterSecondSettlementAndRoad_grantsOnePerAdjacentProducingHex** ( :white_check_mark: )
  - **State of the system**: Round 2; P4 places second settlement at vertex 38; vertex 38 is adjacent to hex 12 (MOUNTAINS) only; `player = P4`
  - **Expected output**: After `placeRoad()`, P4 has exactly 1 ORE; `getTotalResourceCount()` returns 1

- **TC36: resourceDistribution_desertHexDoesNotGrantResource** ( :white_check_mark: )
  - **State of the system**: Round 2; P1 places second settlement at vertex 53; vertex 53 is adjacent to hex 18 (DESERT) only; `player = P1`
  - **Expected output**: After `placeRoad()`, P1 has 0 resources; `getTotalResourceCount()` returns 0

- **TC37: resourceDistribution_multiplePlayersDoNotInterfere** ( :white_check_mark: )
  - **State of the system**: Round 2; P4 places at vertex 38 (adjacent to MOUNTAINS -> 1 ORE); P3 then places at vertex 42 (adjacent to HILLS -> 1 BRICK)
  - **Expected output**: After P3 places, P4's resource count is unchanged; P3's `getTotalResourceCount()` returns 1

- **TC38: resourceDistribution_firstRoundPlacementGrantsNoResources** ( :white_check_mark: )
  - **State of the system**: Round 1; P1 places settlement at vertex 0 then places road; vertex 0 is adjacent to producing hexes
  - **Expected output**: After `placeRoad()`, P1 has 0 resources; `getTotalResourceCount()` returns 0