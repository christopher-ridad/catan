# First Turn Phase — Class & Method Design

## Overview

This document covers the classes and public methods needed to execute a single player's turn in Catan. A turn has three mandatory phases in order: **Resource Production** (dice roll), **Trade** (optional), and **Build** (optional). This design doc covers the full first-turn implementation, beginning from the moment `SetupPhase` is complete and the first player rolls.

The classes introduced here are `DiceRoll`, `Bank`, `TradeOffer`, `TurnPhase` (enum), and `Turn`. They depend on the domain objects already implemented in the Setup Phase (`Game`, `Board`, `Hex`, `Vertex`, `Edge`, `Player`, `ResourceType`).

---

## Dependency Graph

```
TurnPhase (enum)
DiceRoll ──────────────────► (no domain dependencies; pure randomness)
Bank ──────────────────────► ResourceType
TradeOffer ────────────────► Player, ResourceType
Turn ──────────────────────► Game, DiceRoll, Bank, TradeOffer, Vertex, Edge, TurnPhase
```

---

## Enums

### `TurnPhase`
```
PRODUCTION, TRADE, BUILD, DONE
```
Represents the current phase within an active turn. Phases advance in order and may not be skipped or reversed.

---

## Classes

---

### `DiceRoll`

Simulates rolling two dice and returns their sum.

| Method                  | Return Type | Description                                          |
|-------------------------|-------------|------------------------------------------------------|
| `DiceRoll(Random)`      | —           | Constructor; throws `NullPointerException` if random is null |
| `roll()`                | `int`       | Rolls both dice and returns their sum (2–12)         |

**Invariants:**
- Every value returned by `roll()` is in [2, 12]

---

### `Bank`

Represents the shared resource supply. Enforces supply limits and handles card distribution and collection.

| Field       | Type                         | Description                                                        |
|-------------|------------------------------|--------------------------------------------------------------------|
| `supply`    | `Map<ResourceType, Integer>` | Current card count per resource type; initialized to 19 each      |

| Method                                              | Return Type | Description                                                                                                                             |
|-----------------------------------------------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `Bank()`                                            | —           | Constructor; initializes each of the 5 resource types to 19 cards                                                                      |
| `getSupply(ResourceType)`                           | `int`       | Returns how many cards of the given type remain in the bank                                                                             |
| `canDistribute(ResourceType, int amount)`           | `boolean`   | Returns true if the bank has at least `amount` cards of the given type                                                                  |
| `distribute(ResourceType, int amount)`              | `void`      | Removes `amount` cards from supply; throws `IllegalStateException` if supply is insufficient, `IllegalArgumentException` if amount <= 0 |
| `collect(ResourceType, int amount)`                 | `void`      | Returns `amount` cards to supply; throws `IllegalArgumentException` if amount <= 0 or if collection would exceed 19                     |
| `hasEnoughForProduction(Map<ResourceType, Integer>)` | `boolean`  | Returns true if the bank can fulfill every entry in the given production map (used by `Turn` to determine partial-distribution edge case) |

**Invariants:**
- Each resource type is always in the range [0, 19]
- Total cards across all types never exceed 95 (5 × 19)

---

### `TradeOffer`

Represents a proposal from the active player to one other player during the `TRADE` phase. Holds what is being offered and what is being requested, and tracks whether the offer has been responded to.

| Field          | Type                          | Description                                              |
|----------------|-------------------------------|----------------------------------------------------------|
| `offerer`      | `Player`                      | The player making the offer (always the active player)   |
| `recipient`    | `Player`                      | The player being asked to trade                          |
| `offering`     | `Map<ResourceType, Integer>`  | Resources the offerer will give                          |
| `requesting`   | `Map<ResourceType, Integer>`  | Resources the offerer wants in return                    |
| `status`       | `TradeStatus`                 | Current state of the offer: `PENDING`, `ACCEPTED`, `REJECTED` |

#### `TradeStatus` (nested enum)
```
PENDING, ACCEPTED, REJECTED
```

