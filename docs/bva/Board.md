# BVA Analysis: Board

## Method under test: `Board(List<Hex>)`

### Hex count boundaries

- **TC1: Constructor_With18Hexes_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: list of 18 hexes with otherwise valid terrain distribution
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_With19Hexes_NoExceptionThrown** ( implemented )
    - State of the system: list of exactly 19 hexes with valid terrain distribution
    - Expected output: no exception thrown

- **TC3: Constructor_With20Hexes_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: list of 20 hexes with otherwise valid terrain distribution
    - Expected output: `IllegalArgumentException`

### DESERT count boundaries

- **TC4: Constructor_WithNoDesert_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 0 DESERT
  - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithOneDesert_NoExceptionThrown** ( implemented )
  - State of the system: 19 hexes, exactly 1 DESERT
  - Expected output: no exception thrown

- **TC6: Constructor_WithTwoDeserts_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 2 DESERT
  - Expected output: `IllegalArgumentException`

### FIELD count boundaries

- **TC7: Constructor_With3Field_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 3 FIELD instead of 4
  - Expected output: `IllegalArgumentException`

- **TC8: Constructor_With4Field_NoExceptionThrown** ( implemented )
  - State of the system: 19 hexes, exactly 4 FIELD
  - Expected output: no exception thrown

- **TC9: Constructor_With5Field_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 5 FIELD instead of 4
  - Expected output: `IllegalArgumentException`

### PASTURE count boundaries

- **TC10: Constructor_With3Pasture_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 3 PASTURE instead of 4
  - Expected output: `IllegalArgumentException`

- **TC11: Constructor_With4Pasture_NoExceptionThrown** ( implemented )
  - State of the system: 19 hexes, exactly 4 PASTURE
  - Expected output: no exception thrown

- **TC12: Constructor_With5Pasture_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 5 PASTURE instead of 4
  - Expected output: `IllegalArgumentException`

### FOREST count boundaries

- **TC13: Constructor_With3Forest_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 3 FOREST instead of 4
  - Expected output: `IllegalArgumentException`

- **TC14: Constructor_With4Forest_NoExceptionThrown** ( implemented )
  - State of the system: 19 hexes, exactly 4 FOREST
  - Expected output: no exception thrown

- **TC15: Constructor_With5Forest_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 5 FOREST instead of 4
  - Expected output: `IllegalArgumentException`

### MOUNTAINS count boundaries

- **TC16: Constructor_With2Mountains_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 2 MOUNTAINS instead of 3
  - Expected output: `IllegalArgumentException`

- **TC17: Constructor_With3Mountains_NoExceptionThrown** ( implemented )
  - State of the system: 19 hexes, exactly 3 MOUNTAINS
  - Expected output: no exception thrown

- **TC18: Constructor_With4Mountains_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: 19 hexes, 4 MOUNTAINS instead of 3
  - Expected output: `IllegalArgumentException`

### HILLS count boundaries

- **Note:** HILLS count is implicitly validated by the combination of the total hex count check (exactly 19) and the individual counts for DESERT (1), FIELDS (4), PASTURE (4), FOREST (4), and MOUNTAINS (3). Any deviation in HILLS count would necessarily violate one of these other checks first, making independent HILLS boundary tests redundant.

