# BVA for `Hex`

### Method under test: `Hex(TerrainType, int)`

- **TC1: hexConstructor_tokenBelowMin_throwsIllegalArgument** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 1`
    - **Expected output**: `IllegalArgumentException`

- **TC2: hexConstructor_tokenAtMin_doesNotThrow** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 2`
    - **Expected output**: Accept (no exception)

- **TC3: hexConstructor_tokenAtTopOfLowerRange_doesNotThrow** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 6`
    - **Expected output**: Accept (no exception)

- **TC4: hexConstructor_tokenAtGap_throwsIllegalArgument** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 7`
    - **Expected output**: `IllegalArgumentException`

- **TC5: hexConstructor_tokenAtBottomOfUpperRange_doesNotThrow** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 8`
    - **Expected output**: Accept (no exception)

- **TC6: hexConstructor_tokenAtMax_doesNotThrow** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 12`
    - **Expected output**: Accept (no exception)

- **TC7: hexConstructor_tokenAboveMax_throwsIllegalArgument** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 13`
    - **Expected output**: `IllegalArgumentException`

- **TC8: hexConstructor_desertWithNoToken_doesNotThrow** ( :x: )
    - **State of the system**: `TerrainType.DESERT`, `numberToken = 0`
    - **Expected output**: Accept (no exception)

- **TC9: hexConstructor_desertWithToken_throwsIllegalArgument** ( :x: )
    - **State of the system**: `TerrainType.DESERT`, `numberToken = 8`
    - **Expected output**: `IllegalArgumentException`

- **TC10: hexConstructor_nonDesertWithNoToken_throwsIllegalArgument** ( :x: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 0`
    - **Expected output**: `IllegalArgumentException`