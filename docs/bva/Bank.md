# BVA for `Bank`

## Method under test: `Bank()`

- **TC1: Constructor_DefaultBank_Has19OfEachResource** ( :white_check_mark: )
    - **State of the system**: `Bank()` constructed with no arguments
    - **Expected output**: `getResourceCount()` returns `19` for each `ResourceType`

## Method under test: `getResourceCount(ResourceType)`

- **TC2: GetResourceCount_WithNullType_ThrowsIllegalArgumentException** ( :white_check_mark: )
    - **State of the system**: valid Bank, `null` passed as resource type
    - **Expected output**: `IllegalArgumentException`

- **TC3: GetResourceCount_WithValidType_ReturnsCurrentCount** ( :white_check_mark: )
    - **State of the system**: Bank with 5 BRICK
    - **Expected output**: `5`

## Method under test: `deduct(ResourceType, int)`

- **TC4: Deduct_WithNullType_ThrowsIllegalArgumentException** ( :white_check_mark: )
    - **State of the system**: valid Bank, `null` type
    - **Expected output**: `IllegalArgumentException`

- **TC5: Deduct_WithNegativeAmount_ThrowsIllegalArgumentException** ( :white_check_mark: )
    - **State of the system**: valid Bank, amount = -1
    - **Expected output**: `IllegalArgumentException`

- **TC6: Deduct_WithZeroAmount_NoChange** ( :white_check_mark: )
    - **State of the system**: Bank with 5 BRICK, amount = 0
    - **Expected output**: `getResourceCount(BRICK)` still returns `5`

- **TC7: Deduct_ExactlyAvailableAmount_LeavesZero** ( :white_check_mark: )
    - **State of the system**: Bank with 5 BRICK, amount = 5 (at boundary)
    - **Expected output**: `getResourceCount(BRICK)` returns `0`

- **TC8: Deduct_OneMoreThanAvailable_ThrowsIllegalArgumentException** ( :white_check_mark: )
    - **State of the system**: Bank with 5 BRICK, amount = 6 (one above boundary)
    - **Expected output**: `IllegalArgumentException`

## Method under test: `Bank(Map<ResourceType, Integer>)` (package-private)

- **TC9: PackagePrivateConstructor_WithMissingResourceType_ThrowsIllegalArgumentException** ( :white_check_mark: )
    - **State of the system**: map contains only BRICK (missing LUMBER, ORE, GRAIN, WOOL)
    - **Expected output**: `IllegalArgumentException` (all resource types must be present to prevent
      `NullPointerException` on auto-unboxing in `getResourceCount`)