## Method under test: `getHexes()`
- **TC19: GetHexes_OnValidBoard_Returns19Hexes** ( implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `19`

## Method under test: `getVertices()`

- **TC20: GetVertices_OnValidBoard_Returns54Vertices** ( implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `54`

## Method under test: `getEdges()`

- **TC21: GetEdges_OnValidBoard_Returns72Edges** ( implemented )
  - State of the system: Board constructed successfully
  - Expected output: list of size `72`

## Method under test: `getVertex(int id)`
- **TC22: GetVertex_WithNegativeId_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: valid board, id = -1
  - Expected output: `IllegalArgumentException`

- **TC23: GetVertex_WithLowerBoundaryId_ReturnsVertex** ( implemented )
  - State of the system: valid board, id = 0
  - Expected output: Vertex with id `0`

- **TC24: GetVertex_WithUpperBoundaryId_ReturnsVertex** ( implemented )
  - State of the system: valid board, id = 53
  - Expected output: Vertex with id `53`

- **TC25: GetVertex_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: valid board, id = 54
  - Expected output: `IllegalArgumentException`

## Method under test: `getEdge(int id)`
- **TC26: GetEdge_WithNegativeId_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: valid board, id = -1
  - Expected output: `IllegalArgumentException`

- **TC27: GetEdge_WithLowerBoundaryId_ReturnsEdge** ( implemented )
  - State of the system: valid board, id = 0
  - Expected output: Edge with id `0`

- **TC28: GetEdge_WithUpperBoundaryId_ReturnsEdge** ( implemented )
  - State of the system: valid board, id = 71
  - Expected output: Edge with id `71`

- **TC29: GetEdge_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: valid board, id = 72
  - Expected output: `IllegalArgumentException`

## Method under test: `getHexCount(TerrainType)`
- **TC30: GetHexCount_ForDesert_ReturnsOne** ( implemented )
  - State of the system: valid board
  - Expected output: `1`

- **TC31: GetHexCount_ForField_ReturnsFour** ( implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC32: GetHexCount_ForPasture_ReturnsFour** ( implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC33: GetHexCount_ForForest_ReturnsFour** ( implemented )
  - State of the system: valid board
  - Expected output: `4`

- **TC34: GetHexCount_ForMountains_ReturnsThree** ( implemented )
  - State of the system: valid board
  - Expected output: `3`

- **TC35: GetHexCount_ForHills_ReturnsThree** ( implemented )
  - State of the system: valid board
  - Expected output: `3`

## Method under test: `satisfiesDistanceRule(Vertex)`
- **TC36: SatisfiesDistanceRule_WhenAllNeighborsEmpty_ReturnsTrue** ( implemented )
  - State of the system: valid board, target vertex has no occupied neighbors
  - Expected output: `true`

- **TC37: SatisfiesDistanceRule_WhenOneNeighborOccupied_ReturnsFalse** ( implemented )
  - State of the system: valid board, one adjacent vertex is occupied
  - Expected output: `false`

- **TC42: SatisfiesDistanceRule_WhenTargetVertexIsOccupied_ReturnsFalse** ( :white_check_mark: )
  - State of the system: valid board, the target vertex itself already has a settlement (no neighbors occupied)
  - Expected output: `false` (cannot place a second settlement on an already-occupied vertex)

## Method under test: `isConnectedToPlayer(Vertex, Player)`
- **TC38: IsConnectedToPlayer_WhenNoAdjacentRoads_ReturnsFalse** ( implemented )
  - State of the system: valid board, no edges adjacent to vertex have roads
  - Expected output: `false`

- **TC39: IsConnectedToPlayer_WhenAdjacentRoadOwnedByPlayer_ReturnsTrue** ( implemented )
  - State of the system: valid board, one adjacent edge has a road owned by the player
  - Expected output: `true`

- **TC40: IsConnectedToPlayer_WhenAdjacentRoadOwnedByDifferentPlayer_ReturnsFalse** ( implemented )
  - State of the system: valid board, adjacent edge has road owned by another player
  - Expected output: `false`

- **TC41: IsConnectedToPlayer_WhenRoadOnNonAdjacentEdge_ReturnsFalse** ( implemented )
  - State of the system: valid board, edge connects two other vertices (not the target vertex) but has a road owned by the player
  - Expected output: `false`

## Method under test: `getHarborType(Vertex)`

- **TC42: getHarborType_ForNonHarborVertex_ReturnsEmpty** ( implemented )
  - **State of the system**: Valid board constructed, vertex 4 requested (non-harbor inner vertex)
  - **Expected output**: `Optional.empty()`

- **TC43: getHarborType_ForGenericHarborVertex_ReturnsGeneric** ( implemented )
  - **State of the system**: Valid board constructed, vertex 0 requested
  - **Expected output**: `Optional` containing `HarborType.GENERIC`

- **TC44: getHarborType_ForGrainHarborVertex_ReturnsGrain** ( implemented )
  - **State of the system**: Valid board constructed, vertex 1 requested
  - **Expected output**: `Optional` containing `HarborType.GRAIN`

- **TC45: getHarborType_ForOreHarborVertex_ReturnsOre** ( implemented )
  - **State of the system**: Valid board constructed, vertex 10 requested
  - **Expected output**: `Optional` containing `HarborType.ORE`

- **TC46: getHarborType_ForWoolHarborVertex_ReturnsWool** ( implemented )
  - **State of the system**: Valid board constructed, vertex 42 requested
  - **Expected output**: `Optional` containing `HarborType.WOOL`

- **TC47: getHarborType_ForBrickHarborVertex_ReturnsBrick** ( implemented )
  - **State of the system**: Valid board constructed, vertex 33 requested
  - **Expected output**: `Optional` containing `HarborType.BRICK`

- **TC48: getHarborType_ForLumberHarborVertex_ReturnsLumber** ( implemented )
  - **State of the system**: Valid board constructed, vertex 11 requested
  - **Expected output**: `Optional` containing `HarborType.LUMBER`




