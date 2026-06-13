# Multi-Turn Game Loop — Class & Method Design

## Overview

This document covers the classes and methods needed to sequence multiple turns across all players for a full game of Catan. It picks up where the First Turn Phase doc leaves off: `SetupPhase` is complete, the first `Turn` has been executed, and the game now needs an orchestrator to advance play from player to player until a win condition is detected.

Following the pattern established by `SetupPhase`, this phase introduces a single new orchestrator class: `TurnManager`. It also introduces a `DevelopmentCard` stub class and a `DevelopmentDeck` to support the one-card-per-turn dev card rule tracked by `Turn`.

The classes introduced here are `DevelopmentCard`, `DevelopmentDeck`, and `TurnManager`. All depend on domain objects established in prior phases.

---

## Dependency Graph

```
DevelopmentCard ───────────► (no dependencies; stub)
DevelopmentDeck ───────────► DevelopmentCard
TurnManager ───────────────► Game, Bank, Dice, Turn, DevelopmentDeck
```

---

## Classes

---

### `DevelopmentCard`

A stub representing a single development card. Tracks only what is needed for the multi-turn rule: whether the card has been played, and what turn it was purchased on (to enforce the "cannot play the turn it was bought" rule). Full card-type effects are deferred to a later phase.

| Field            | Type      | Description                                                           |
|------------------|-----------|-----------------------------------------------------------------------|
| `turnPurchased`  | `int`     | The turn number on which this card was drawn; used to block same-turn play |
| `played`         | `boolean` | True if this card has already been played                             |

| Method                          | Return Type | Description                                                                                                  |
|---------------------------------|-------------|----------------------------------------------------------------------------------------------------------------------|
| `DevelopmentCard(int turnPurchased)` | —      | Constructor; throws `IllegalArgumentException` if `turnPurchased` < 1                                        |
| `getTurnPurchased()`            | `int`       | Returns the turn on which this card was purchased                                                            |
| `isPlayed()`                    | `boolean`   | Returns true if this card has been played                                                                    |
| `markPlayed()`                  | `void`      | Marks the card as played; throws `IllegalStateException` if already played                                   |
| `isPlayableOnTurn(int currentTurn)` | `boolean` | Returns true if `currentTurn` > `turnPurchased` and the card has not been played                          |

**Invariants:**
- A card may only be played on a turn strictly after the one it was purchased on
- Once `markPlayed()` is called, `isPlayed()` is permanently true

---

### `DevelopmentDeck`

Represents the face-down stack of 25 development cards. Tracks the draw order and remaining count. Card type resolution is stubbed — each drawn card is a plain `DevelopmentCard` for now.

| Field    | Type                    | Description                             |
|----------|-------------------------|-----------------------------------------|
| `cards`  | `List<DevelopmentCard>` | The remaining cards in draw order       |

| Method                              | Return Type       | Description                                                                                                      |
|--------------------------------------|-------------------|------------------------------------------------------------------------------------------------------------------|
| `DevelopmentDeck()`                 | —                 | Constructor; initializes all 25 cards shuffled; `turnPurchased` is set to 0 as a placeholder until drawn        |
| `draw(int currentTurn)`             | `DevelopmentCard` | Removes and returns the top card, setting its `turnPurchased` to `currentTurn`; throws `IllegalStateException` if the deck is empty |
| `isEmpty()`                         | `boolean`         | Returns true if no cards remain                                                                                  |
| `getRemainingCount()`               | `int`             | Returns the number of cards left in the deck                                                                     |

**Invariants:**
- Total cards across all players' hands plus deck always equals 25
- `draw()` on an empty deck always throws

---

### `TurnManager`

Orchestrates the full turn sequence across all players for the duration of the game. Mirrors the structure of `SetupPhase`: it owns the current-player cursor, creates and completes `Turn` objects, and exposes clean state-query methods. It does not make game decisions — that responsibility belongs to callers.

`TurnManager` checks for a winner at the end of every turn by delegating to `VictoryPointCalculator` (defined in the Win Conditions doc). If a winner is found, `TurnManager` stops advancing and exposes the winner.

