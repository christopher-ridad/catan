# Win Conditions — Class & Method Design

## Overview

This document covers the classes and methods needed to detect and declare a winner in Catan. A player wins by reaching **10 victory points (VP) on their own turn**. VP are accumulated from settlements (1 VP each), cities (2 VP each), the Longest Road special card (2 VP), the Largest Army special card (2 VP), and Victory Point development cards (1 VP each, counted only when the player declares victory or the game ends).

Following the project's established pattern, this phase introduces a single computation class, `VictoryPointCalculator`, which owns all VP logic including the graph traversal for Longest Road and the knight-count tracking for Largest Army. It is stateless — it reads from `Game`, `Player`, `Board`, and `DevelopmentCard` but mutates nothing.

The classes introduced here are `VictoryPointCalculator` and `SpecialCardTracker`. `TurnManager` calls into `VictoryPointCalculator` after each turn ends.

> **Prerequisite:** `getDevCardVP()` and `computeKnightCount()` both need to read a player's development card hand, but `Player` does not currently expose this. A small addition to `Player` is required before these two methods can be implemented — see the `Player` revision below.

---

## Dependency Graph

```
Player (existing, MODIFIED) ► DevelopmentCard
SpecialCardTracker ─────────► Player
VictoryPointCalculator ─────► Game, Board, Player, SpecialCardTracker, DevelopmentCard
```

---

## Classes

---

### `Player` (existing class — REVISION)

`Player` needs one additional method to expose the development cards in a player's hand, so `VictoryPointCalculator` can compute VP from Victory Point cards and count played Knights without reaching into private state.

| Method                            | Return Type             | Status   | Description                                                                 |
|------------------------------------|--------------------------|----------|-------------------------------------------------------------------------------|
| `getDevelopmentCards()`            | `List<DevelopmentCard>`  | **NEW**  | Returns an unmodifiable view of all development cards currently held by the player, including both played and unplayed cards |

**Invariants:**
- The returned list always reflects the player's current hand, including cards drawn via `DevelopmentDeck.draw()`; nothing is ever removed from this list — played cards remain with `isPlayed() == true`
- Order is not significant; `VictoryPointCalculator` filters by `getType()` and `isPlayed()`, not position

**Migration note:** if `Player` already stores development cards in a private list (e.g. as part of `buyDevelopmentCard()` from the First Turn Phase doc), this is a pure accessor addition with no change to existing behavior.

---

### `SpecialCardTracker`

Tracks the current holders of the two special cards — Longest Road and Largest Army — and the qualifying thresholds needed to claim or steal each. Both cards are worth 2 VP to whoever holds them. This class is the single source of truth for which player currently holds each card; `VictoryPointCalculator` reads from it to compute total VP.

| Field                    | Type     | Description                                                                              |
|--------------------------|----------|------------------------------------------------------------------------------------------|
| `longestRoadHolder`      | `Player` | The player currently holding the Longest Road card; null if unclaimed                   |
| `longestRoadLength`      | `int`    | The road length of the current holder; 0 if unclaimed                                   |
| `largestArmyHolder`      | `Player` | The player currently holding the Largest Army card; null if unclaimed                   |
| `largestArmySize`        | `int`    | The knight count of the current holder; 0 if unclaimed                                  |

