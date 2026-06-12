# BVA Analysis: VictoryPointCalculator

## Method under test: `getTotalVP(Player player, SpecialCardTracker tracker)`

- **TC1: GetTotalVP_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer, valid `tracker` pointer to a true object
  - Expected output: `IllegalArgumentException`

- **TC2: GetTotalVP_WithNullTracker_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `player` pointer to a true object, `tracker` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC3: GetTotalVP_NoVpSources_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `tracker`; player's VP source counts are 0 (0 settlements, 0 cities, 0 VP dev cards), and boolean checks for Longest Road and Largest Army are false
  - Expected output: `0`

- **TC4: GetTotalVP_OnlySettlements_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `tracker`; player has a count >1 for settlements (e.g., 4), but a count of 0 for all other VP sources
  - Expected output: `4`

- **TC5: GetTotalVP_OnlyCities_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `tracker`; player has a count >1 for cities (e.g., 3), but a count of 0 for all other VP sources
  - Expected output: `6`

- **TC6: GetTotalVP_OnlySpecialCards_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `tracker`; tracker boolean values indicate the player holds Longest Road (true) and Largest Army (true), with a count of 0 for all other VP sources
  - Expected output: `4`

- **TC7: GetTotalVP_OnlyVpDevCards_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `tracker`; player has a count >1 for VP dev cards (e.g., 3), with a count of 0 for all other VP sources
  - Expected output: `3`

- **TC8: GetTotalVP_WinThresholdJustBelow_ReturnsNine** ( :x: )
  - State of the system: valid `player` and `tracker`; combinations of sources sum exactly to 9 VP (e.g., 3 settlements = 3 VP, 2 cities = 4 VP, Longest Road = 2 VP)
  - Expected output: `9`

- **TC9: GetTotalVP_WinThresholdExact_ReturnsTen** ( :x: )
  - State of the system: valid `player` and `tracker`; combinations of sources sum exactly to the win condition of 10 VP (e.g., 2 settlements = 2 VP, 2 cities = 4 VP, Largest Army = 2 VP, 2 VP dev cards = 2 VP)
  - Expected output: `10`

- **TC10: GetTotalVP_MaximumPossibleVP_ReturnsTheoreticalMax** ( :x: )
  - State of the system: valid `player` and `tracker`; player has the maximum possible value for all VP counts simultaneously: 5 settlements (5 VP), 4 cities (8 VP), 5 VP dev cards (5 VP), Longest Road (2 VP), and Largest Army (2 VP)
  - Expected output: `22`

## Method under test: `getSettlementVP(Player player, Board board)`

- **TC11: GetSettlementVP_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer, valid `board` pointer to a true object
  - Expected output: `IllegalArgumentException`

- **TC12: GetSettlementVP_WithNullBoard_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `player` pointer to a true object, `board` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC13: GetSettlementVP_EmptyBoard_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `board`; no vertices on the board contain any buildings
  - Expected output: `0`

- **TC14: GetSettlementVP_PlayerHasZeroSettlements_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `board`; board has settlements, but none are owned by `player`
  - Expected output: `0`

- **TC15: GetSettlementVP_PlayerHasOneSettlement_ReturnsOne** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns exactly 1 settlement on the board
  - Expected output: `1`

- **TC16: GetSettlementVP_PlayerHasMultipleSettlements_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns >1 settlements (e.g., 3) on the board
  - Expected output: `3`

- **TC17: GetSettlementVP_PlayerHasMaximumSettlements_ReturnsMax** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns the maximum possible number of settlements allowed by the rules (e.g., 5)
  - Expected output: `5`

- **TC18: GetSettlementVP_PlayerHasCities_CitiesAreNotCounted** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns 2 settlements and 2 cities on the board
  - Expected output: `2`

- **TC19: GetSettlementVP_MixedOwnership_CountsOnlyTargetPlayer** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns 2 settlements, another player owns 3 settlements on the board
  - Expected output: `2`

## Method under test: `getCityVP(Player player, Board board)`

- **TC20: GetCityVP_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer, valid `board` pointer to a true object
  - Expected output: `IllegalArgumentException`

- **TC21: GetCityVP_WithNullBoard_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `player` pointer to a true object, `board` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC22: GetCityVP_EmptyBoard_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `board`; no vertices on the board contain any buildings
  - Expected output: `0`

- **TC23: GetCityVP_PlayerHasZeroCities_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `board`; board has cities, but none are owned by `player`
  - Expected output: `0`

- **TC24: GetCityVP_PlayerHasOneCity_ReturnsTwo** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns exactly 1 city on the board
  - Expected output: `2`

- **TC25: GetCityVP_PlayerHasMultipleCities_ReturnsSum** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns >1 cities (e.g., 2) on the board
  - Expected output: `4`

- **TC26: GetCityVP_PlayerHasMaximumCities_ReturnsMax** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns the maximum possible number of cities allowed by the rules (e.g., 4) 
  - Expected output: `8`

- **TC27: GetCityVP_PlayerHasSettlements_SettlementsAreNotCounted** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns 2 cities and 3 settlements on the board
  - Expected output: `4`