| Method                                                                              | Return Type   | Description                                                                                                                                                    |
|-------------------------------------------------------------------------------------|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `TradeOffer(Player offerer, Player recipient, Map<ResourceType, Integer> offering, Map<ResourceType, Integer> requesting)` | — | Constructor; throws `IllegalArgumentException` if offerer equals recipient, if either map is null or empty, or if any value in either map is <= 0 |
| `getOfferer()`                                                                      | `Player`      | Returns the player who made the offer                                                                                                                          |
| `getRecipient()`                                                                    | `Player`      | Returns the player who received the offer                                                                                                                      |
| `getOffering()`                                                                     | `Map<ResourceType, Integer>` | Returns an unmodifiable view of what the offerer is giving                                                                              |
| `getRequesting()`                                                                   | `Map<ResourceType, Integer>` | Returns an unmodifiable view of what the offerer wants                                                                                  |
| `getStatus()`                                                                       | `TradeStatus` | Returns the current status of the offer                                                                                                                        |
| `accept()`                                                                          | `void`        | Sets status to `ACCEPTED`; throws `IllegalStateException` if status is not `PENDING`                                                                           |
| `reject()`                                                                          | `void`        | Sets status to `REJECTED`; throws `IllegalStateException` if status is not `PENDING`                                                                           |
| `isPending()`                                                                       | `boolean`     | Returns true if status is `PENDING`                                                                                                                            |

**Invariants:**
- `offerer` and `recipient` are never the same player
- All values in `offering` and `requesting` are > 0
- Once `accept()` or `reject()` is called, the status is final

---



Orchestrates a single player's turn through its three phases. Enforces phase order, handles the robber on a 7, and delegates building cost validation to the existing domain objects.

| Field                  | Type          | Description                                                                    |
|------------------------|---------------|--------------------------------------------------------------------------------|
| `game`                 | `Game`        | The current game state                                                         |
| `activePlayer`         | `Player`      | The player taking this turn                                                    |
| `dice`                 | `DiceRoll`    | The dice used for resource production                                          |
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

| Method                              | Return Type    | Description                                                                                                                       |
|-------------------------------------|----------------|-----------------------------------------------------------------------------------------------------------------------------------|
| `enforceDiscard()`                  | `void`         | For each player holding > 7 cards, removes `floor(count / 2)` cards at random and returns them to the bank; called internally    |
| `moveRobber(int hexId)`             | `void`         | Moves the robber to the given hex; throws `IllegalArgumentException` if hex is current robber location or id is invalid           |
| `steal(Player target)`              | `void`         | Steals 1 random resource from `target`; throws if target has no cards or no settlement adjacent to the robber's new hex           |
| `getRobbingCandidates()`            | `List<Player>` | Returns all players (excluding active player) with a settlement or city adjacent to the hex the robber was just moved to          |

**Phase transition rules:**

```
PRODUCTION ──[rollDice()]──► TRADE ──[advanceToBuild()]──► BUILD ──[endTurn()]──► DONE
```

- Build actions (`buildRoad`, `buildSettlement`, `buildCity`, `buyDevelopmentCard`) are only valid in `BUILD` phase.
- When a 7 is rolled, `moveRobber()` and `steal()` must be called before `advanceToBuild()` is permitted.

---

## Key Validation Rules (BVA Targets)