| Method                                                   | Return Type      | Description                                                                                                                                                          |
|----------------------------------------------------------|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `SpecialCardTracker()`                                   | —                | Constructor; initializes all fields to null/0                                                                                                                        |
| `getLongestRoadHolder()`                                 | `Optional<Player>` | Returns the current Longest Road holder, or `Optional.empty()` if unclaimed                                                                                        |
| `getLongestRoadLength()`                                 | `int`            | Returns the current qualifying road length (0 if unclaimed)                                                                                                          |
| `getLargestArmyHolder()`                                 | `Optional<Player>` | Returns the current Largest Army holder, or `Optional.empty()` if unclaimed                                                                                        |
| `getLargestArmySize()`                                   | `int`            | Returns the current qualifying knight count (0 if unclaimed)                                                                                                         |
| `updateLongestRoad(Player candidate, int roadLength)`    | `void`           | Awards the Longest Road card to `candidate` if their `roadLength` meets the threshold (≥ 5 to claim from unclaimed; > current holder's length to steal); no-op otherwise; throws `IllegalArgumentException` if `roadLength` < 0 |
| `updateLargestArmy(Player candidate, int knightCount)`   | `void`           | Awards the Largest Army card to `candidate` if their `knightCount` meets the threshold (≥ 3 to claim from unclaimed; > current holder's count to steal); no-op otherwise; throws `IllegalArgumentException` if `knightCount` < 0 |
| `holdsLongestRoad(Player)`                               | `boolean`        | Returns true if the given player currently holds the Longest Road card                                                                                               |
| `holdsLargestArmy(Player)`                               | `boolean`        | Returns true if the given player currently holds the Largest Army card                                                                                               |

**Invariants:**
- `longestRoadLength` is always ≥ 5 when `longestRoadHolder` is non-null, and 0 when null
- `largestArmySize` is always ≥ 3 when `largestArmyHolder` is non-null, and 0 when null
- At most one player holds each special card at any time

---

### `VictoryPointCalculator`

A stateless computation class. Calculates total VP for any player, determines the current Longest Road for any player via graph traversal, counts played knights, and identifies a winner if one exists. Called by `TurnManager` after each turn ends.

All methods are intended to be called with current game state passed in — this class holds no mutable state of its own.

| Method                                                            | Return Type      | Description                                                                                                                                                                                    |
|-------------------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `VictoryPointCalculator()`                                        | —                | Default constructor; no fields to initialize                                                                                                                                                   |
| `getTotalVP(Player player, SpecialCardTracker tracker)`           | `int`            | Returns the player's total VP: settlements + cities + Longest Road card (if held) + Largest Army card (if held) + VP dev cards in hand; throws `IllegalArgumentException` if either argument is null |
| `getSettlementVP(Player player, Board board)`                     | `int`            | Returns 1 VP per vertex on `board` owned by `player` that holds a settlement (not a city)                                                                                                     |
| `getCityVP(Player player, Board board)`                           | `int`            | Returns 2 VP per vertex on `board` owned by `player` that holds a city                                                                                                                        |
| `getDevCardVP(Player player)`                                     | `int`            | Returns 1 VP per `DevelopmentCard` in `player.getDevelopmentCards()` with `getType() == VICTORY_POINT` (including unplayed ones — VP cards are always counted)                          |
| `computeLongestRoad(Player player, Board board)`                  | `int`            | Returns the length of the player's longest continuous road via depth-first graph traversal on edges owned by `player`; branches do not count — only the longest non-branching path            |
| `computeKnightCount(Player player)`                               | `int`            | Returns the count of `DevelopmentCard`s in `player.getDevelopmentCards()` with `getType() == KNIGHT` and `isPlayed() == true`                                                                 |
| `updateSpecialCards(Game game, Board board, SpecialCardTracker tracker)` | `void`   | Recomputes Longest Road and Largest Army for all players and calls `tracker.updateLongestRoad()` and `tracker.updateLargestArmy()` accordingly; intended to be called by `TurnManager` after every turn |
| `getWinner(Game game, Board board, SpecialCardTracker tracker)`   | `Optional<Player>` | Returns the player with ≥ 10 VP if one exists, or `Optional.empty()` otherwise; if multiple players somehow reach 10 VP simultaneously, returns the one whose turn it currently is (determined by turn order in `game.getPlayers()`) |
| `hasWinner(Game game, Board board, SpecialCardTracker tracker)`   | `boolean`        | Returns true if any player has ≥ 10 VP; convenience wrapper around `getWinner()`                                                                                                              |

**Longest Road computation rules (per ruleset):**
- Only continuous road segments count — a road is continuous if each segment connects end-to-end without lifting.
- Branching paths do not extend the count; at a fork, only one branch is followed per traversal path.
- A road is broken if an opponent's settlement or city is placed at an intermediate vertex; segments on either side of the break are counted separately.
- The algorithm is a depth-first search over edges owned by `player`, tracking visited edges (not vertices) to handle loops correctly.
- The minimum qualifying length to claim the Longest Road card is 5.

**VP source summary (used in `getTotalVP`):**

| Source                        | VP per unit | Notes                                      |
|-------------------------------|-------------|---------------------------------------------|
| Settlement on board           | 1           | Read from `board.getVertices()`             |
| City on board                 | 2           | Read from `board.getVertices()`             |
| Longest Road card             | 2           | Read from `SpecialCardTracker`              |
| Largest Army card             | 2           | Read from `SpecialCardTracker`              |
| VP development card (in hand) | 1           | Always counted, even if not yet "revealed" |

---

## Key Validation Rules (BVA Targets)

| Rule                                              | Invalid (boundary)                                          | Valid (boundary)                                           |
|---------------------------------------------------|-------------------------------------------------------------|------------------------------------------------------------|
| Longest Road — claim threshold                    | Road of length 4 (not enough to claim)                      | Road of exactly length 5 (first claim)                     |
| Longest Road — steal threshold                    | Matching the current holder's length (tie; no steal)         | Exceeding the holder's length by 1                         |
| Largest Army — claim threshold                    | 2 knights played (not enough to claim)                      | Exactly 3 knights played (first claim)                     |
| Largest Army — steal threshold                    | Matching the current holder's count (tie; no steal)          | Exceeding the holder's count by 1                          |
| Win condition — VP threshold                      | 9 VP (no win)                                               | Exactly 10 VP (win)                                        |
| Win condition — VP counted on own turn only       | Player reaches 10 VP mid-turn (win still triggered at turn end) | Player reaches 10 VP and `endCurrentTurn()` is called  |
| `getTotalVP()` — null player                      | `player` is null                                            | Any valid `Player` instance                                |
| `getTotalVP()` — null tracker                     | `tracker` is null                                           | Any valid `SpecialCardTracker` instance                    |
| `computeLongestRoad()` — no roads placed          | Player has 0 roads; result should be 0                      | Player has at least 1 road; result is ≥ 1                  |
| `computeLongestRoad()` — road broken by opponent  | Opponent settlement splits a 6-road into two 3-road halves  | Unbroken 6-road counts as 6                                |
| `updateLongestRoad()` — negative road length      | `roadLength` < 0                                            | `roadLength` == 0                                          |
| `updateLargestArmy()` — negative knight count     | `knightCount` < 0                                           | `knightCount` == 0                                         |
| `getDevCardVP()` / `computeKnightCount()` — empty hand | `player.getDevelopmentCards()` returns empty list (both return 0) | Hand contains at least one card of the relevant type |

---

## Package Structure

```
src/main/java/domain/
├── ResourceType.java              (enum — existing)
├── TerrainType.java               (enum — existing)
├── PlayerColor.java               (enum — existing)
├── TurnPhase.java                 (enum — existing)
├── Hex.java                       (existing)
├── Vertex.java                    (existing)
├── Edge.java                      (existing)
├── Board.java                     (existing)
├── Player.java                    (existing — MODIFIED)
├── Game.java                      (existing)
├── SetupPhase.java                (existing)
├── DiceRoll.java                      (existing)
├── Bank.java                      (existing)
├── TradeOffer.java                (existing)
├── Turn.java                      (existing)
├── DevelopmentCard.java           (existing)
├── DevelopmentDeck.java           (existing)
├── TurnManager.java               (existing)
├── SpecialCardTracker.java        (NEW)
└── VictoryPointCalculator.java    (NEW)

src/test/java/domain/
├── HexTest.java                   (existing)
├── BoardTest.java                 (existing)
├── PlayerTest.java                (existing — MODIFIED)
├── GameTest.java                  (existing)
├── SetupPhaseTest.java            (existing)
├── DiceRollTest.java                  (existing)
├── BankTest.java                  (existing)
├── TradeOfferTest.java            (existing)
├── TurnTest.java                  (existing)
├── DevelopmentCardTest.java       (existing)
├── DevelopmentDeckTest.java       (existing)
├── TurnManagerTest.java           (existing)
├── SpecialCardTrackerTest.java    (NEW)
└── VictoryPointCalculatorTest.java (NEW)
```

---

## Implementation Notes

- `VictoryPointCalculator` is fully stateless and has no fields. All inputs come in as method parameters. This makes it trivially easy to test — no mocking needed for the calculator itself, only for its inputs.
- `SpecialCardTracker` should be constructed once alongside `TurnManager` and passed into both `TurnManager` (which passes it to `VictoryPointCalculator`) and any caller that needs to query special card holders for display purposes.
- The Longest Road DFS must track **visited edges**, not visited vertices, to correctly handle road loops (a loop of 6 roads should count as 6, not terminate early when it returns to a visited vertex).
- Road-breaking by opponent settlements requires checking, for each edge in the player's road network, whether the connecting vertex is occupied by an opponent. The `Vertex.getOwner()` method already supports this. The DFS should refuse to cross a vertex owned by another player when traversing between edges.
- VP development cards in a player's hand are always counted toward `getTotalVP()`, even before they are "revealed." The ruleset states they may be revealed on the turn purchased if they push the player to 10+ VP — but since `VictoryPointCalculator` always counts them, this is handled automatically.
- `getWinner()` is called by `TurnManager.endCurrentTurn()` after `updateSpecialCards()` has already run. The order matters: special cards must be reassigned before VP is totaled, or a player who just earned Longest Road might not be credited correctly.
- `getDevCardVP()` and `computeKnightCount()` both depend on `Player.getDevelopmentCards()` (new in this revision). This is the only cross-class change required by this doc — implement it on `Player` first, since both `VictoryPointCalculatorTest` and `PlayerTest` will need it.
- The tie-breaking rule (multiple players at 10+ VP) is theoretically impossible in standard Catan since VP are only checked on the active player's turn. The rule is included defensively and resolves by turn order position in `game.getPlayers()`.