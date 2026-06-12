# Multi-Turn Game Loop — Class & Method Design

## Overview

This document covers the classes and methods needed to sequence multiple turns across all players for a full game of Catan. It picks up where the First Turn Phase doc leaves off: `SetupPhase` is complete, the first `Turn` has been executed, and the game now needs an orchestrator to advance play from player to player until a win condition is detected.

Following the pattern established by `SetupPhase`, this phase introduces a single new orchestrator class: `TurnManager`. It also extends the existing `DevelopmentCard` class (introduced in the First Turn Phase doc) with the fields needed for the one-card-per-turn rule, and introduces a new `DevelopmentDeck` to manage the draw pile.

The classes introduced here are `DevelopmentDeck` and `TurnManager`, plus a revision to the existing `DevelopmentCard` class. All depend on domain objects established in prior phases.

> **Reconciliation note:** An earlier draft of this doc independently re-specified `DevelopmentCard` with a different field set (`turnPurchased`, `markPlayed()`, `isPlayableOnTurn()`) than the version already implemented from the First Turn Phase doc (`DevelopmentCardType`, `markAsPlayed()`, `isPlayed()`). The section below merges both into a single definition. **The implemented class should be updated to match this merged definition before TurnManager work begins.**

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

### `DevelopmentCard` (existing class — REVISION)

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

### `TurnManager`

Orchestrates the full turn sequence across all players for the duration of the game. Mirrors the structure of `SetupPhase`: it owns the current-player cursor, creates and completes `Turn` objects, and exposes clean state-query methods. It does not make game decisions — that responsibility belongs to callers.

`TurnManager` checks for a winner at the end of every turn by delegating to `VictoryPointCalculator` (defined in the Win Conditions doc). If a winner is found, `TurnManager` stops advancing and exposes the winner.

| Field                | Type               | Description                                                                              |
|----------------------|--------------------|------------------------------------------------------------------------------------------|
| `game`               | `Game`             | The current game state                                                                   |
| `bank`               | `Bank`             | The shared resource bank, passed through to each `Turn`                                  |
| `dice`               | `DiceRoll`             | The shared dice instance, passed through to each `Turn`                                  |
| `devDeck`            | `DevelopmentDeck`  | The shared development card deck                                                         |
| `currentTurnNumber`  | `int`              | How many turns have been completed; starts at 0, increments after each `endCurrentTurn()` |
| `currentPlayerIndex` | `int`              | Index into `game.getPlayers()` for the active player; advances modulo player count       |
| `currentTurn`        | `Turn`             | The `Turn` object for the player currently acting; null between turns                    |
| `winner`             | `Player`           | The winning player once 10 VP are reached; null while game is ongoing                   |

| Method                                                                    | Return Type      | Description                                                                                                                                                       |
|---------------------------------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `TurnManager(Game game, Bank bank, DiceRoll dice, DevelopmentDeck devDeck)`   | —                | Constructor; validates no argument is null; sets `currentPlayerIndex` to 0, `currentTurnNumber` to 0; does not start the first turn automatically                 |
| `startNextTurn()`                                                         | `Turn`           | Creates a new `Turn` for the current player, stores it in `currentTurn`, returns it; throws `IllegalStateException` if the previous turn is not yet complete or if `winner` is set |
| `endCurrentTurn()`                                                        | `void`           | Calls `currentTurn.endTurn()`, increments `currentTurnNumber`, advances `currentPlayerIndex`, then checks for a winner via `VictoryPointCalculator`; throws `IllegalStateException` if `currentTurn` is not in `BUILD` or `DONE` phase |
| `getCurrentPlayer()`                                                      | `Player`         | Returns the player whose turn it currently is (or whose turn is next if between turns)                                                                            |
| `getCurrentTurnNumber()`                                                  | `int`            | Returns how many turns have been completed                                                                                                                        |
| `getCurrentTurn()`                                                        | `Optional<Turn>` | Returns the active `Turn`, or `Optional.empty()` if between turns                                                                                                 |
| `isGameOver()`                                                            | `boolean`        | Returns true if a winner has been found                                                                                                                           |
| `getWinner()`                                                             | `Optional<Player>` | Returns the winning player, or `Optional.empty()` if the game is still ongoing                                                                                  |
| `getPlayerTurnCount(Player)`                                              | `int`            | Returns how many turns the given player has completed; throws `IllegalArgumentException` if player is not in the game                                             |

**Turn sequencing rules:**
- Players take turns in the order returned by `game.getPlayers()`, cycling indefinitely until a winner is found.
- `currentPlayerIndex` advances as `(currentPlayerIndex + 1) % game.getPlayerCount()` after each completed turn.
- A turn is only completable if `currentTurn.getPhase()` is `BUILD` or `DONE`. Calling `endCurrentTurn()` while still in `PRODUCTION` or `TRADE` throws.
- Once `winner` is set, `startNextTurn()` throws, freezing the game state.

**Turn lifecycle (mirrors SetupPhase's placement lifecycle):**

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
