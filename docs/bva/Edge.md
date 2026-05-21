# BVA Analysis for Edge

## Method under test: `Edge(int id, Vertex endpoint1, Vertex endpoint2)`

- **TC1: Constructor_WithNegativeId_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: id = -1, valid endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithLowerBoundaryId_NoExceptionThrown** ( not implemented )
    - State of the system: id = 0, valid endpoint1, valid endpoint2
    - Expected output: no exception thrown

- **TC3: Constructor_WithUpperBoundaryId_NoExceptionThrown** ( not implemented )
    - State of the system: id = 71, valid endpoint1, valid endpoint2
    - Expected output: no exception thrown

- **TC4: Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: id = 72, valid endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithNullEndpoint1_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: valid id, null endpoint1, valid endpoint2
    - Expected output: `IllegalArgumentException`

- **TC6: Constructor_WithNullEndpoint2_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: valid id, valid endpoint1, null endpoint2
    - Expected output: `IllegalArgumentException`