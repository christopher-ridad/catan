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

### DESERT count boundaries

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

- **TC7: Constructor_With3Grain_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 3 GRAIN instead of 4
  - Expected output: `IllegalArgumentException`

- **TC8: Constructor_With4Grain_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 4 GRAIN
  - Expected output: no exception thrown

- **TC9: Constructor_With5Grain_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 5 GRAIN instead of 4
  - Expected output: `IllegalArgumentException`

### WOOL count boundaries

- **TC10: Constructor_With3Wool_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 3 WOOL instead of 4
  - Expected output: `IllegalArgumentException`

- **TC11: Constructor_With4Wool_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 4 WOOL
  - Expected output: no exception thrown

- **TC12: Constructor_With5Wool_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 5 WOOL instead of 4
  - Expected output: `IllegalArgumentException`

### LUMBER count boundaries

- **TC13: Constructor_With3Lumber_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 3 LUMBER instead of 4
  - Expected output: `IllegalArgumentException`

- **TC14: Constructor_With4Lumber_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 4 LUMBER
  - Expected output: no exception thrown

- **TC15: Constructor_With5Lumber_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 5 LUMBER instead of 4
  - Expected output: `IllegalArgumentException`

### ORE count boundaries

- **TC16: Constructor_With2Ore_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 2 ORE instead of 3
  - Expected output: `IllegalArgumentException`

- **TC17: Constructor_With3Ore_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 3 ORE
  - Expected output: no exception thrown

- **TC18: Constructor_With4Ore_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 4 ORE instead of 3
  - Expected output: `IllegalArgumentException`

### BRICK count boundaries

- **TC19: Constructor_With2Brick_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 2 BRICK instead of 3
  - Expected output: `IllegalArgumentException`

- **TC20: Constructor_With3Brick_NoExceptionThrown** ( not implemented )
  - State of the system: 19 hexes, exactly 3 BRICK
  - Expected output: no exception thrown

- **TC21: Constructor_With4Brick_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: 19 hexes, 4 BRICK instead of 3
  - Expected output: `IllegalArgumentException`

## Method under test: `getHexes()`
- **TC22: GetHexes_OnValidBoard_Returns19Hexes** ( not implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `19`

## Method under test: `getVertices()`

- **TC23: GetVertices_OnValidBoard_Returns54Vertices** ( not implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `54`

## Method under test: `getEdges()`

- **TC24: GetEdges_OnValidBoard_Returns72Edges** ( not implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `72`

## Method under test: `getVertex(int id)`
- **TC25: GetVertex_WithNegativeId_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid board, id = -1
  - Expected output: `IllegalArgumentException`

- **TC26: GetVertex_WithLowerBoundaryId_ReturnsVertex** ( not implemented )
  - State of the system: valid board, id = 0
  - Expected output: Vertex with id `0`

- **TC27: GetVertex_WithUpperBoundaryId_ReturnsVertex** ( not implemented )
  - State of the system: valid board, id = 53
  - Expected output: Vertex with id `53`

- **TC28: GetVertex_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid board, id = 54
  - Expected output: `IllegalArgumentException`

## Method under test: `getEdge(int id)`
- **TC29: GetEdge_WithNegativeId_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid board, id = -1
  - Expected output: `IllegalArgumentException`

- **TC30: GetEdge_WithLowerBoundaryId_ReturnsEdge** ( not implemented )
  - State of the system: valid board, id = 0
  - Expected output: Edge with id `0`

- **TC31: GetEdge_WithUpperBoundaryId_ReturnsEdge** ( not implemented )
  - State of the system: valid board, id = 71
  - Expected output: Edge with id `71`

- **TC32: GetEdge_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: valid board, id = 72
  - Expected output: `IllegalArgumentException`

## Method under test: `getHexCount(ResourceType)`
- **TC33: GetHexCount_ForDesert_ReturnsOne** ( not implemented )
  - State of the system: valid board
  - Expected output: `1`

- **TC34: GetHexCount_ForGrain_ReturnsFour** ( not implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC35: GetHexCount_ForWool_ReturnsFour** ( not implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC36: GetHexCount_ForLumber_ReturnsFour** ( not implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC37: GetHexCount_ForOre_ReturnsThree** ( not implemented )
  - State of the system: valid board
  - Expected output: `3`

- **TC38: GetHexCount_ForBrick_ReturnsThree** ( not implemented )
  - State of the system: valid board
  - Expected output: `3`

## Method under test: `satisfiesDistanceRule(Vertex)`
- **TC39: SatisfiesDistanceRule_WhenAllNeighborsEmpty_ReturnsTrue** ( not implemented )
  - State of the system: valid board, target vertex has no occupied neighbors
  - Expected output: `true`

- **TC40: SatisfiesDistanceRule_WhenOneNeighborOccupied_ReturnsFalse** ( not implemented )
  - State of the system: valid board, one adjacent vertex is occupied
  - Expected output: `false`

## Method under test: `isConnectedToPlayer(Vertex, Player)`
- **TC41: IsConnectedToPlayer_WhenNoAdjacentRoads_ReturnsFalse** ( not implemented )
  - State of the system: valid board, no edges adjacent to vertex have roads
  - Expected output: `false`

- **TC42: IsConnectedToPlayer_WhenAdjacentRoadOwnedByPlayer_ReturnsTrue** ( not implemented )
  - State of the system: valid board, one adjacent edge has a road owned by the player
  - Expected output: `true`

- **TC43: IsConnectedToPlayer_WhenAdjacentRoadOwnedByDifferentPlayer_ReturnsFalse** ( not implemented )
  - State of the system: valid board, adjacent edge has road owned by another player
  - Expected output: `false`


