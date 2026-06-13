# Multi-Turn Game Loop — Class & Method Design

## Overview

This document covers the classes and methods needed to sequence multiple turns across all players for a full game of Catan. It picks up where the First Turn Phase doc leaves off: `SetupPhase` is complete, the first `Turn` has been executed, and the game now needs an orchestrator to advance play from player to player until a win condition is detected.

The classes introduced here are `DiceRoll`, `Bank`, `TradeOffer`, `TurnPhase` (enum), and `Turn`. They depend on the domain objects already implemented in the Setup Phase (`Game`, `Board`, `Hex`, `Vertex`, `Edge`, `Player`, `ResourceType`).

The classes introduced here are `DevelopmentDeck` and `TurnManager`, plus a revision to the existing `DevelopmentCard` class. All depend on domain objects established in prior phases.

```
TurnPhase (enum)
DiceRoll ──────────────────────► (no domain dependencies; pure randomness)
Bank ──────────────────────► ResourceType
TradeOffer ────────────────► Player, ResourceType
Turn ──────────────────────► Game, DiceRoll, Bank, TradeOffer, Vertex, Edge, TurnPhase
```

---

## Dependency Graph

```
DevelopmentCard (existing, MODIFIED) ─► DevelopmentCardType
DevelopmentDeck ───────────► DevelopmentCard
TurnManager ───────────────► Game, Bank, DiceRoll, Turn, DevelopmentDeck
```

---

### `DevelopmentCardType`
```
KNIGHT, ROAD_BUILDING, YEAR_OF_PLENTY, MONOPOLY, VICTORY_POINT
```
Represents the type of a development card. There are 25 total: 14 Knight, 6 Progress (Road Building, Year of Plenty, Monopoly), and 5 Victory Point.


## Classes

---

### `DiceRoll`

Simulates rolling two dice. Keeps the most recent individual die values so callers can detect a 7 and inspect each die separately.

Simulates rolling two dice and returns their sum.

| Method         | Return Type | Description                                                                                 |
|----------------|-------------|---------------------------------------------------------------------------------------------|
| `DiceRoll()`       | —           | Default constructor; initializes both dice to 0 (no roll yet)                               |
| `roll()`       | `int`       | Rolls both dice, stores results in `die1` and `die2`, returns their sum (2–12)              |
| `getTotal()`   | `int`       | Returns the sum of the last roll; throws `IllegalStateException` if `roll()` has not been called |
| `getDie1()`    | `int`       | Returns the value of the first die from the last roll                                       |
| `getDie2()`    | `int`       | Returns the value of the second die from the last roll                                      |
| `isSevenRolled()` | `boolean` | Returns true if the last roll summed to 7                                                  |

**Invariants:**
- Every value returned by `roll()` is in [2, 12]

---

### `Bank`

This class already exists from the First Turn Phase implementation, with `DevelopmentCardType`, `markAsPlayed()`, and `isPlayed()`. The multi-turn rule ("a development card cannot be played the same turn it was purchased") requires one additional field — `turnPurchased` — and one additional method — `isPlayableOnTurn()`. The merged definition below is the **single source of truth** going forward; it combines both sets of fields and standardizes on the `markAsPlayed()` naming already in use.

| Field            | Type                  | Status   | Description                                                           |
|------------------|------------------------|----------|-------------------------------------------------------------------------|
| `type`           | `DevelopmentCardType`  | existing | The kind of card (Knight, Progress variant, or Victory Point)         |
| `played`         | `boolean`              | existing | True if this card has already been played                             |
| `turnPurchased`  | `int`                  | **NEW**  | The turn number on which this card was drawn; used to block same-turn play |

| Method                                          | Return Type           | Status   | Description                                                                                                  |
|--------------------------------------------------|------------------------|----------|------------------------------------------------------------------------------------------------------------|
| `DevelopmentCard(DevelopmentCardType type, int turnPurchased)` | —      | **MODIFIED** | Constructor now takes `turnPurchased` in addition to `type`; `turnPurchased` must be `-1` (undrawn placeholder) or `>= 1`; throws `IllegalArgumentException` for any other value (e.g. 0) |
| `getType()`                                      | `DevelopmentCardType`  | existing | Returns the card's type                                                                                       |
| `getTurnPurchased()`                             | `int`                  | **NEW**  | Returns the turn on which this card was purchased, or `-1` if not yet drawn                                  |
| `isPlayed()`                                     | `boolean`              | existing | Returns true if this card has been played                                                                    |
| `markAsPlayed()`                                 | `void`                 | existing | Marks the card as played; throws `IllegalStateException` if already played                                  |
| `isPlayableOnTurn(int currentTurn)`              | `boolean`              | **NEW**  | Returns false if `turnPurchased == -1` (not yet drawn); otherwise true if `currentTurn > turnPurchased` and `isPlayed()` is false |

