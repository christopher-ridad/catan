# BVA Analysis: Turn

## Method under test: `Turn(Game game, Player activePlayer, DiceRoll dice, Bank bank)`

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

- **TC16: BuildSettlement_PlayerDoesNotHaveBrick_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `BRICK`
  - Expected output: `IllegalStateException`

- **TC17: BuildSettlement_PlayerDoesNotHaveLumber_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `LUMBER`
  - Expected output: `IllegalStateException`

- **TC18: BuildSettlement_PlayerDoesNotHaveWool_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `WOOL`
  - Expected output: `IllegalStateException`

- **TC19: BuildSettlement_PlayerDoesNotHaveGrain_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 1 `GRAIN`
  - Expected output: `IllegalStateException`

- **TC20: BuildSettlement_PlayerHasExactlyOneOfEachRequiredResource_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` has exactly one `BRICK`, `LUMBER`, `WOOL`, and `GRAIN`
  - Expected output: `getResources` called on `activePlayer` returns 0 for each required resource

- **TC21: BuildSettlement_VertexIsNotConnectedToExistingRoad_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is surrounded by empty edges
  - Expected output: `IllegalStateException`

- **TC22: BuildSettlement_VertexIsSurroundedByEnemyRoads_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is surrounded by enemy roads
  - Expected output: `IllegalStateException`

- **TC23: BuildSettlement_VertexIsConnectedToExistingRoad_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is connected to activePlayer's existing road
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

- **TC24: BuildSettlement_DoesNotSatisfyDistanceRuleSettlement_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, adjacent vertex already has a settlement
  - Expected output: `IllegalStateException`

- **TC25: BuildSettlement_DoesNotSatisfyDistanceRuleCity_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, adjacent vertex already has a city
  - Expected output: `IllegalStateException`

- **TC26: BuildSettlement_SatisfiesDistanceRuleSettlement_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, satisfies distance rule, adjacent vertices are empty
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

- **TC27: BuildSettlement_VertexAlreadyOccupied_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, already has an existing settlement or city 
  - Expected output: `IllegalStateException`

- **TC28: BuildSettlement_PlayerHasFiveSettlements_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, player already has the maximum number of settlements
  - Expected output: `IllegalStateException`

- **TC29: BuildSettlement_PlayerHasFourSettlements_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, player already has one less than the maximum number of settlements
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`

## Method under test `buildCity(int vertexId)`

- **TC30: BuildCity_PlayerDoesNotHaveEnoughOre_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 3 `ORE`, has 2
  - Expected output: `IllegalStateException`

- **TC31: BuildCity_PlayerDoesNotHaveEnoughGrain_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` does not have the required 2 `GRAIN`, has 1
  - Expected output: `IllegalStateException`

- **TC32: BuildCity_PlayerHasExactAmountOfEachRequiredResource_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, `activePlayer` has exactly three `ORE` and two `GRAIN`
  - Expected output: `getResources` called on `activePlayer` returns 0 for each required resource

- **TC33: BuildCity_PlayerHasFourCities_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, player already has the maximum number of cities
  - Expected output: `IllegalStateException`

- **TC34: BuildCity_PlayerHasThreeCities_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, player already has one less than the maximum number of cities
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`, `isCity` called on `vertexId` returns True

- **TC35: BuildCity_PlayerDoesNotHaveExistingSettlement_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is empty
  - Expected output: `IllegalStateException`

- **TC36: BuildCity_VertexOccupiedByEnemySettlement_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is occupied by enemy settlement
  - Expected output: `IllegalStateException`

- **TC37: BuildCity_VertexOccupiedByEnemyCity_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is occupied by enemy city
  - Expected output: `IllegalStateException`

- **TC38: BuildCity_VertexOccupiedByOwnCity_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is occupied by player's own city
  - Expected output: `IllegalStateException`

- **TC39: BuildCity_VertexOccupiedByOwnSettlement_NoExceptionThrown** ( :white_check_mark: )
  - State of the system: valid `vertexId`, vertex is occupied by player's own settlement
  - Expected output: `getOwner` called on `vertexId` returns `activePlayer`, `isCity` called on `vertexId` returns True

## Method under test: `rollDice()`

