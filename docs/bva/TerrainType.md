# BVA for `TerrainType`

## Method under test: `getResourceType()`

- **TC1: GetResourceType_ForHills_ReturnsBrick** ( :white_check_mark: )
    - **State of the system**: `TerrainType.HILLS`
    - **Expected output**: `ResourceType.BRICK`

- **TC2: GetResourceType_ForForest_ReturnsLumber** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FOREST`
    - **Expected output**: `ResourceType.LUMBER`

- **TC3: GetResourceType_ForMountains_ReturnsOre** ( :white_check_mark: )
    - **State of the system**: `TerrainType.MOUNTAINS`
    - **Expected output**: `ResourceType.ORE`

- **TC4: GetResourceType_ForFields_ReturnsGrain** ( :white_check_mark: )
    - **State of the system**: `TerrainType.FIELDS`
    - **Expected output**: `ResourceType.GRAIN`

- **TC5: GetResourceType_ForPasture_ReturnsWool** ( :white_check_mark: )
    - **State of the system**: `TerrainType.PASTURE`
    - **Expected output**: `ResourceType.WOOL`

- **TC6: GetResourceType_ForDesert_ThrowsIllegalStateException** ( :white_check_mark: )
    - **State of the system**: `TerrainType.DESERT`
    - **Expected output**: `IllegalStateException` (DESERT produces no resource; callers must check `producesResource()` first)
