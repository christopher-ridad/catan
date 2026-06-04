# BVA for `MaritimeTrade`

### Method under test: `MaritimeTrade(Player, ResourceType, int, ResourceType, Board)`

- **TC-01: maritimeTradeConstructor_givingEqualsReceiving_throwsIllegalArgument** ( :x: )
    - **State of the system**: `giving == receiving` (e.g. both `BRICK`), player has sufficient cards, no harbor
    - **Expected output**: `IllegalArgumentException`

- **TC-02: maritimeTradeConstructor_playerCannotAffordGiving_throwsIllegalArgument** ( :x: )
    - **State of the system**: No harbor (rate = 4), player has 3 `BRICK`, `giving = BRICK`, `amount = 4`
    - **Expected output**: `IllegalArgumentException`

- **TC-03: maritimeTradeConstructor_playerHasExactlyRequiredAmount_noHarbor_objectCreated** ( :x: )
    - **State of the system**: No harbor (rate = 4), player has exactly 4 `BRICK`, `giving = BRICK`, `amount = 4`, `receiving = LUMBER`
    - **Expected output**: Object created successfully

- **TC-04: maritimeTradeConstructor_amountBelowRateNoHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: No harbor (rate = 4), player has 4 `BRICK`, `giving = BRICK`, `amount = 3`
    - **Expected output**: `IllegalArgumentException`

- **TC-05: maritimeTradeConstructor_amountAboveRateNoHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: No harbor (rate = 4), player has 5 `BRICK`, `giving = BRICK`, `amount = 5`
    - **Expected output**: `IllegalArgumentException`

- **TC-06: maritimeTradeConstructor_playerCannotAffordGiving_genericHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: Player has generic harbor (rate = 3), player has 2 `BRICK`, `giving = BRICK`, `amount = 3`
    - **Expected output**: `IllegalArgumentException`

- **TC-07: maritimeTradeConstructor_playerHasExactlyRequiredAmount_genericHarbor_objectCreated** ( :x: )
    - **State of the system**: Player has generic harbor (rate = 3), player has exactly 3 `BRICK`, `giving = BRICK`, `amount = 3`, `receiving = LUMBER`
    - **Expected output**: Object created successfully

- **TC-08: maritimeTradeConstructor_amountBelowRateGenericHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: Player has generic harbor (rate = 3), player has 3 `BRICK`, `giving = BRICK`, `amount = 2`
    - **Expected output**: `IllegalArgumentException`

- **TC-09: maritimeTradeConstructor_amountAboveRateGenericHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: Player has generic harbor (rate = 3), player has 4 `BRICK`, `giving = BRICK`, `amount = 4`
    - **Expected output**: `IllegalArgumentException`

- **TC-10: maritimeTradeConstructor_playerCannotAffordGiving_specialHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: Player has special `BRICK` harbor (rate = 2), player has 1 `BRICK`, `giving = BRICK`, `amount = 2`
    - **Expected output**: `IllegalArgumentException`

- **TC-11: maritimeTradeConstructor_playerHasExactlyRequiredAmount_specialHarbor_objectCreated** ( :x: )
    - **State of the system**: Player has special `BRICK` harbor (rate = 2), player has exactly 2 `BRICK`, `giving = BRICK`, `amount = 2`, `receiving = LUMBER`
    - **Expected output**: Object created successfully

- **TC-12: maritimeTradeConstructor_amountBelowRateSpecialHarbor_throwsIllegalArgument** ( :x: )
    - **State of the system**: Player has special `BRICK` harbor (rate = 2), player has 2 `BRICK`, `giving = BRICK`, `amount = 1`
    - **Expected output**: `IllegalArgumentException`

- **TC-13: maritimeTradeConstructor_specialHarborDoesNotApplyToOtherResource_usesDefaultRate** ( :x: )
    - **State of the system**: Player has special `BRICK` harbor only, `giving = LUMBER`, player has 4 `LUMBER`, `amount = 4`, `receiving = BRICK`
    - **Expected output**: Object created successfully (rate falls back to 4)

- **TC-14: maritimeTradeConstructor_genericHarborBeatsDefault_usesGenericRate** ( :x: )
    - **State of the system**: Player has generic harbor (rate = 3), `giving = LUMBER`, player has 3 `LUMBER`, `amount = 3`, `receiving = BRICK`
    - **Expected output**: Object created successfully (generic harbor rate used over default 4)

- **TC-15: maritimeTradeConstructor_specialHarborBeatGenericHarbor_usesSpecialRate** ( :x: )
    - **State of the system**: Player has both a generic harbor and a special `BRICK` harbor, `giving = BRICK`, player has 2 `BRICK`, `amount = 2`, `receiving = LUMBER`
    - **Expected output**: Object created successfully (special harbor rate 2 used over generic rate 3)

---

### Method under test: `getPlayer()`

- **TC-16: getPlayer_validMaritimeTrade_returnsCorrectPlayer** ( :x: )
    - **State of the system**: Valid `MaritimeTrade` constructed
    - **Expected output**: Returns the `Player` passed to the constructor

---

### Method under test: `getGiving()`

- **TC-17: getGiving_validMaritimeTrade_returnsCorrectResource** ( :x: )
    - **State of the system**: Valid `MaritimeTrade` constructed
    - **Expected output**: Returns the `ResourceType` passed as `giving`

---

### Method under test: `getAmount()`

- **TC-18: getAmount_validMaritimeTrade_returnsCorrectAmount** ( :x: )
    - **State of the system**: Valid `MaritimeTrade` constructed
    - **Expected output**: Returns the `int` passed as `amount`

---

### Method under test: `getReceiving()`

- **TC-19: getReceiving_validMaritimeTrade_returnsCorrectResource** ( :x: )
    - **State of the system**: Valid `MaritimeTrade` constructed
    - **Expected output**: Returns the `ResourceType` passed as `receiving`

---

### Method under test: `getRate()`

- **TC-20: getRate_noHarbor_returnsFour** ( :x: )
    - **State of the system**: Player has no harbor; valid `MaritimeTrade` constructed with `amount = 4`
    - **Expected output**: `4`

- **TC-21: getRate_genericHarbor_returnsThree** ( :x: )
    - **State of the system**: Player has a generic harbor; valid `MaritimeTrade` constructed with `amount = 3`
    - **Expected output**: `3`

- **TC-22: getRate_specialHarbor_returnsTwo** ( :x: )
    - **State of the system**: Player has a matching special harbor; valid `MaritimeTrade` constructed with `amount = 2`
    - **Expected output**: `2`