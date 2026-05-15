# BVA Analysis: Board

## Method under test: `Board(List<Hex>)`

### Hex count boundaries

- **TC1: Constructor_With18Hexes_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: list of 18 hexes with otherwise valid resource distribution
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_With19Hexes_NoExceptionThrown** ( not implemented )
    - State of the system: list of exactly 19 hexes with valid resource distribution
    - Expected output: no exception thrown

- **TC3: Constructor_With20Hexes_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: list of 20 hexes with otherwise valid resource distribution
    - Expected output: `IllegalArgumentException`

### Desert count boundaries

- **TC4: Constructor_WithNoDesert_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 0 DESERT
  - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithOneDesert_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 1 DESERT
  - Expected output: no exception thrown

- **TC6: Constructor_WithTwoDeserts_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 2 DESERT
  - Expected output: `IllegalArgumentException`

### GRAIN count boundaries

- **TC8: Constructor_With3Grain_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 3 GRAIN instead of 4
  - Expected output: `IllegalArgumentException`

- **TC9: Constructor_With4Grain_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 4 GRAIN
  - Expected output: no exception thrown

- **TC10: Constructor_With5Grain_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 5 GRAIN instead of 4
  - Expected output: `IllegalArgumentException`
