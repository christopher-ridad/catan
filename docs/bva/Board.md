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
