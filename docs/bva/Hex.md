# BVA for `Hex`

### Method under test: `Hex(TerrainType, int)`

- **TC1: hexConstructor_tokenBelowMin_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 1`
    - **Expected output**: `IllegalArgumentException`

- **TC2: hexConstructor_tokenAtMin_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 2`
    - **Expected output**: Accept (no exception)

- **TC3: hexConstructor_tokenAtTopOfLowerRange_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 6`
    - **Expected output**: Accept (no exception)

- **TC4: hexConstructor_tokenAtGap_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 7`
    - **Expected output**: `IllegalArgumentException`

- **TC5: hexConstructor_tokenAtBottomOfUpperRange_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 8`
    - **Expected output**: Accept (no exception)

- **TC6: hexConstructor_tokenAtMax_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 12`
    - **Expected output**: Accept (no exception)

- **TC7: hexConstructor_tokenAboveMax_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 13`
    - **Expected output**: `IllegalArgumentException`

- **TC8: hexConstructor_desertWithNoToken_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: `TerrainType.DESERT`, `numberToken = 0`
    - **Expected output**: Accept (no exception)

- **TC9: hexConstructor_desertWithToken_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `TerrainType.DESERT`, `numberToken = 8`
    - **Expected output**: `IllegalArgumentException`

- **TC10: hexConstructor_nonDesertWithNoToken_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`, `numberToken = 0`
    - **Expected output**: `IllegalArgumentException`

### Method under test: `getNumberToken()`

- **TC11: getNumberToken_onNonDesertHex_returnsToken** ( :white_check_mark: )
  - **State of the system**: `Hex(TerrainType.FOREST, 6)`
  - **Expected output**: `6`

- **TC12: getNumberToken_onDesertHex_returnsZero** ( :white_check_mark: )
  - **State of the system**: `Hex(TerrainType.DESERT, 0)`
  - **Expected output**: `0`

### Method under test: `isDesert()`

- **TC13: isDesert_onDesertHex_returnsTrue** ( :x: )
  - **State of the system**: `Hex(TerrainType.DESERT, 0)`
  - **Expected output**: `true`

- **TC14: isDesert_onNonDesertHex_returnsFalse** ( :x: )
  - **State of the system**: `Hex(TerrainType.FOREST, 6)`
  - **Expected output**: `false`

### Method under test: `producesResource()`

- **TC15: producesResource_onNonDesertHex_returnsTrue** ( :x: )
  - **State of the system**: `Hex(TerrainType.FOREST, 6)`
  - **Expected output**: `true`

- **TC16: producesResource_onDesertHex_returnsFalse** ( :x: )
  - **State of the system**: `Hex(TerrainType.DESERT, 0)`
  - **Expected output**: `false`