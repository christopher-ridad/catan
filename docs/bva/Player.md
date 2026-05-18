# BVA Analysis: Player

## Method under test: `Player(String name, PlayerColor color)`

### Name and color boundaries

- **TC1: Constructor_WithNullName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed null name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC2: Constructor_WithEmptyStringForName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed empty string for name, PlayerColor color is present
    - Expected output: `IllegalArgumentException`

- **TC3: Constructor_WithSpacesForName_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed a string consisting of only whitespace for the name and PlayerColor color 
    - Expected output: `IllegalArgumentException`

- **TC4: Constructor_WithNullColor_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed string name and null color
    - Expected output: `IllegalArgumentException`

- **TC5: Constructor_WithNameLengthGreaterThanMax_ThrowsIllegalArgumentException** ( not implemented )
    - State of the system: constructor is passed string name with length 51 characters and PlayerColor color
    - Expected output: `IllegalArgumentException`
  
- **TC6: Constructor_WithNameLengthEqualToMax_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name with length 50 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC7: Constructor_WithNameLengthLessThanMax_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name with length 49 characters and PlayerColor color
    - Expected output: no exception thrown

- **TC8: Constructor_WithCorrectName_NoExceptionThrown** ( not implemented )
    - State of the system: constructor is passed string name of correct length and non-null PlayerColor color
    - Expected output: no exception thrown