**Invariants:**
- A card may only be played on a turn strictly after the one it was purchased on
- A card with `turnPurchased == -1` is never playable
- Once `markAsPlayed()` is called, `isPlayed()` is permanently true

**Migration note:** existing call sites constructing `new DevelopmentCard(type)` need to be updated to pass `turnPurchased` as well. Since cards are only ever constructed via `DevelopmentDeck.draw(currentTurn)` (below), this should be a small, contained change.

---

### `DevelopmentDeck`

Represents the face-down stack of 25 development cards (14 Knight, 6 Progress, 5 Victory Point per the ruleset breakdown). Tracks the draw order and remaining count, and assigns each card's `turnPurchased` at draw time.

| Field    | Type                    | Description                             |
|----------|-------------------------|-------------------------------------------|
| `cards`  | `List<DevelopmentCard>` | The remaining cards in draw order, with `type` assigned and `turnPurchased` unset (placeholder) |

| Method                              | Return Type       | Description                                                                                                      |
|-------------------------------------|-------------------|------------------------------------------------------------------------------------------------------------------|
| `DevelopmentDeck()`                 | —                 | Constructor; initializes all 25 cards with their `type` values, shuffled; `turnPurchased` is set to a placeholder of `-1` until drawn |
| `draw(int currentTurn)`             | `DevelopmentCard` | Removes and returns the top card, setting its `turnPurchased` to `currentTurn`; throws `IllegalStateException` if the deck is empty |
| `isEmpty()`                         | `boolean`         | Returns true if no cards remain                                                                                  |
| `getRemainingCount()`               | `int`             | Returns the number of cards left in the deck                                                                     |
| `getRemainingCount(DevelopmentCardType)` | `int`        | Returns the number of cards of the given type left in the deck                                                  |

**Invariants:**
- Total cards across all players' hands plus deck always equals 25
- `draw()` on an empty deck always throws
- A card's `turnPurchased` is `-1` (placeholder, not yet drawn) until `draw()` is called on it

---

### `Turn`
### `DevelopmentCard`
Represents a single development card. Tracks its type and whether it has been played.

| Field        | Type                  | Description                                      |
|--------------|-----------------------|--------------------------------------------------|
| `type`       | `DevelopmentCardType` | The type of this card                            |
| `played`     | `boolean`             | True if this card has already been played        |

| Method                          | Return Type           | Description                                                                                       |
|---------------------------------|-----------------------|---------------------------------------------------------------------------------------------------|
| `DevelopmentCard(DevelopmentCardType type)` | —       | Constructor; throws `IllegalArgumentException` if type is null                                    |
| `getType()`                     | `DevelopmentCardType` | Returns the card type                                                                             |
| `isPlayed()`                    | `boolean`             | Returns true if the card has been played                                                          |
| `markAsPlayed()`                | `void`                | Marks the card as played; throws `IllegalStateException` if already played                        |

**Invariants:**
- Once `markAsPlayed()` is called, `isPlayed()` always returns true
- Victory Point cards are never marked as played until the player declares victory

---

### `Turn`
Orchestrates a single player's turn through its three phases. Enforces phase order, handles the robber on a 7, and delegates building cost validation to the existing domain objects.

| Field                  | Type          | Description                                                                    |
|------------------------|---------------|--------------------------------------------------------------------------------|
| `game`                 | `Game`        | The current game state                                                         |
| `activePlayer`         | `Player`      | The player taking this turn                                                    |
| `dice`                 | `DiceRoll`        | The dice used for resource production                                          |
| `bank`                 | `Bank`        | The shared resource bank                                                       |
| `phase`                | `TurnPhase`   | The current phase of this turn                                                 |
| `rolledThisTurn`       | `boolean`     | True once `rollDice()` has been called                                         |
| `playedDevCardThisTurn`| `boolean`     | True if a development card has already been played this turn                   |
| `pendingTrade`         | `TradeOffer`  | The active domestic trade offer, if any; null when no offer is pending         |

