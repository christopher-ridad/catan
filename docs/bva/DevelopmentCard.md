# BVA Analysis for DevelopmentCard

## Method under test: `DevelopmentCard(DevelopmentCardType type)`

**Step 4: Test Cases (Each-Choice)**

- **TC1: Constructor_WithNullType_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: `type = null`
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithValidType_NoExceptionThrown** ( not implemented )
    - State of the system: `type = KNIGHT`
    - Expected output: no exception thrown

- **TC3: Constructor_NewCard_IsNotPlayed** ( not implemented )
    - State of the system: `type = KNIGHT`
    - Expected output: `isPlayed()` returns `false`