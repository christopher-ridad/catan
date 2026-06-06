# BVA Analysis for Edge

## Method under test: `Edge(int id, Vertex endpoint1, Vertex endpoint2)`

- **TC1: Constructor_WithNegativeId_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: id = -1, valid endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithLowerBoundaryId_NoExceptionThrown** ( implemented )
    - State of the system: id = 0, valid endpoint1, valid endpoint2
    - Expected output: no exception thrown

- **TC3: Constructor_WithUpperBoundaryId_NoExceptionThrown** ( implemented )
    - State of the system: id = 71, valid endpoint1, valid endpoint2
    - Expected output: no exception thrown

- **TC4: Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: id = 72, valid endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithNullEndpoint1_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid id, null endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC6: Constructor_WithNullEndpoint2_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid id, valid endpoint1, null endpoint2
    - Expected output: `IllegalArgumentException`

## Method under test: `getId()`

- **TC7: GetId_ReturnsCorrectId** ( implemented )
  - State of the system: Edge constructed with id = 5
  - Expected output: `5`


## Method under test: `hasRoad()`

- **TC8: HasRoad_WhenNoRoad_ReturnsFalse** ( implemented )
  - State of the system: Edge with no road placed
  - Expected output: `false`

- **TC9: HasRoad_WhenRoadPlaced_ReturnsTrue** ( implemented )
  - State of the system: Edge with a road placed
  - Expected output: `true`

## Method under test: `getOwner()`

- **TC10: GetOwner_WhenNoRoad_ReturnsEmpty** ( implemented )
  - State of the system: Edge with no road placed
  - Expected output: `Optional.empty()`

- **TC11: GetOwner_WhenRoadPlaced_ReturnsOwner** ( implemented )
  - State of the system: Edge with a road placed by a player
  - Expected output: `Optional` containing the Player object that placed the road


## Method under test: `getEndpoints()`

- **TC12: GetEndpoints_ReturnsCorrectEndpoints** ( implemented )
  - State of the system: Edge constructed with two valid endpoints
  - Expected output: array containing endpoint1 and endpoint2

## Method under test: `connectsTo(Vertex)`

- **TC13: ConnectsTo_WhenVertexIsEndpoint1_ReturnsTrue** ( implemented )
  - State of the system: Edge constructed with endpoint1, checking connectsTo(endpoint1)
  - Expected output: `true`

- **TC14: ConnectsTo_WhenVertexIsEndpoint2_ReturnsTrue** ( implemented )
  - State of the system: Edge constructed with endpoint2, checking connectsTo(endpoint2)
  - Expected output: `true`

- **TC15: ConnectsTo_WhenVertexIsNotEndpoint_ReturnsFalse** ( implemented )
  - State of the system: Edge constructed with two endpoints, checking connectsTo with a different vertex
  - Expected output: `false`