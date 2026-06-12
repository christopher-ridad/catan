# BVA Analysis for DevelopmentDeck

## Method under test: `DevelopmentDeck()`

- **TC1: Constructor_CreatesFullDeck_HasTwentyFiveCards** ( not implemented )
  - State of the system: `DevelopmentDeck()` constructed with no arguments
  - Expected output: `getRemainingCount()` returns `25`

## Method under test: `draw(int currentTurn)`

- **TC2: Draw_OnFullDeck_ReturnsCard** ( not implemented )
  - State of the system: full deck, `currentTurn = 1`
  - Expected output: returns a `DevelopmentCard`, `getRemainingCount()` returns `24`

- **TC3: Draw_SetsCorrectTurnPurchased** ( not implemented )
  - State of the system: full deck, `currentTurn = 3`
  - Expected output: returned card has `getTurnPurchased()` returning `3`

- **TC4: Draw_OnEmptyDeck_ThrowsIllegalStateException** ( not implemented )
  - State of the system: all 25 cards have been drawn
  - Expected output: `IllegalStateException`

## Method under test: `isEmpty()`

- **TC5: IsEmpty_OnFullDeck_ReturnsFalse** ( not implemented )
  - State of the system: full deck
  - Expected output: `false`

- **TC6: IsEmpty_AfterAllCardsDrawn_ReturnsTrue** ( not implemented )
  - State of the system: all 25 cards drawn
  - Expected output: `true`

## Method under test: `getRemainingCount()`

- **TC7: GetRemainingCount_OnFullDeck_Returns25** ( not implemented )
  - State of the system: full deck
  - Expected output: `25`

- **TC8: GetRemainingCount_AfterOneDraw_Returns24** ( not implemented )
  - State of the system: full deck, one card drawn
  - Expected output: `24`

- **TC9: GetRemainingCount_AfterAllDrawn_ReturnsZero** ( not implemented )
  - State of the system: all 25 cards drawn
  - Expected output: `0`