- **TC28: GetCityVP_MixedOwnership_CountsOnlyTargetPlayer** ( :x: )
  - State of the system: valid `player` and `board`; `player` owns 2 cities, another player owns 1 city on the board
  - Expected output: `4`

## Method under test: `getDevCardVP(Player player)`

- **TC29: GetDevCardVP_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC30: GetDevCardVP_ZeroVPCards_ReturnsZero** ( :x: )
  - State of the system: valid `player` pointer to a true object; player's hand contains 0 VP development cards
  - Expected output: `0`

- **TC31: GetDevCardVP_OneVPCard_ReturnsOne** ( :x: )
  - State of the system: valid `player`; player's hand contains exactly 1 VP development card
  - Expected output: `1`

- **TC32: GetDevCardVP_MultipleVPCards_ReturnsSum** ( :x: )
  - State of the system: valid `player`; player's hand contains >1 VP development cards (e.g., 3)
  - Expected output: `3`

- **TC33: GetDevCardVP_MaximumVPCards_ReturnsMax** ( :x: )
  - State of the system: valid `player`; player's hand contains the maximum possible number of VP development cards available in the game (typically 5)
  - Expected output: `5`

- **TC34: GetDevCardVP_OnlyNonVpDevCards_ReturnsZero** ( :x: )
  - State of the system: valid `player`; player's hand contains development cards, but they are all non-VP cards (e.g., 2 Knights, 1 Monopoly)
  - Expected output: `0`

- **TC35: GetDevCardVP_MixedDevCards_CountsOnlyVPCards** ( :x: )
  - State of the system: valid `player`; player's hand contains a mix of card types (e.g., 2 Knights, 2 VP cards)
  - Expected output: `2`

- **TC36: GetDevCardVP_CardsAreUnplayed_AreStillCounted** ( :x: )
  - State of the system: valid `player`; player has 2 VP cards that are marked as strictly "unplayed" or unrevealed
  - Expected output: `2`

## Method under test: `computeLongestRoad(Player player, Board board)`

- **TC37: ComputeLongestRoad_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer, valid `board` pointer to a true object
  - Expected output: `IllegalArgumentException`

- **TC38: ComputeLongestRoad_WithNullBoard_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `player` pointer to a true object, `board` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC39: ComputeLongestRoad_ZeroRoadsPlaced_ReturnsZero** ( :x: )
  - State of the system: valid `player` and `board`; `player` has 0 roads placed on the board
  - Expected output: `0`

- **TC40: ComputeLongestRoad_OneRoadPlaced_ReturnsOne** ( :x: )
  - State of the system: valid `player` and `board`; `player` has exactly 1 road placed
  - Expected output: `1`

- **TC41: ComputeLongestRoad_RoadBelowClaimThreshold_ReturnsLength** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a continuous unbroken path of 4 roads
  - Expected output: `4`

- **TC42: ComputeLongestRoad_RoadAtClaimThreshold_ReturnsLength** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a continuous unbroken path of exactly 5 roads
  - Expected output: `5`

- **TC43: ComputeLongestRoad_DisconnectedRoads_ReturnsLongestSegment** ( :x: )
  - State of the system: valid `player` and `board`; `player` has two disconnected road segments (e.g., one of length 3, one of length 4)
  - Expected output: `4`

- **TC44: ComputeLongestRoad_BranchingPath_ReturnsLongestBranchOnly** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a Y-shaped road network with a common base of 2 roads, branching into one path of 2 roads and another of 3 roads
  - Expected output: `5` (base of 2 + longest branch of 3)

- **TC45: ComputeLongestRoad_CircularPath_EdgesVisitedOnce_ReturnsLength** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a closed hexagon loop of 6 roads with no branches
  - Expected output: `6`

- **TC46: ComputeLongestRoad_CircularPathWithTail_ReturnsTotalLength** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a closed hexagon loop of 6 roads with a 2-road tail extending from one vertex
  - Expected output: `8`

- **TC47: ComputeLongestRoad_PathBrokenByOpponentSettlement_ReturnsLongestHalf** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a continuous line of 6 roads, but an opponent's settlement is placed at the middle vertex, splitting it into two 3-road segments
  - Expected output: `3`

- **TC48: ComputeLongestRoad_PathNotBrokenByOwnSettlement_ReturnsTotalLength** ( :x: )
  - State of the system: valid `player` and `board`; `player` has a continuous line of 6 roads, with their own settlement placed at the middle vertex
  - Expected output: `6`

- **TC49: ComputeLongestRoad_MaximumRoadsPlaced_ReturnsMax** ( :x: )
  - State of the system: valid `player` and `board`; `player` has all 15 available roads placed in a single, continuous, unbroken, non-branching line
  - Expected output: `15`

## Method under test: `computeKnightCount(Player player)`

- **TC50: ComputeKnightCount_WithNullPlayer_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `player` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC51: ComputeKnightCount_ZeroKnightsPlayed_ReturnsZero** ( :x: )
  - State of the system: valid `player` pointer to a true object; `player` has 0 Knight cards marked as played
  - Expected output: `0`

