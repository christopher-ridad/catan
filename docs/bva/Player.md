# BVA Analysis: Player

## Method under test: `Player(String name, PlayerColor color)`

- **TC1: constructor_WithNullName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed null name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC2: constructor_WithEmptyStringForName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed empty string for name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC3: constructor_WithSpacesForName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed a string consisting of only whitespace for the name and PlayerColor color 
    - Expected output: `IllegalArgumentException`

- **TC4: constructor_WithNullColor_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed string name and null color
    - Expected output: `IllegalArgumentException`

- **TC5: constructor_WithNameLengthGreaterThanMax_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed string name with length 51 characters and PlayerColor color
    - Expected output: `IllegalArgumentException`
  
- **TC6: constructor_WithNameLengthEqualToMax_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name with length 50 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC7: constructor_WithNameLengthLessThanMax_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name with length 49 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC8: constructor_WithCorrectName_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name of correct length and non-null PlayerColor color
    - Expected output: no exception thrown

## Method under test: `getName()`

- **TC9: getName_OnValidPlayer_ReturnsPlayerNameString** ( not implemented )
    - State of the system: Player constructed successfully
    - Expected output: player name string

## Method under test: `getColor()`

- **TC10: getColor_OnValidPlayer_ReturnsPlayerColor** ( not implemented )
    - State of the system: Player constructed successfully
    - Expected output: valid PlayerColor object

## Method under test: `getResourceCount(ResourceType type)`

- **TC11: getResourceCount_brickType_ReturnsZero** ( not implemented )
  - State of the system: getResourceCount called with brick as ResourceType input, player has 0 brick
  - Expected output: 0

- **TC12: getResourceCount_lumberType_ReturnsOne** ( not implemented )
    - State of the system: getResourceCount called with lumber as ResourceType input, player has 1 lumber
    - Expected output: 1

- **TC13: getResourceCount_woolType_ReturnsMoreThanOne** ( not implemented )
    - State of the system: getResourceCount called with wool as ResourceType input, player has 3 wool
    - Expected output: 3

- **TC14: getResourceCount_oreType_ReturnsMaxPossibleInteger** ( not implemented )
    - State of the system: getResourceCount called with ore as ResourceType input, player has INT_MAX ore
    - Expected output: INT_MAX

- **TC15: getResourceCount_nullResourceType_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: getResourceCount called with null as ResourceType input
    - Expected output:`IllegalArgumentException`

## Method under test: `addResources(ResourceType type, int amount)`

- **TC16: addResources_nullResourceType_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: addResources called with null as ResourceType input
  - Expected output: `IllegalArgumentException`

- **TC17: addResources_woolType, amountLessThanZero_ThrowsIllegalArgumentException** ( not implemented )
  - State of the system: addResources called with -1 as amount integer value, wool as resourceType input
  - Expected output: `IllegalArgumentException`

- **TC18: addResources_brickType_amountZero_noExceptionThrown** ( not implemented )
  - State of the system: addResources called with 0 as amount integer value and brick as ResourceType input
  - Expected output: no exception thrown

- **TC19: addResources_lumberType_amountOne_noExceptionThrown** ( not implemented )
  - State of the system: addResources called with 1 as amount integer value and lumber as ResourceType input
  - Expected output: no exception thrown

- **TC20: addResources_grainType_amountTwo_throwsIllegalArgumentException** ( not implemented )
  - State of the system: addResources called with 2 as amount integer value and grain as ResourceType input
  - Expected output: `IllegalArgumentException`