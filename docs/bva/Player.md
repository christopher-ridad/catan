# BVA Analysis: Player

## Method under test: `Player(String name, PlayerColor color)`

- **TC1: constructor_WithNullName_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: constructor is passed null name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC2: constructor_WithEmptyStringForName_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: constructor is passed empty string for name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC3: constructor_WithSpacesForName_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: constructor is passed a string consisting of only whitespace for the name and PlayerColor color 
    - Expected output: `IllegalArgumentException`

- **TC4: constructor_WithNullColor_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: constructor is passed string name and null color
    - Expected output: `IllegalArgumentException`

- **TC5: constructor_WithNameLengthGreaterThanMax_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: constructor is passed string name with length 51 characters and PlayerColor color
    - Expected output: `IllegalArgumentException`
  
- **TC6: constructor_WithNameLengthEqualToMax_NoExceptionThrown** ( implemented )
    - State of the system: constructor is passed string name with length 50 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC7: constructor_WithNameLengthLessThanMax_NoExceptionThrown** ( implemented )
    - State of the system: constructor is passed string name with length 49 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC8: constructor_WithCorrectName_NoExceptionThrown** ( implemented )
    - State of the system: constructor is passed string name of correct length and non-null PlayerColor color
    - Expected output: no exception thrown

## Method under test: `getName()`

- **TC9: getName_OnValidPlayer_ReturnsPlayerNameString** ( implemented )
    - State of the system: Player constructed successfully
    - Expected output: player name string

## Method under test: `getColor()`

- **TC10: getColor_OnValidPlayer_ReturnsPlayerColor** ( implemented )
    - State of the system: Player constructed successfully
    - Expected output: valid PlayerColor object

## Method under test: `getResourceCount(ResourceType type)`

- **TC11: getResourceCount_brickType_ReturnsZero** ( implemented )
  - State of the system: getResourceCount called with brick as ResourceType input, player has 0 brick
  - Expected output: 0

- **TC12: getResourceCount_lumberType_ReturnsOne** ( implemented )
    - State of the system: getResourceCount called with lumber as ResourceType input, player has 1 lumber
    - Expected output: 1

- **TC13: getResourceCount_woolType_ReturnsMoreThanOne** ( implemented )
    - State of the system: getResourceCount called with wool as ResourceType input, player has 3 wool
    - Expected output: 3

- **TC14: getResourceCount_oreType_ReturnsMaxPossibleInteger** ( implemented )
    - State of the system: getResourceCount called with ore as ResourceType input, player has INT_MAX ore
    - Expected output: INT_MAX

- **TC15: getResourceCount_nullResourceType_ThrowsIllegalArgumentException** ( implemented )
    - State of the system: getResourceCount called with null as ResourceType input
    - Expected output:`IllegalArgumentException`

## Method under test: `addResources(ResourceType type, int amount)`

- **TC16: addResources_nullResourceType_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: addResources called with null as ResourceType input
  - Expected output: `IllegalArgumentException`

- **TC17: addResources_woolType_amountLessThanZero_ThrowsIllegalArgumentException** ( implemented )
  - State of the system: addResources called with -1 as amount integer value, wool as resourceType input
  - Expected output: `IllegalArgumentException`

- **TC18: addResources_brickType_amountZero_noExceptionThrown** ( implemented )
  - State of the system: addResources called with 0 as amount integer value and brick as ResourceType input
  - Expected output: no exception thrown

- **TC19: addResources_lumberType_amountOne_noExceptionThrown** ( implemented )
  - State of the system: addResources called with 1 as amount integer value and lumber as ResourceType input
  - Expected output: no exception thrown

- **TC20: addResources_grainType_amountTwo_throwsIllegalArgumentException** ( implemented )
  - State of the system: addResources called with 2 as amount integer value and grain as ResourceType input
  - Expected output: `IllegalArgumentException`

## Method under test: `getTotalResourceCount()`

- **TC21: getTotalResourceCount_ReturnsZero** ( implemented )
  - State of the system: getTotalResourceCount called on a player with no resources (each resource is mapped to 0)
  - Expected output: 0

- **TC22: getTotalResourceCount_ReturnsOne** ( implemented )
  - State of the system: getTotalResourceCount called on a player with only one resource card
  - Expected output: 1

- **TC23: getTotalResourceCount_MaxPossibleValue** ( implemented )
  - State of the system: getTotalResourceCount called on a player with resources that sum to INT_MAX
  - Expected output: INT_MAX