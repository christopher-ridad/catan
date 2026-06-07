# Integration Testing Plan

## Overview
Integration testing verifies that multiple classes work together correctly as a system.
Per course requirements, integration testing must be done on at least 2 main features.
Unlike unit tests which test classes in isolation, integration tests use real implementations
of all dependencies with no mocking.

---

## Feature 1: Setup Phase

`SetupPhaseTest` serves as the integration test for this feature. It uses real implementations
of `Board`, `Game`, `Player`, `Vertex`, `Edge`, and `Hex` with no mocking, testing the full
two-round setup sequence end to end including settlement placement, road placement, distance
rule enforcement, turn order, and resource distribution.

**Classes involved:** `SetupPhase`, `Game`, `Board`, `Vertex`, `Edge`, `Player`, `Hex`

**Test file:** `src/test/java/domain/SetupPhaseTest.java`

---

## Feature 2: Turn Phase

Integration testing for the Turn Phase will verify that `Turn`, `Bank`, `DiceRoll`, `Player`,
and `Board` work together correctly for a complete turn including dice roll, resource production,
trading, and building.

**What will be tested:**
- Dice roll triggers correct resource production for all players based on real board topology
- Resources flow correctly from `Bank` to `Player` during production
- Player can spend resources to build roads, settlements, and cities on the real board
- Turn phase transitions work correctly end to end

**Classes involved:** `Turn`, `Bank`, `DiceRoll`, `Player`, `Board`, `Vertex`, `Edge`

**Test file:** `src/test/java/domain/TurnTest.java`

---