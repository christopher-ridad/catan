# BVA Analysis for Vertex

## Method under test: `Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices)`

- **TC1: Constructor_WithNegativeId_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: id = -1, valid adjacentHexes, valid adjacentVertices
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithLowerBoundaryId_NoExceptionThrown** ( implemented )
    - State of the system: id = 0, valid adjacentHexes, valid adjacentVertices
    - Expected output: no exception thrown

- **TC3: Constructor_WithUpperBoundaryId_NoExceptionThrown** ( implemented )
    - State of the system: id = 53, valid adjacentHexes, valid adjacentVertices
    - Expected output: no exception thrown

- **TC4: Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: id = 54, valid adjacentHexes, valid adjacentVertices
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithNullAdjacentHexes_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid id, null adjacentHexes
    - Expected output: `IllegalArgumentException`

- **TC6: Constructor_WithNullAdjacentVertices_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: valid id, null adjacentVertices
    - Expected output: `IllegalArgumentException`

## Method under test: `getId()`

- **TC7: GetId_ReturnsCorrectId** ( implemented )
  - State of the system: Vertex constructed with id = 5
  - Expected output: `5`

## Method under test: `isOccupied()`

- **TC8: IsOccupied_WhenNoOwner_ReturnsFalse** ( implemented )
  - State of the system: Vertex with no owner set
  - Expected output: `false`

- **TC9: IsOccupied_WhenOwnerSet_ReturnsTrue** ( implemented )
  - State of the system: Vertex with an owner set
  - Expected output: `true`

## Method under test: `getOwner()`

- **TC10: GetOwner_WhenNoOwner_ReturnsEmpty** ( implemented )
  - State of the system: Vertex with no owner set
  - Expected output: `Optional.empty()`

- **TC11: GetOwner_WhenOwnerSet_ReturnsOwner** ( implemented )
  - State of the system: Vertex with an owner set
  - Expected output: `Optional` containing the Player object that was set as owner

## Method under test: `getAdjacentHexes()`

- **TC12: GetAdjacentHexes_ReturnsAdjacentHexes** ( implemented )
  - State of the system: Vertex constructed with a list of adjacent hexes
  - Expected output: the same list of adjacent hexes passed in the constructor

## Method under test: `getAdjacentVertices()`

- **TC13: GetAdjacentVertices_ReturnsAdjacentVertices** ( implemented )
  - State of the system: Vertex constructed with a list of adjacent vertices
  - Expected output: the same list of adjacent vertices passed in the constructor

## Method under test: `isCity()`

- **TC14: IsCity_WhenNoOwner_ReturnsFalse** ( :white_check_mark: )
  - State of the system: Vertex with no owner set
  - Expected output: `false`

- **TC15: IsCity_WhenOwnerSetButNotUpgraded_ReturnsFalse** ( :white_check_mark: )
  - State of the system: Vertex with an owner set, `upgradeToCity()` not called
  - Expected output: `false`

- **TC16: IsCity_AfterUpgradeToCity_ReturnsTrue** ( :white_check_mark: )
  - State of the system: Vertex with an owner set, `upgradeToCity()` called
  - Expected output: `true`

## Method under test: `upgradeToCity()`

- **TC17: UpgradeToCity_SetsIsCityTrue** ( implemented in TC16 )