- **TC52: ComputeKnightCount_OneKnightPlayed_ReturnsOne** ( :x: )
  - State of the system: valid `player`; `player` has exactly 1 Knight card marked as played 
  - Expected output: `1`

- **TC53: ComputeKnightCount_MultipleKnightsPlayed_ReturnsCount** ( :x: )
  - State of the system: valid `player`; `player` has >1 Knight cards marked as played (e.g., 3)
  - Expected output: `3`

- **TC54: ComputeKnightCount_MaximumKnightsPlayed_ReturnsMax** ( :x: )
  - State of the system: valid `player`; `player` has the maximum possible number of Knight cards in the standard deck marked as played (14)
  - Expected output: `14`

- **TC55: ComputeKnightCount_UnplayedKnights_AreNotCounted** ( :x: )
  - State of the system: valid `player`; `player` holds 3 Knight cards, but only 1 is marked as played and 2 remain unplayed
  - Expected output: `1`

- **TC56: ComputeKnightCount_MixedPlayedDevCards_CountsOnlyKnights** ( :x: )
  - State of the system: valid `player`; `player` has 2 Knight cards marked as played, and 1 other non-knight development card (e.g., Monopoly) marked as played
  - Expected output: `2`

## Method under test: `getWinner(Game game, Board board, SpecialCardTracker tracker)`

- **TC69: GetWinner_WithNullGame_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: `game` is the null pointer, valid `board`, valid `tracker`
  - Expected output: `IllegalArgumentException`

- **TC70: GetWinner_WithNullBoard_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `game`, `board` is the null pointer, valid `tracker`
  - Expected output: `IllegalArgumentException`

- **TC71: GetWinner_WithNullTracker_ThrowsIllegalArgumentException** ( :x: )
  - State of the system: valid `game`, valid `board`, `tracker` is the null pointer
  - Expected output: `IllegalArgumentException`

- **TC72: GetWinner_HighestVPIsNine_ReturnsEmpty** ( :x: )
  - State of the system: valid arguments; the highest total VP among all players is exactly 9 (Just below threshold)
  - Expected output: `Optional.empty()`

- **TC73: GetWinner_OnePlayerHasExactlyTenVP_ReturnsPlayer** ( :x: )
  - State of the system: valid arguments; exactly one player has a total VP of exactly 10, all others have < 10 (Exact threshold)
  - Expected output: `Optional.of(player)` containing the player with 10 VP

- **TC74: GetWinner_OnePlayerHasElevenVP_ReturnsPlayer** ( :x: )
  - State of the system: valid arguments; exactly one player has a total VP > 10 (e.g., 11), all others have < 10
  - Expected output: `Optional.of(player)` containing the player with 11 VP

- **TC75: GetWinner_MultiplePlayersHaveTenVP_ActivePlayerWins** ( :x: )
  - State of the system: valid arguments; Player A and Player B both have exactly 10 VP; according to `game.getPlayers()` turn order, it is currently Player B's turn
  - Expected output: `Optional.of(Player B)`

- **TC76: GetWinner_MultiplePlayersHaveTenVP_NeitherIsActivePlayer_ReturnsFirstInOrder** ( :x: )
  - State of the system: valid arguments; Player A and Player B both have exactly 10 VP; according to `game.getPlayers()` turn order, it is Player C's turn (who has < 10 VP)
  - Expected output: `Optional.of(Player A)` (assuming Player A precedes Player B in standard turn order, serving as a fallback tiebreaker)

## Method under test: `hasWinner(Game game, Board board, SpecialCardTracker tracker)`

- **TC77: HasWinner_WithNullGame_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: `game` is the null pointer, valid `board` pointer to a true object, valid `tracker`
    - Expected output: `IllegalArgumentException`

- **TC78: HasWinner_WithNullBoard_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: valid `game` pointer to a true object, `board` is the null pointer, valid `tracker`
    - Expected output: `IllegalArgumentException`

- **TC79: HasWinner_WithNullTracker_ThrowsIllegalArgumentException** ( :x: )
    - State of the system: valid `game`, valid `board`, `tracker` is the null pointer
    - Expected output: `IllegalArgumentException`

- **TC80: HasWinner_HighestVPIsNine_ReturnsFalse** ( :x: )
    - State of the system: valid arguments; the highest total VP among all players is exactly 9 
    - Expected output: `false`

- **TC81: HasWinner_OnePlayerHasExactlyTenVP_ReturnsTrue** ( :x: )
    - State of the system: valid arguments; exactly one player has a total VP of exactly 10, all others have < 10 
    - Expected output: `true`

- **TC82: HasWinner_OnePlayerHasElevenVP_ReturnsTrue** ( :x: )
    - State of the system: valid arguments; exactly one player has a total VP > 10 (e.g., 11), all others have < 10
    - Expected output: `true`

- **TC83: HasWinner_MultiplePlayersHaveTenVP_ReturnsTrue** ( :x: )
    - State of the system: valid arguments; two players somehow both have exactly 10 VP simultaneously
    - Expected output: `true`