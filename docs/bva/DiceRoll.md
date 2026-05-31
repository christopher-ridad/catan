# BVA for `DiceRoll`

## Method under test: `DiceRoll(Random)`

- **TC1: constructor_withNullRandom_throwsNullPointerException** ( :white_check_mark: )
    - **State of the system**: `null` passed as random
    - **Expected output**: `NullPointerException`

- **TC2: constructor_withValidRandom_doesNotThrow** ( :white_check_mark: )
    - **State of the system**: valid `Random` instance passed
    - **Expected output**: no exception thrown

## Method under test: `roll()`

- **TC3: roll_returnsValueAtLeast2** ( :white_check_mark: )
    - **State of the system**: `DiceRoll` with real `Random`, `roll()` called 1000 times
    - **Expected output**: every returned value >= 2

- **TC4: roll_returnsValueAtMost12** ( :white_check_mark: )
    - **State of the system**: `DiceRoll` with real `Random`, `roll()` called 1000 times
    - **Expected output**: every returned value <= 12

- **TC5: roll_canReturnMinimumSum** ( :white_check_mark: )
    - **State of the system**: mocked `Random` where `nextInt(6)` returns `0` twice (each die = 1)
    - **Expected output**: `2`

- **TC6: roll_canReturnMaximumSum** ( :white_check_mark: )
    - **State of the system**: mocked `Random` where `nextInt(6)` returns `5` twice (each die = 6)
    - **Expected output**: `12`

- **TC7: roll_canReturn7** ( :white_check_mark: )
    - **State of the system**: mocked `Random` where `nextInt(6)` returns `2` then `3` (dice = 3, 4)
    - **Expected output**: `7` (the value that triggers robber in game turn logic)

- **TC8: roll_canReturnNon7Value** ( :white_check_mark: )
    - **State of the system**: mocked `Random` where `nextInt(6)` returns `1` then `2` (dice = 2, 3)
    - **Expected output**: `5`