| Rule                                        | Invalid (boundary)                                   | Valid (boundary)                                 |
|---------------------------------------------|------------------------------------------------------|--------------------------------------------------|
| `rollDice()` call order                     | Called twice in one turn; called outside `PRODUCTION`| Called exactly once at start of turn             |
| Bank supply during production               | Requested amount > remaining supply                  | Requested amount == remaining supply             |
| Discard threshold                           | 7 cards (no discard), 9 cards (discard 4)            | 8 cards (discard 4)                              |
| Road placement                              | Edge already has a road; edge not connected to player's network | First valid connected, empty edge      |
| Settlement placement (Distance Rule)        | Vertex with any occupied neighbor                    | Vertex with all neighbors empty                  |
| Settlement placement (road connection)      | Vertex not connected to any player road              | Vertex at end of at least one player road        |
| City upgrade                                | Vertex not owned by player; vertex has a city already| Vertex with player's existing settlement         |
| `moveRobber()` target                       | Current robber hex id                                | Any other valid hex id                           |
| `steal()` target                            | Player with 0 resource cards                         | Player with 1 resource card                      |
| Build action outside `BUILD` phase          | Any build call during `PRODUCTION` or `TRADE`        | Any build call during `BUILD`                    |
| Domestic trade — offerer affords offering   | Offerer has 0 of a resource they're offering         | Offerer has exactly the amount they're offering  |
| Domestic trade — recipient affords request  | Recipient has 0 of a resource being requested        | Recipient has exactly the amount being requested |
| Domestic trade — self-trade                 | `offerer` == `recipient`                             | Any two distinct players                         |
| Domestic trade — offer map values           | Any value <= 0 in offering or requesting             | All values >= 1                                  |
| Trade proposed when one already pending     | Second `proposeTrade()` before prior offer resolves  | `proposeTrade()` after prior offer accepted/rejected |
| Maritime trade — default rate               | Fewer than 4 identical cards (no harbor)             | Exactly 4 identical cards                        |
| Maritime trade — generic harbor rate        | Fewer than 3 identical cards                         | Exactly 3 identical cards                        |
| Maritime trade — special harbor rate        | Fewer than 2 of the matching resource                | Exactly 2 of the matching resource               |
| Maritime trade action outside `TRADE` phase | `executeMaritimeTrade()` called during `BUILD`        | `executeMaritimeTrade()` called during `TRADE`   |

---

## Package Structure

```
src/main/java/domain/
├── ResourceType.java          (enum — existing)
├── TerrainType.java           (enum — existing)
├── PlayerColor.java           (enum — existing)
├── TurnPhase.java             (enum — NEW)
├── Hex.java                   (existing)
├── Vertex.java                (existing)
├── Edge.java                  (existing)
├── Board.java                 (existing)
├── Player.java                (existing)
├── Game.java                  (existing)
├── SetupPhase.java            (existing)
├── DiceRoll.java              (NEW)
├── Bank.java                  (NEW)
├── TradeOffer.java            (NEW)
└── Turn.java                  (NEW)

src/test/java/domain/
├── HexTest.java               (existing)
├── BoardTest.java             (existing)
├── PlayerTest.java            (existing)
├── GameTest.java              (existing)
├── SetupPhaseTest.java        (existing)
├── DiceRollTest.java          (NEW)
├── BankTest.java              (NEW)
├── TradeOfferTest.java        (NEW)
└── TurnTest.java              (NEW)
```

---

## Implementation Notes

- `Turn` depends on `Game` for board topology and player list, but should not modify `Game` state directly. All state mutations (resource counts, vertex/edge ownership) go through `Player`, `Vertex`, and `Edge` methods already defined in the Setup Phase design.
- `Bank` is a standalone class, not embedded in `Game`, so it can be mocked independently in `TurnTest`.
- `DiceRoll` has no domain dependencies and should be straightforward to test by injecting a seeded or mocked `Random` for deterministic rolls.
- `TradeOffer` is a pure data/state class with no dependency on `Turn` — test it in isolation in `TradeOfferTest` before wiring it into `TurnTest`.
- Only one domestic trade offer may be pending at a time. `Turn` must clear `pendingTrade` (set to null) as soon as an offer is accepted or rejected, before another `proposeTrade()` call is permitted.
- Maritime trade rate lookup in `getMaritimeRate()` requires checking which vertices the active player has settlements or cities on, then inspecting the harbor type at each. This will likely require a `getHarborAt(Vertex)` or similar query on `Board` — that may be a small extension needed there.
- Other players cannot trade among themselves during another player's turn; `proposeTrade()` enforces this by requiring `offerer == activePlayer`.
- The robber's current hex should be tracked on `Board` (a `getRobberHex()` / `setRobberHex(Hex)` pair may need to be added to `Board` as a small extension of the existing class).