# BVA for `TradeOffer`

### Method under test: `TradeOffer(Player, Player, Map<ResourceType, Integer>, Map<ResourceType, Integer>)`

- **TO-01: tradeOfferConstructor_offererEqualsRecipient_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer == recipient`
    - **Expected output**: `IllegalArgumentException`

- **TO-02: tradeOfferConstructor_offeringNull_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `offering` is `null`
    - **Expected output**: `IllegalArgumentException`

- **TO-03: tradeOfferConstructor_requestingNull_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `requesting` is `null`
    - **Expected output**: `IllegalArgumentException`

- **TO-04: tradeOfferConstructor_offeringEmpty_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `offering` is an empty map
    - **Expected output**: `IllegalArgumentException`

- **TO-05: tradeOfferConstructor_requestingEmpty_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `requesting` is an empty map
    - **Expected output**: `IllegalArgumentException`

- **TO-06: tradeOfferConstructor_offeringValueZero_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `offering` contains a value of `0`
    - **Expected output**: `IllegalArgumentException`

- **TO-07: tradeOfferConstructor_offeringValueNegative_throwsIllegalArgument** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, `offering` contains a value of `-1`
    - **Expected output**: `IllegalArgumentException`

- **TO-08: tradeOfferConstructor_requestingValueZero_throwsIllegalArgument** ( :white_check_mark )
    - **State of the system**: `offerer != recipient`, `requesting` contains a value of `0`
    - **Expected output**: `IllegalArgumentException`

- **TO-09: tradeOfferConstructor_requestingValueNegative_throwsIllegalArgument** ( :white_check_mark )
    - **State of the system**: `offerer != recipient`, `requesting` contains a value of `-1`
    - **Expected output**: `IllegalArgumentException`

- **TO-10: tradeOfferConstructor_allValuesMinimumValid_objectCreatedWithPendingStatus** ( :white_check_mark: )
    - **State of the system**: `offerer != recipient`, all values in both maps equal `1`
    - **Expected output**: Object created; `getStatus()` returns `PENDING`

### Method under test: `getOffering()`

- **TO-11: getOffering_validTradeOffer_returnsMatchingOfferingMap** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: Returns map equal to `offering` passed to constructor

- **TO-12: getOffering_validTradeOffer_mutationAttemptThrowsUnsupportedOperation** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: `UnsupportedOperationException` on `put()` attempt

### Method under test: `getRequesting()`

- **TO-13: getRequesting_validTradeOffer_returnsMatchingRequestingMap** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: Returns map equal to `requesting` passed to constructor

- **TO-14: getRequesting_validTradeOffer_mutationAttemptThrowsUnsupportedOperation** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: `UnsupportedOperationException` on `put()` attempt

### Method under test: `getOfferer()`

- **TO-15: getOfferer_validTradeOffer_returnsCorrectOfferer** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: Returns the `Player` passed as `offerer`


### Method under test: `getRecipient()`

- **TO-16: getRecipient_validTradeOffer_returnsCorrectRecipient** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed
    - **Expected output**: Returns the `Player` passed as `recipient`

### Method under test: `getStatus()`

- **TO-17: getStatus_freshlyConstructed_returnsPending** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed, no calls to `accept()` or `reject()`
    - **Expected output**: `TradeStatus.PENDING`

### Method under test: `isPending()`

- **TO-18: isPending_freshlyConstructed_returnsTrue** ( :x: )
    - **State of the system**: Valid `TradeOffer` constructed, no calls to `accept()` or `reject()`
    - **Expected output**: `true`

- **TO-19: isPending_statusAccepted_returnsFalse** ( :x: )
    - **State of the system**: `accept()` previously called
    - **Expected output**: `false`

- **TO-20: isPending_statusRejected_returnsFalse** ( :x: )
    - **State of the system**: `reject()` previously called
    - **Expected output**: `false`

### Method under test: `accept()`

- **TO-21: accept_statusPending_statusBecomesAccepted** ( :x: )
    - **State of the system**: Status is `PENDING`
    - **Expected output**: `getStatus()` returns `ACCEPTED`

- **TO-22: accept_statusAlreadyAccepted_throwsIllegalState** ( :x: )
    - **State of the system**: `accept()` previously called; status is `ACCEPTED`
    - **Expected output**: `IllegalStateException`

- **TO-23: accept_statusRejected_throwsIllegalState** ( :x: )
    - **State of the system**: `reject()` previously called; status is `REJECTED`
    - **Expected output**: `IllegalStateException`

### Method under test: `reject()`

- **TO-24: reject_statusPending_statusBecomesRejected** ( :x: )
    - **State of the system**: Status is `PENDING`
    - **Expected output**: `getStatus()` returns `REJECTED`

- **TO-25: reject_statusAlreadyRejected_throwsIllegalState** ( :x: )
    - **State of the system**: `reject()` previously called; status is `REJECTED`
    - **Expected output**: `IllegalStateException`

- **TO-26: reject_statusAccepted_throwsIllegalState** ( :x: )
    - **State of the system**: `accept()` previously called; status is `ACCEPTED`
    - **Expected output**: `IllegalStateException`