# BVA Analysis for DevelopmentCard

## Method under test: `DevelopmentCard(DevelopmentCardType type)`

- **TC1: Constructor_WithNullType_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: `type = null`
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithValidType_NoExceptionThrown** ( not implemented )
    - State of the system: `type = KNIGHT`
    - Expected output: no exception thrown

- **TC3: Constructor_NewCard_IsNotPlayed** ( not implemented )
    - State of the system: `type = KNIGHT`
    - Expected output: `isPlayed()` returns `false`

## Method under test: `getType()`

- **TC4: GetType_ReturnsCorrectType** ( not implemented )
    - State of the system: `DevelopmentCard(ROAD_BUILDING)`
    - Expected output: `ROAD_BUILDING`