| Method                                                      | Return Type | Description                                                                                                                                                              |
|-------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Turn(Game game, Player activePlayer, DiceRoll dice, Bank bank)`| —           | Constructor; validates no argument is null, sets `phase` to `PRODUCTION`                                                                                                |
| `getActivePlayer()`                                         | `Player`    | Returns the player whose turn this is                                                                                                                                    |
| `getPhase()`                                               | `TurnPhase` | Returns the current phase                                                                                                                                                |
| `rollDice()`                                               | `int`       | Calls `dice.roll()`, advances to `TRADE` phase, triggers production or robber logic; throws `IllegalStateException` if called outside `PRODUCTION` phase or called twice |
| `produceResources(int roll)`                               | `void`      | Distributes resources to all players for the rolled number per settlement/city adjacency; respects bank supply limits and the robber's blocked hex; called internally by `rollDice()` |
| `advanceToTrade()`                                         | `void`      | Internal; sets `phase` to `TRADE` after production resolves                                                                                                             |
| `advanceToBuild()`                                         | `void`      | Called by the active player when done trading; sets `phase` to `BUILD`; throws `IllegalStateException` if not in `TRADE` phase                                          |
| `endTurn()`                                               | `void`      | Sets `phase` to `DONE`; throws `IllegalStateException` if not in `BUILD` phase                                                                                          |
| `buildRoad(int edgeId)`                                   | `void`      | Validates player can afford a road (1 Brick + 1 Lumber), that the edge is unoccupied and connected to their network, deducts cost; throws if invalid                   |
| `buildSettlement(int vertexId)`                           | `void`      | Validates player can afford a settlement (1 Brick + 1 Lumber + 1 Wool + 1 Grain), Distance Rule, and road connection; deducts cost; throws if invalid                  |
| `buildCity(int vertexId)`                                 | `void`      | Validates player can afford a city (3 Ore + 2 Grain) and that the vertex has an existing settlement owned by the player; replaces settlement; throws if invalid         |
| `buyDevelopmentCard()`                                    | `void`      | Validates player can afford (1 Ore + 1 Wool + 1 Grain) and that development cards remain; deducts cost; throws if invalid                                               |
| `isSevenRolled()`                                         | `boolean`   | Returns true if the roll this turn was a 7                                                                                                                               |
| `proposeTrade(Player recipient, Map<ResourceType, Integer> offering, Map<ResourceType, Integer> requesting)` | `TradeOffer` | Creates and stores a `TradeOffer`; throws `IllegalStateException` if not in `TRADE` phase or if a trade is already pending; throws `IllegalArgumentException` if the active player cannot afford the offering |
| `acceptTrade(TradeOffer offer)`                           | `void`      | Called by the recipient to accept; validates the recipient can afford `requesting`, then executes the exchange between both players via their `addResources` / resource deduction; throws if offer is not pending or either player lacks cards |
| `rejectTrade(TradeOffer offer)`                           | `void`      | Called by the recipient to reject; marks the offer as `REJECTED` and clears `pendingTrade`; throws if offer is not pending                                              |
| `executeMaritimeTrade(ResourceType giving, int amount, ResourceType receiving)` | `void` | Performs a maritime trade at the active player's best available rate (4:1 default, 3:1 with a generic harbor, 2:1 with a matching special harbor); validates rate and bank supply; throws if not in `TRADE` phase or player cannot afford it |
| `getMaritimeRate(ResourceType)`                           | `int`       | Returns the best maritime trade rate available to the active player for the given resource (2, 3, or 4), based on adjacent harbor vertices                              |
| `getPendingTrade()`                                       | `Optional<TradeOffer>` | Returns the current pending trade offer, or `Optional.empty()` if none                                                                                    |

**Robber logic (triggered internally when `rollDice()` returns 7):**
1. No resources are produced.
2. Any player holding more than 7 resource cards must discard half (rounded down) — handled by `enforceDiscard()`.
3. The active player must then call `moveRobber(int hexId)` and `steal(Player target)` before `advanceToBuild()` is allowed.

| Method                  | Return Type    | Description                                                                                                                        |
|-------------------------|----------------|------------------------------------------------------------------------------------------------------------------------------------|
| `enforceDiscard()`      | `void`         | For each player holding > 7 cards, removes `floor(count / 2)` cards at random and returns them to the bank; called internally     |
| `moveRobber(int hexId)` | `void`         | Moves the robber to the given hex; throws `IllegalArgumentException` if hex is current robber location or id is invalid            |
| `steal(Player target)`  | `void`         | Steals 1 random resource from `target`; no-op if target holds no resource cards (so the turn cannot get stuck on an empty-handed candidate); throws if `target` has no settlement or city adjacent to the robber's new hex |
| `getRobbingCandidates()`| `List<Player>` | Returns all players (excluding active player) with a settlement or city adjacent to the hex the robber was just moved to           |

**Phase transition rules:**

```
[between turns] ──[startNextTurn()]──► PRODUCTION ──[rollDice()]──► TRADE
                                                                        │
                                                              [advanceToBuild()]
                                                                        │
                                                                      BUILD ──[endCurrentTurn()]──► [between turns / game over]
