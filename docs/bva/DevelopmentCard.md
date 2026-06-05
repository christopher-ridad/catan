# BVA Analysis for DevelopmentCard

## Method under test: `DevelopmentCard(DevelopmentCardType type)`

- **TC1: Constructor_WithNullType_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: `type = null`
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithValidType_NoExceptionThrown** ( implemented )
    - State of the system: `type = KNIGHT`
    - Expected output: no exception thrown

## Method under test: `getType()`

- **TC3: GetType_ReturnsCorrectType** ( implemented )
    - State of the system: `DevelopmentCard(ROAD_BUILDING)`
    - Expected output: `ROAD_BUILDING`

## Method under test: `isPlayed()`

- **TC4: IsPlayed_WhenNewCard_ReturnsFalse** ( implemented )
    - State of the system: `DevelopmentCard(YEAR_OF_PLENTY)`, `markAsPlayed()` not yet called
    - Expected output: `false`

- **TC5: IsPlayed_AfterMarkAsPlayed_ReturnsTrue** ( implemented )
    - State of the system: `DevelopmentCard(YEAR_OF_PLENTY)`, `markAsPlayed()` called once
    - Expected output: `true`

## Method under test: `markAsPlayed()`

- **TC6: MarkAsPlayed_OnUnplayedCard_NoExceptionThrown** ( implemented )
    - State of the system: `DevelopmentCard(MONOPOLY)`, card not yet played
    - Expected output: no exception thrown

- **TC7: MarkAsPlayed_OnAlreadyPlayedCard_ThrowsIllegalStateException** ( implemented )
    - State of the system: `DevelopmentCard(MONOPOLY)`, `markAsPlayed()` already called once
    - Expected output: `IllegalStateException`