- **TC40: RollDice_ValidCall_ReturnsRollValue** ( :white_check_mark: )
  - State of the system: `phase = PRODUCTION`, dice mocked to return 8
  - Expected output: `rollDice()` returns `8`

- **TC41: RollDice_ValidCall_AdvancesPhaseToTrade** ( :white_check_mark: )
  - State of the system: `phase = PRODUCTION`, valid roll
  - Expected output: `getPhase()` returns `TRADE`

- **TC42: RollDice_CalledOutsideProductionPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `rollDice()` already called once this turn (phase advanced to `TRADE`)
  - Expected output: `IllegalStateException`

- **TC43: RollDice_NonSevenRoll_DistributesResourcesToSettlementOwner** ( :white_check_mark: )
  - State of the system: roll = 8 (non-7, boundary above robber trigger), `activePlayer` owns a settlement adjacent to a producing hex with number token 8
  - Expected output: `getResourceCount` for the produced resource is greater than 0

- **TC44: RollDice_RollOfSeven_NoResourcesDistributed** ( :white_check_mark: )
  - State of the system: roll = 7 (boundary), `activePlayer` owns a settlement adjacent to a producing hex
  - Expected output: `getTotalResourceCount()` returns `0`

- **TC109: RollDice_NumberMatchesRobberHex_DoesNotProduceFromThatHex** ( :white_check_mark: )
  - State of the system: roll matches the number token of the hex the robber currently occupies, `activePlayer` owns a settlement adjacent only to that hex for the rolled number
  - Expected output: `getTotalResourceCount()` returns `0` (the robbed hex produces nothing even on a matching roll)

## Method under test: `isSevenRolled()`

- **TC45: IsSevenRolled_BeforeRollingDice_ReturnsFalse** ( :white_check_mark: )
  - State of the system: `rollDice()` not yet called this turn
  - Expected output: `false`

- **TC46: IsSevenRolled_AfterRollingSeven_ReturnsTrue** ( :white_check_mark: )
  - State of the system: `rollDice()` called, dice mocked to return 7 (boundary)
  - Expected output: `true`

- **TC47: IsSevenRolled_AfterRollingNonSeven_ReturnsFalse** ( :white_check_mark: )
  - State of the system: `rollDice()` called, dice mocked to return 8 (one above the boundary)
  - Expected output: `false`

## Robber logic — triggered internally by `rollDice()` on a roll of 7

### `enforceDiscard()` (private; exercised through `rollDice()`)

- **TC48: RollDice_RollOfSeven_PlayerWithSevenCards_NoDiscard** ( :white_check_mark: )
  - State of the system: roll = 7, an opponent holds exactly 7 resource cards (boundary — at the discard threshold but not over)
  - Expected output: opponent's `getTotalResourceCount()` remains `7`

- **TC49: RollDice_RollOfSeven_PlayerWithEightCards_DiscardsFour** ( :white_check_mark: )
  - State of the system: roll = 7, an opponent holds exactly 8 resource cards (one above the boundary; `floor(8/2) = 4`)
  - Expected output: opponent's `getTotalResourceCount()` becomes `4`

- **TC50: RollDice_RollOfSeven_PlayerWithNineCards_DiscardsFour** ( :white_check_mark: )
  - State of the system: roll = 7, an opponent holds 9 resource cards across two types (`floor(9/2) = 4`)
  - Expected output: opponent's `getTotalResourceCount()` becomes `5`

### `getRobbingCandidates()`

- **TC51: GetRobbingCandidates_OpponentSettlementAdjacentToRobberHex_IncludesOpponent** ( :white_check_mark: )
  - State of the system: an opponent owns a settlement on a vertex adjacent to the robber's hex
  - Expected output: returned list contains the opponent

- **TC52: GetRobbingCandidates_ActivePlayerSettlementAdjacentToRobberHex_ExcludesActivePlayer** ( :white_check_mark: )
  - State of the system: `activePlayer` owns a settlement on a vertex adjacent to the robber's hex
  - Expected output: returned list does not contain `activePlayer`

- **TC53: GetRobbingCandidates_NoSettlementsAdjacentToRobberHex_ReturnsEmptyList** ( :white_check_mark: )
  - State of the system: no vertex adjacent to the robber's hex is occupied
  - Expected output: returned list is empty

### `moveRobber(int hexId)`

