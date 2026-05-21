# BVA Analysis for Vertex

## Method under test: `Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices)`

- **TC1: Constructor_WithNegativeId_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: id = -1, valid adjacentHexes, valid adjacentVertices
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithLowerBoundaryId_NoExceptionThrown** ( not implemented )
    - State of the system: id = 0, valid adjacentHexes, valid adjacentVertices
    - Expected output: no exception thrown

- **TC3: Constructor_WithUpperBoundaryId_NoExceptionThrown** ( not implemented )
    - State of the system: id = 53, valid adjacentHexes, valid adjacentVertices
    - Expected output: no exception thrown

- **TC4: Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: id = 54, valid adjacentHexes, valid adjacentVertices
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithNullAdjacentHexes_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: valid id, null adjacentHexes
    - Expected output: `IllegalArgumentException`

- **TC6: Constructor_WithNullAdjacentVertices_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: valid id, null adjacentVertices
    - Expected output: `IllegalArgumentException`