```

---

## Key Validation Rules (BVA Targets)

| Rule                                           | Invalid (boundary)                                        | Valid (boundary)                                      |
|------------------------------------------------|-----------------------------------------------------------|-------------------------------------------------------|
| `startNextTurn()` before previous turn ends    | Called while `currentTurn.getPhase()` is not `DONE`       | Called after `endCurrentTurn()` completes             |
| `endCurrentTurn()` too early                   | Called while phase is `PRODUCTION` or `TRADE`             | Called while phase is `BUILD`                         |
| `startNextTurn()` after game over              | Called when `winner` is set                               | Called when `winner` is null                          |
| Dev card purchase — deck empty                 | `devDeck.isEmpty()` is true                               | At least 1 card remains                               |
| Dev card play — same-turn purchase             | `isPlayableOnTurn(currentTurn)` is false                  | Card purchased on any earlier turn                    |
| Dev card play — already played                 | `isPlayed()` is true                                      | `isPlayed()` is false                                 |
| Dev card play — more than one per turn         | Second `markAsPlayed()` call in the same turn             | First and only `markAsPlayed()` call in a turn        |
| Dev card construction — placeholder vs invalid | `turnPurchased == 0`                                       | `turnPurchased == -1` (placeholder) or `>= 1`         |
| Player turn count                              | `getPlayerTurnCount(player)` for a player not in the game | Any valid player reference                            |

---

## Package Structure

```
src/main/java/domain/
├── ResourceType.java          (enum — existing)
├── TerrainType.java           (enum — existing)
├── PlayerColor.java           (enum — existing)
├── TurnPhase.java             (enum — existing)
├── Hex.java                   (existing)
├── Vertex.java                (existing)
├── Edge.java                  (existing)
├── Board.java                 (existing)
├── Player.java                (existing)
├── Game.java                  (existing)
├── SetupPhase.java            (existing)
├── DiceRoll.java                  (existing)
├── Bank.java                  (existing)
├── TradeOffer.java            (existing)
├── Turn.java                  (existing)
├── DevelopmentCard.java       (existing — MODIFIED)
├── DevelopmentDeck.java       (NEW)
└── TurnManager.java           (NEW)

src/test/java/domain/
├── HexTest.java               (existing)
├── BoardTest.java             (existing)
├── PlayerTest.java            (existing)
├── GameTest.java              (existing)
├── SetupPhaseTest.java        (existing)
├── DiceRollTest.java              (existing)
├── BankTest.java              (existing)
├── TradeOfferTest.java        (existing)
├── TurnTest.java              (existing)
├── DevelopmentCardTest.java   (existing — MODIFIED)
├── DevelopmentDeckTest.java   (NEW)
└── TurnManagerTest.java       (NEW)
```

---

## Implementation Notes

- `TurnManager` is the direct successor to `SetupPhase` in the game lifecycle. Once `SetupPhase.isComplete()` returns true, the caller constructs a `TurnManager` with the same `Game`, `Bank`, and `DiceRoll` instances.
- `TurnManager` does not own win-condition logic — it only calls `VictoryPointCalculator.getWinner(game)` (defined in the Win Conditions doc) after each turn ends. This keeps the two concerns separately testable.
- `DevelopmentCard` gains `turnPurchased` and `isPlayableOnTurn()` on top of its existing `type`/`markAsPlayed()`/`isPlayed()`. All card-effect logic (what Knight/Progress/VP cards actually *do*) remains deferred to a later phase — this revision only adds the multi-turn sequencing fields.
- `DevelopmentDeck` sets `turnPurchased` at draw time (not at construction), so all 25 cards start with the placeholder value `-1`. This avoids a chicken-and-egg problem where the deck doesn't yet know what turn it will be drawn on, while keeping `0` (and any other non-positive value) as a genuinely invalid input that the constructor rejects.
- `TurnManager` passes the same `DiceRoll` instance to every `Turn`, preserving roll history if needed for debugging or replay. If your team prefers fresh dice each turn, construct a new `DiceRoll()` inside `startNextTurn()` instead.
- `getPlayerTurnCount()` can be derived from `currentTurnNumber` and `currentPlayerIndex` rather than maintained in a separate map, but either approach is valid. The simpler formula is: `completedTurns / playerCount` for players who have gone first, adjusted by index for others.
