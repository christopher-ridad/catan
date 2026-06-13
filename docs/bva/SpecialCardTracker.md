# BVA Analysis: SpecialCardTracker

## Method under test: `SpecialCardTracker()`

- **TC1: Constructor_InitialState_LongestRoadHolderIsEmpty** ( not implemented )
    - State of the system: tracker just constructed
    - Expected output: `getLongestRoadHolder()` returns `Optional.empty()`

- **TC2: Constructor_InitialState_LongestRoadLengthIsZero** ( not implemented )
    - State of the system: tracker just constructed
    - Expected output: `getLongestRoadLength()` returns `0`

- **TC3: Constructor_InitialState_LargestArmyHolderIsEmpty** ( not implemented )
    - State of the system: tracker just constructed
    - Expected output: `getLargestArmyHolder()` returns `Optional.empty()`

- **TC4: Constructor_InitialState_LargestArmySizeIsZero** ( not implemented )
    - State of the system: tracker just constructed
    - Expected output: `getLargestArmySize()` returns `0`

## Method under test: `updateLongestRoad(Player candidate, int roadLength)`

- **TC5: UpdateLongestRoad_WithNegativeRoadLength_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: `updateLongestRoad(player, -1)` called on a fresh tracker
    - Expected output: `IllegalArgumentException`

- **TC6: UpdateLongestRoad_WithZeroRoadLength_RemainsUnclaimed** ( not implemented )
    - State of the system: `updateLongestRoad(player, 0)` called on a fresh tracker
    - Expected output: no exception thrown; Longest Road remains unclaimed (`getLongestRoadHolder()` empty, `getLongestRoadLength()` returns `0`)

- **TC7: UpdateLongestRoad_WithRoadLengthBelowClaimThreshold_RemainsUnclaimed** ( not implemented )
    - State of the system: `updateLongestRoad(player, 4)` called on a fresh tracker (Longest Road unclaimed)
    - Expected output: Longest Road remains unclaimed (`getLongestRoadHolder()` empty, `getLongestRoadLength()` returns `0`)

- **TC8: UpdateLongestRoad_WithRoadLengthAtClaimThreshold_ClaimsCard** ( not implemented )
    - State of the system: `updateLongestRoad(player, 5)` called on a fresh tracker (Longest Road unclaimed)
    - Expected output: `player` becomes the Longest Road holder; `getLongestRoadLength()` returns `5`

- **TC9: UpdateLongestRoad_WhenCandidateTiesCurrentHolder_DoesNotSteal** ( not implemented )
    - State of the system: `holder` claimed Longest Road with length `6`; `updateLongestRoad(challenger, 6)` called
    - Expected output: `holder` remains the Longest Road holder; `getLongestRoadLength()` returns `6`

- **TC10: UpdateLongestRoad_WhenCandidateExceedsCurrentHolderByOne_StealsCard** ( not implemented )
    - State of the system: `holder` claimed Longest Road with length `5`; `updateLongestRoad(challenger, 6)` called
    - Expected output: `challenger` becomes the Longest Road holder; `getLongestRoadLength()` returns `6`

- **TC11: UpdateLongestRoad_WhenSameHolderIncreasesRoadLength_UpdatesLength** ( not implemented )
    - State of the system: `holder` claimed Longest Road with length `5`; `updateLongestRoad(holder, 7)` called
    - Expected output: `holder` remains the Longest Road holder; `getLongestRoadLength()` returns `7`

## Method under test: `holdsLongestRoad(Player player)`

- **TC12: HoldsLongestRoad_BeforeClaim_ReturnsFalse** ( not implemented )
    - State of the system: fresh tracker, Longest Road unclaimed
    - Expected output: `holdsLongestRoad(player)` returns `false`

- **TC13: HoldsLongestRoad_AfterClaim_ReturnsTrueForHolderAndFalseForOthers** ( not implemented )
    - State of the system: `holder` claimed Longest Road with length `5`
    - Expected output: `holdsLongestRoad(holder)` returns `true`; `holdsLongestRoad(other)` returns `false`

## Method under test: `updateLargestArmy(Player candidate, int knightCount)`

- **TC14: UpdateLargestArmy_WithNegativeKnightCount_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: `updateLargestArmy(player, -1)` called on a fresh tracker
    - Expected output: `IllegalArgumentException`

- **TC15: UpdateLargestArmy_WithZeroKnightCount_RemainsUnclaimed** ( not implemented )
    - State of the system: `updateLargestArmy(player, 0)` called on a fresh tracker
    - Expected output: no exception thrown; Largest Army remains unclaimed (`getLargestArmyHolder()` empty, `getLargestArmySize()` returns `0`)

- **TC16: UpdateLargestArmy_WithKnightCountBelowClaimThreshold_RemainsUnclaimed** ( not implemented )
    - State of the system: `updateLargestArmy(player, 2)` called on a fresh tracker (Largest Army unclaimed)
    - Expected output: Largest Army remains unclaimed (`getLargestArmyHolder()` empty, `getLargestArmySize()` returns `0`)

- **TC17: UpdateLargestArmy_WithKnightCountAtClaimThreshold_ClaimsCard** ( not implemented )
    - State of the system: `updateLargestArmy(player, 3)` called on a fresh tracker (Largest Army unclaimed)
    - Expected output: `player` becomes the Largest Army holder; `getLargestArmySize()` returns `3`

- **TC18: UpdateLargestArmy_WhenCandidateTiesCurrentHolder_DoesNotSteal** ( not implemented )
    - State of the system: `holder` claimed Largest Army with count `4`; `updateLargestArmy(challenger, 4)` called
    - Expected output: `holder` remains the Largest Army holder; `getLargestArmySize()` returns `4`

- **TC19: UpdateLargestArmy_WhenCandidateExceedsCurrentHolderByOne_StealsCard** ( not implemented )
    - State of the system: `holder` claimed Largest Army with count `3`; `updateLargestArmy(challenger, 4)` called
    - Expected output: `challenger` becomes the Largest Army holder; `getLargestArmySize()` returns `4`

- **TC20: UpdateLargestArmy_WhenSameHolderIncreasesKnightCount_UpdatesCount** ( not implemented )
    - State of the system: `holder` claimed Largest Army with count `3`; `updateLargestArmy(holder, 5)` called
    - Expected output: `holder` remains the Largest Army holder; `getLargestArmySize()` returns `5`

## Method under test: `holdsLargestArmy(Player player)`

- **TC21: HoldsLargestArmy_BeforeClaim_ReturnsFalse** ( not implemented )
    - State of the system: fresh tracker, Largest Army unclaimed
    - Expected output: `holdsLargestArmy(player)` returns `false`

- **TC22: HoldsLargestArmy_AfterClaim_ReturnsTrueForHolderAndFalseForOthers** ( not implemented )
    - State of the system: `holder` claimed Largest Army with count `3`
    - Expected output: `holdsLargestArmy(holder)` returns `true`; `holdsLargestArmy(other)` returns `false`