- **TC54: MoveRobber_BeforeSevenRolled_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `rollDice()` not yet called (or last roll was not a 7)
  - Expected output: `IllegalStateException`

- **TC55: MoveRobber_ToCurrentRobberHex_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: 7 just rolled, `hexId` identifies the hex the robber is already on
  - Expected output: `IllegalArgumentException`

- **TC56: MoveRobber_WithInvalidHexId_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: 7 just rolled, `hexId = 99` (out of the board's [0, 18] range)
  - Expected output: `IllegalArgumentException`

- **TC57: MoveRobber_ToValidHex_UpdatesBoardRobberHex** ( :white_check_mark: )
  - State of the system: 7 just rolled, `hexId` identifies a different, valid hex
  - Expected output: `board.getRobberHex()` returns the targeted hex

- **TC58: MoveRobber_CalledTwice_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: robber already moved once this turn
  - Expected output: `IllegalStateException`

### `steal(Player target)`

- **TC59: Steal_BeforeMovingRobber_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: 7 just rolled, robber not yet moved this turn
  - Expected output: `IllegalStateException`

- **TC60: Steal_WhenNoRobbingCandidates_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: robber moved to a hex with no adjacent settlements
  - Expected output: `IllegalStateException`

- **TC61: Steal_FromPlayerNotAdjacentToRobberHex_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: robber moved to a hex; `target` does not own a settlement adjacent to it (a different player does)
  - Expected output: `IllegalArgumentException`

- **TC62: Steal_TargetHasNoResourceCards_CompletesWithNoTransferAndResolvesRobber** ( :white_check_mark: )
  - State of the system: `target` is a valid robbing candidate but holds 0 resource cards (boundary)
  - Expected output: no resource cards change hands, and the robber is fully resolved (`advanceToBuild()` no longer throws — a target with no cards cannot leave the turn permanently stuck)

- **TC63: Steal_FromValidCandidate_TransfersOneResourceCardToActivePlayer** ( :white_check_mark: )
  - State of the system: `target` is a valid robbing candidate holding exactly 1 resource card (boundary)
  - Expected output: `target`'s count for that resource becomes `0`; `activePlayer`'s count for that resource becomes `1`

## Method under test: `advanceToBuild()`

- **TC64: AdvanceToBuild_FromTradePhase_SetsPhaseToBuild** ( :white_check_mark: )
  - State of the system: dice rolled (non-7), turn is in TRADE phase, no robber resolution pending
  - Expected output: `getPhase()` returns `TurnPhase.BUILD`

- **TC65: AdvanceToBuild_FromProductionPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `rollDice()` not yet called, turn is still in PRODUCTION phase
  - Expected output: `IllegalStateException`

- **TC66: AdvanceToBuild_FromBuildPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `advanceToBuild()` already called once this turn (boundary: calling it again)
  - Expected output: `IllegalStateException`

- **TC67: AdvanceToBuild_WithRobberMovePending_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: a 7 was just rolled and `moveRobber()` has not yet been called
  - Expected output: `IllegalStateException`

- **TC68: AdvanceToBuild_WithStealPending_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: a 7 was rolled, robber moved onto a hex with an eligible candidate, `steal()` not yet called
  - Expected output: `IllegalStateException`

- **TC69: AdvanceToBuild_AfterRobberFullyResolved_SetsPhaseToBuild** ( :white_check_mark: )
  - State of the system: a 7 was rolled, robber moved and `steal()` completed (boundary: last required robber step just finished)
  - Expected output: `getPhase()` returns `TurnPhase.BUILD`

- **TC70: AdvanceToBuild_AfterRobberMovedWithNoCandidates_SetsPhaseToBuild** ( :white_check_mark: )
  - State of the system: a 7 was rolled, robber moved onto a hex with no adjacent settlements (no steal required)
  - Expected output: `getPhase()` returns `TurnPhase.BUILD`

- **TC108: AdvanceToBuild_WithPendingTrade_RejectsAndClearsTheOffer** ( :white_check_mark: )
  - State of the system: turn is in TRADE phase with a pending trade offer that has not yet been accepted or rejected
  - Expected output: the offer's status becomes `REJECTED` and `getPendingTrade()` returns empty (a pending offer expires when the active player moves on to the build phase)

## Method under test: `endTurn()`

- **TC71: EndTurn_FromBuildPhase_SetsPhaseToDone** ( :white_check_mark: )
  - State of the system: turn has advanced to BUILD phase
  - Expected output: `getPhase()` returns `TurnPhase.DONE`

- **TC72: EndTurn_FromTradePhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: dice rolled, turn is in TRADE phase (boundary: one phase before BUILD)
  - Expected output: `IllegalStateException`

- **TC73: EndTurn_FromProductionPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `rollDice()` not yet called, turn is still in PRODUCTION phase
  - Expected output: `IllegalStateException`

- **TC74: EndTurn_CalledTwice_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: `endTurn()` already called once this turn, phase is DONE (boundary: calling it again)
  - Expected output: `IllegalStateException`

## Build-phase enforcement: `buildRoad(int)`, `buildSettlement(int)`, `buildCity(int)`

- **TC75: BuildRoad_OutsideBuildPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn still in PRODUCTION phase (`rollDice()` not yet called, boundary: one phase before BUILD is reachable)
  - Expected output: `IllegalStateException`

- **TC76: BuildSettlement_OutsideBuildPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn still in PRODUCTION phase (`rollDice()` not yet called)
  - Expected output: `IllegalStateException`

- **TC77: BuildCity_OutsideBuildPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn still in PRODUCTION phase (`rollDice()` not yet called)
  - Expected output: `IllegalStateException`

## Method under test: `proposeTrade(Player recipient, Map<ResourceType, Integer> offering, Map<ResourceType, Integer> requesting)`

- **TC78: ProposeTrade_OutsideTradePhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn still in PRODUCTION phase (`rollDice()` not yet called)
  - Expected output: `IllegalStateException`

- **TC79: ProposeTrade_WhenTradeAlreadyPending_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: a prior `proposeTrade()` call already created a `PENDING` offer this turn
  - Expected output: `IllegalStateException`

- **TC80: ProposeTrade_OffererCannotAffordOffering_ThrowsIllegalArgumentException** ( :white_check_mark: )
  - State of the system: active player has 0 of a resource type included in `offering` (boundary)
  - Expected output: `IllegalArgumentException`

- **TC81: ProposeTrade_OffererHasExactlyOfferedAmount_CreatesAndStoresPendingTrade** ( :white_check_mark: )
  - State of the system: active player has exactly the amount of each resource included in `offering` (boundary)
  - Expected output: a `PENDING` `TradeOffer` is returned and `getPendingTrade()` returns it

- **TC82: ProposeTrade_AfterPriorOfferResolved_CreatesNewPendingTrade** ( :white_check_mark: )
  - State of the system: a prior offer was accepted/rejected and `pendingTrade` was cleared
  - Expected output: a new `PENDING` `TradeOffer` is created and stored as the pending trade

## Method under test: `acceptTrade(TradeOffer offer)`

- **TC83: AcceptTrade_NoMatchingPendingTrade_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: no trade is pending, or the supplied `offer` is not the stored `pendingTrade`
  - Expected output: `IllegalStateException`

- **TC84: AcceptTrade_OffererCannotAffordOffering_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: offer is pending, but the offerer no longer holds enough of a resource in `offering` (boundary: 0 of a required resource)
  - Expected output: `IllegalStateException`

- **TC85: AcceptTrade_RecipientCannotAffordRequesting_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: offer is pending, offerer can afford `offering`, but the recipient has 0 of a resource in `requesting` (boundary)
  - Expected output: `IllegalStateException`

- **TC86: AcceptTrade_BothPlayersCanAfford_ExchangesResourcesAndClearsPendingTrade** ( :white_check_mark: )
  - State of the system: offerer holds exactly the offered amounts, recipient holds exactly the requested amounts (boundary)
  - Expected output: offerer's and recipient's resource counts reflect the exchange, offer status becomes `ACCEPTED`, and `getPendingTrade()` returns empty

## Method under test: `rejectTrade(TradeOffer offer)`

- **TC87: RejectTrade_NoMatchingPendingTrade_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: no trade is pending, or the supplied `offer` is not the stored `pendingTrade`
  - Expected output: `IllegalStateException`

- **TC88: RejectTrade_PendingOffer_MarksRejectedAndClearsPendingTrade** ( :white_check_mark: )
  - State of the system: a `PENDING` offer is the stored `pendingTrade`
  - Expected output: `offer.getStatus()` becomes `REJECTED` and `getPendingTrade()` returns empty

## Method under test: `getPendingTrade()`

- **TC89: GetPendingTrade_NoOfferProposed_ReturnsEmpty** ( :white_check_mark: )
  - State of the system: `proposeTrade()` has not been called this turn
  - Expected output: `Optional.empty()`

- **TC90: GetPendingTrade_AfterProposeTrade_ReturnsTheOffer** ( :white_check_mark: )
  - State of the system: `proposeTrade()` was just called and returned a `PENDING` offer
  - Expected output: `Optional` containing that exact `TradeOffer`

## Method under test: `submitMaritimeTrade(MaritimeTrade trade)`

- **TC91: SubmitMaritimeTrade_OutsideTradePhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn has advanced to the BUILD phase
  - Expected output: `IllegalStateException`

- **TC92: SubmitMaritimeTrade_BankHasNoneOfReceivingResource_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn is in TRADE phase, bank holds 0 of the trade's `receiving` resource (boundary)
  - Expected output: `IllegalStateException`

- **TC93: SubmitMaritimeTrade_BankHasExactlyOneOfReceivingResource_ExecutesTrade** ( :white_check_mark: )
  - State of the system: turn is in TRADE phase, bank holds exactly 1 of the `receiving` resource (boundary), player holds at least `amount` of `giving`
  - Expected output: player's `giving` count decreases by `amount` and `receiving` count increases by 1; bank's `giving` count increases by `amount` and `receiving` count decreases by 1

## Method under test: `buyDevelopmentCard()`

- **TC94: BuyDevelopmentCard_OutsideBuildPhase_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn still in TRADE phase (not yet advanced to BUILD)
  - Expected output: `IllegalStateException`

- **TC95: BuyDevelopmentCard_PlayerCannotAffordCost_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn is in BUILD phase, active player has 0 of one required resource (Ore, Wool, or Grain) (boundary)
  - Expected output: `IllegalStateException`

- **TC96: BuyDevelopmentCard_NoCardsRemainInDeck_ThrowsIllegalStateException** ( :white_check_mark: )
  - State of the system: turn is in BUILD phase, active player can afford the cost, `getRemainingDeckSize()` is 0 (boundary)
  - Expected output: `IllegalStateException`

- **TC97: BuyDevelopmentCard_PlayerHasExactCostAndOneCardRemains_DeductsCostAndAddsCardToHand** ( :white_check_mark: )
  - State of the system: turn is in BUILD phase, active player has exactly 1 Ore + 1 Wool + 1 Grain, exactly 1 card remains in the deck (boundary)
  - Expected output: player's Ore/Wool/Grain counts become `0`, `getRemainingDeckSize()` becomes `0`, and `getPlayerHand(activePlayer)` grows by one card

## Method under test: `getPlayerHand(Player player)`

- **TC98: GetPlayerHand_PlayerWithNoCards_ReturnsEmptyList** ( :white_check_mark: )
  - State of the system: `player` has not purchased any development cards (boundary)
  - Expected output: empty list

- **TC99: GetPlayerHand_AfterBuyingOneCard_ReturnsListContainingPurchasedCard** ( :white_check_mark: )
  - State of the system: `player` purchased exactly one development card via `buyDevelopmentCard()` (boundary)
  - Expected output: list of size `1` containing that card

- **TC100: GetPlayerHand_ReturnedList_IsUnmodifiable** ( :white_check_mark: )
  - State of the system: a hand list obtained via `getPlayerHand()`
  - Expected output: `UnsupportedOperationException` on attempted mutation

## Method under test: `getRemainingDeckSize()`

- **TC101: GetRemainingDeckSize_NewTurn_Returns25** ( :white_check_mark: )
  - State of the system: no cards have been purchased yet this game (boundary: full 14 KNIGHT + 6 Progress + 5 VICTORY_POINT deck)
  - Expected output: `25`

- **TC102: GetRemainingDeckSize_AfterBuyingOneCard_DecreasesByOne** ( :white_check_mark: )
  - State of the system: exactly one card has been purchased via `buyDevelopmentCard()` (boundary)
  - Expected output: `24`

## Shared validation for dev card play methods

- **TC103: playKnightCard_NullCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `card = null`
  - Expected output: `IllegalArgumentException`

- **TC104: playRoadBuildingCard_WrongPlayer_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `player != activePlayer`
  - Expected output: `IllegalArgumentException`

- **TC105: playYearOfPlenty_WrongPhase_ThrowsIllegalStateException** ( not implemented )
  - State of the system: turn in PRODUCTION phase, valid card
  - Expected output: `IllegalStateException`

- **TC106: playMonopoly_AlreadyPlayedDevCard_ThrowsIllegalStateException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `playedDevCardThisTurn = true`
  - Expected output: `IllegalStateException`

- **TC107: playKnightCard_PlayerDoesNotOwnCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, card not in player's hand
  - Expected output: `IllegalArgumentException`

- **TC108: playRoadBuildingCard_CardPurchasedThisTurn_ThrowsIllegalStateException** ( not implemented )
  - State of the system: valid turn in TRADE phase, card was purchased this turn
  - Expected output: `IllegalStateException`

- **TC109: playYearOfPlenty_CardAlreadyPlayed_ThrowsIllegalStateException** ( not implemented )
  - State of the system: valid turn in TRADE phase, card already played
  - Expected output: `IllegalStateException`

- **TC110: playMonopoly_VictoryPointCard_ThrowsIllegalStateException** ( not implemented )
  - State of the system: valid turn in TRADE phase, card is VICTORY_POINT
  - Expected output: `IllegalStateException`

## Method under test: `playKnightCard(Player, DevelopmentCard)`

- **TC110: playKnightCard_NullCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `card = null`
  - Expected output: `IllegalArgumentException`

- **TC111: playKnightCard_ValidCard_SetsRobberPendingMove** ( not implemented )
  - State of the system: valid turn in TRADE phase, valid KNIGHT card in player's hand
  - Expected output: robber pending move is set, `moveRobber()` can be called, `card.isPlayed()` returns `true`, second card cannot be played this turn

## Method under test: `playRoadBuildingCard(Player, DevelopmentCard, int, int)`

- **TC112: playRoadBuildingCard_NullCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `card = null`
  - Expected output: `IllegalArgumentException`

- **TC113: playRoadBuildingCard_ValidCard_PlacesTwoFreeRoads** ( not implemented )
  - State of the system: valid turn in BUILD phase, valid ROAD_BUILDING card in player's hand, two valid edge IDs
  - Expected output: two roads placed, player resources unchanged, `card.isPlayed()` returns `true`, second card cannot be played this turn

## Method under test: `playYearOfPlenty(Player, DevelopmentCard, ResourceType, ResourceType)`

- **TC114: playYearOfPlenty_NullCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `card = null`
  - Expected output: `IllegalArgumentException`

- **TC115: playYearOfPlenty_ValidCard_GivesTwoResources** ( not implemented )
  - State of the system: valid turn in TRADE phase, valid YEAR_OF_PLENTY card in player's hand, bank has resources
  - Expected output: player receives 1 of r1 and 1 of r2, bank decremented accordingly, `card.isPlayed()` returns `true`, second card cannot be played this turn

- **TC116: playYearOfPlenty_BankEmpty_ThrowsIllegalStateException** ( not implemented )
  - State of the system: valid turn in TRADE phase, valid YEAR_OF_PLENTY card, bank has 0 of requested resource
  - Expected output: `IllegalStateException`

## Method under test: `playMonopoly(Player, DevelopmentCard, ResourceType)`

- **TC117: playMonopoly_NullCard_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid turn in TRADE phase, `card = null`
  - Expected output: `IllegalArgumentException`

- **TC118: playMonopoly_ValidCard_TakesAllResourceFromOtherPlayers** ( not implemented )
  - State of the system: valid turn in TRADE phase, valid MONOPOLY card, other players have BRICK
  - Expected output: active player receives all BRICK from other players, other players have 0 BRICK, `card.isPlayed()` returns `true`, second card cannot be played this turn

- **TC119: playMonopoly_OtherPlayersHaveNone_NoChange** ( not implemented )
  - State of the system: valid turn in TRADE phase, valid MONOPOLY card, other players have 0 of resource
  - Expected output: no resources transferred, no exception thrown