| Field                | Type               | Description                                                                              |
|----------------------|--------------------|------------------------------------------------------------------------------------------|
| `game`               | `Game`             | The current game state                                                                   |
| `bank`               | `Bank`             | The shared resource bank, passed through to each `Turn`                                  |
| `dice`               | `Dice`             | The shared dice instance, passed through to each `Turn`                                  |
| `devDeck`            | `DevelopmentDeck`  | The shared development card deck                                                         |
| `currentTurnNumber`  | `int`              | How many turns have been completed; starts at 0, increments after each `endCurrentTurn()` |
| `currentPlayerIndex` | `int`              | Index into `game.getPlayers()` for the active player; advances modulo player count       |
| `currentTurn`        | `Turn`             | The `Turn` object for the player currently acting; null between turns                    |
| `winner`             | `Player`           | The winning player once 10 VP are reached; null while game is ongoing                   |

| Method                                                                    | Return Type      | Description                                                                                                                                                       |
|-----------------------------------------------------------------------------|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `TurnManager(Game game, Bank bank, Dice dice, DevelopmentDeck devDeck)`   | —                | Constructor; validates no argument is null; sets `currentPlayerIndex` to 0, `currentTurnNumber` to 0; does not start the first turn automatically                 |
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
|------------------------------------------------|-------------------------------------------------------------|---------------------------------------------------------|
| `startNextTurn()` before previous turn ends    | Called while `currentTurn.getPhase()` is not `DONE`       | Called after `endCurrentTurn()` completes             |
| `endCurrentTurn()` too early                   | Called while phase is `PRODUCTION` or `TRADE`             | Called while phase is `BUILD`                         |
| `startNextTurn()` after game over              | Called when `winner` is set                               | Called when `winner` is null                          |
| Dev card purchase — deck empty                 | `devDeck.isEmpty()` is true                               | At least 1 card remains                               |
| Dev card play — same-turn purchase             | `isPlayableOnTurn(currentTurn)` is false                  | Card purchased on any earlier turn                    |
| Dev card play — already played                 | `isPlayed()` is true                                      | `isPlayed()` is false                                 |
| Dev card play — more than one per turn         | Second `markPlayed()` call in the same turn               | First and only `markPlayed()` call in a turn          |
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
├── Dice.java                  (existing)
├── Bank.java                  (existing)
├── TradeOffer.java            (existing)
├── Turn.java                  (existing)
├── DevelopmentCard.java       (NEW)
├── DevelopmentDeck.java       (NEW)
└── TurnManager.java            (NEW)

src/test/java/domain/
├── HexTest.java               (existing)
├── BoardTest.java             (existing)
├── PlayerTest.java            (existing)
├── GameTest.java              (existing)
├── SetupPhaseTest.java        (existing)
├── DiceTest.java              (existing)
├── BankTest.java              (existing)
├── TradeOfferTest.java        (existing)
├── TurnTest.java              (existing)
├── DevelopmentCardTest.java   (NEW)
├── DevelopmentDeckTest.java   (NEW)
└── TurnManagerTest.java       (NEW)
```

---

## Implementation Notes

- `TurnManager` is the direct successor to `SetupPhase` in the game lifecycle. Once `SetupPhase.isComplete()` returns true, the caller constructs a `TurnManager` with the same `Game`, `Bank`, and `Dice` instances.
- `TurnManager` does not own win-condition logic — it only calls `VictoryPointCalculator.getWinner(game)` (defined in the Win Conditions doc) after each turn ends. This keeps the two concerns separately testable.
- `DevelopmentCard` is intentionally minimal. The `turnPurchased` field and `isPlayableOnTurn()` are the only multi-turn-relevant behaviors; all card-effect logic is deferred. When card effects are eventually implemented, `DevelopmentCard` can be subclassed or given a type field without changing `TurnManager`.
- `DevelopmentDeck` sets `turnPurchased` at draw time (not at construction), which means all 25 cards in the deck start with a placeholder value of 0. This avoids a chicken-and-egg problem where the deck doesn't yet know what turn it will be drawn on.
- `TurnManager` passes the same `Dice` instance to every `Turn`, preserving roll history if needed for debugging or replay. If your team prefers fresh dice each turn, construct a new `Dice()` inside `startNextTurn()` instead.
- `getPlayerTurnCount()` can be derived from `currentTurnNumber` and `currentPlayerIndex` rather than maintained in a separate map, but either approach is valid. The simpler formula is: `completedTurns / playerCount` for players who have gone first, adjusted by index for others.
