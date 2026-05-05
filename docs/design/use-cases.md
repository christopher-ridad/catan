# Catan GUI – Use Cases (Complex Flows)

Scope: High-complexity setup flows only. Assumes **single shared device** — players pass the device to the active player on their turn. Simpler epics are covered sufficiently by user stories alone.

---

## UC-01: Variable Board Generation with Constraint Validation

**Actor:** System (triggered on game start with Variable setup selected)

**Preconditions:**
- Number of players (3–4) has been set.
- Variable setup mode is selected.

**Main Success Scenario:**
1. System shuffles the 19 terrain hexes and places them randomly in the 3-4-5-4-3 hex grid.
2. System randomly assigns the 9 harbor pieces to the 9 harbor slots on the frame.
3. System identifies the Desert hex and places the robber on it.
4. System places number tokens alphabetically (A–R) in a counterclockwise spiral from a corner toward the center, skipping the Desert hex.
5. System checks that no two red tokens (6 and 8) are placed on adjacent hexes.
6. Constraint passes — board is finalized and displayed to all players.

**Alternate Flow A – Red Token Adjacency Violation:**
- At step 5, one or more pairs of red tokens are found on adjacent hexes.
- System discards the token placement and reruns step 4.
- System repeats up to 50 attempts.
- If a valid arrangement is found, resume at step 6.

**Alternate Flow B – Max Retries Exceeded:**
- After 50 failed token placements, System reshuffles terrain hexes and restarts from step 1.
- If still unresolved after 3 full restarts, System displays an error and presents a manual **"Randomize Board"** button for the host to retry.

**Postconditions:**
- Board is displayed with all 19 hexes, 18 number tokens, 9 harbors, and the robber on the Desert.
- No two red tokens are adjacent.
- All players can see the finalized board before proceeding to color selection.

---

## UC-02: Starting Player Determination via Sequential Dice Roll

**Actor:** All Players sequentially (pre-game, after color selection); System

**Preconditions:**
- All players have completed color selection.
- No starting player has been designated yet.
- Device is with Player 1 (first in the configured player list).

**Main Success Scenario:**
1. System displays a **"Roll for Starting Player"** screen showing all player names and empty roll result slots.
2. System prompts the current player: *"[Player Name], tap Roll Dice."*
3. Active player taps the Roll Dice button.
4. System rolls both dice, displays the result for that player, and locks it in.
5. System displays: *"Pass the device to [Next Player Name]."*
6. Next player taps **"I'm Ready"** to dismiss the handoff screen.
7. Steps 2–6 repeat until all players have rolled.
8. System displays all roll results simultaneously and highlights the highest roller.
9. System announces: *"[Player Name] goes first!"* and sets clockwise turn order from that player.
10. System transitions to the Setup Phase (UC-03) with the starting player as the active player.

**Alternate Flow A – Tie Between Two or More Players:**
- At step 8, two or more players share the highest roll.
- System identifies the tied players and displays: *"Tie! [Names] must re-roll."*
- System re-runs steps 2–8 for tied players only, in their original order.
- Repeat until a single highest roller is determined.

**Alternate Flow B – Active Player Accidentally Sees Another's Roll Early:**
- Not applicable — rolls are sequential and each player's result is displayed only after they tap Roll. All results are revealed together at step 8.

**Postconditions:**
- A single starting player is established.
- Clockwise turn order is set from the starting player.
- Device is with the starting player, ready for the Setup Phase.

---

## UC-03: Settlement Placement with Distance Rule Enforcement

**Actor:** Active Player

**Preconditions:**
- It is the active player's turn to place a settlement (Round 1 or Round 2 of the Setup Phase).
- The device has been passed to the active player and they have tapped **"I'm Ready"**.
- The player has at least 1 settlement piece remaining.
- Settlement cost is waived during the Setup Phase.

**Main Success Scenario:**
1. System highlights all valid intersections on the board (those satisfying the Distance Rule and unoccupied).
2. Active player reviews the board and taps a highlighted intersection.
3. System places the active player's settlement on the selected intersection.
4. System decrements the player's settlement count by 1.
5. System updates the valid placement set for all future placements (removes the selected intersection and its neighbors).
6. Proceed to road placement (UC-04).

**Alternate Flow A – Player Taps Invalid Intersection:**
- At step 2, the player taps a non-highlighted intersection.
- System ignores the tap and displays a brief inline message: *"That spot is too close to an existing settlement."*
- Return to step 2.

**Alternate Flow B – No Valid Intersections Exist:**
- At step 1, no intersections pass the Distance Rule filter.
- System displays: *"No valid placements available."* (Should be unreachable in a standard game — treated as a critical error.)
- System halts the setup phase and prompts the host to restart.

**Postconditions:**
- Settlement is placed on the selected intersection.
- Distance Rule is enforced for all subsequent placements by any player.
- Road placement (UC-04) is immediately triggered.

---

## UC-04: Road Placement Adjacent to New Settlement

**Actor:** Active Player (immediately after UC-03 completes)

**Preconditions:**
- Active player has just placed a settlement (UC-03 complete).
- The player has at least 1 road piece remaining.
- Road cost is waived during the Setup Phase.

**Main Success Scenario:**
1. System identifies the 2–3 paths directly adjacent to the just-placed settlement.
2. System filters out any paths already occupied by a road.
3. System highlights the remaining valid adjacent paths.
4. Active player taps a highlighted path.
5. System places the player's road on the selected path.
6. System decrements the player's road count by 1.
7. If this is **Round 2**, System triggers resource distribution (UC-05) before the handoff.
8. System displays the device handoff screen: *"Pass the device to [Next Player Name]."*
9. Next player taps **"I'm Ready"** to begin their turn.

**Alternate Flow A – Player Taps Non-Adjacent or Occupied Path:**
- At step 4, the player taps an invalid path.
- System ignores the tap and displays: *"Your road must connect to your new settlement."*
- Return to step 4.

**Alternate Flow B – All Adjacent Paths Occupied:**
- At step 3, all adjacent paths are already taken (rare edge case in late setup).
- System skips road placement, logs the event, and proceeds to step 7.

**Postconditions:**
- Road is placed on a path adjacent to the new settlement.
- Player's road count is decremented.
- If Round 2, resources have been distributed (UC-05).
- Device handoff screen is shown to transition to the next player.

---

## UC-05: Round 2 Initial Resource Distribution

**Actor:** System (triggered automatically after active player completes road placement in Round 2)

**Preconditions:**
- Active player has just completed settlement and road placement for Round 2 (UC-03 and UC-04 complete).
- Resource distribution has not yet occurred for this player's second settlement.

**Main Success Scenario:**
1. System identifies the 1–3 terrain hexes adjacent to the player's second settlement.
2. System filters out the Desert hex and any hex without a number token.
3. For each remaining adjacent hex, System maps the terrain type to its resource.
4. System adds 1 of each identified resource card to the active player's hand.
5. System decrements the bank supply for each distributed resource.
6. System displays a resource summary screen to the active player: *"You received: [Resource list]."*
7. Active player reviews their resources and taps **"Done"** to confirm.
8. System dismisses the summary and triggers the device handoff screen (UC-04, step 8).

**Alternate Flow A – Bank Runs Out of a Resource:**
- At step 5, the bank has 0 of a required resource.
- System gives 0 of that resource to the player.
- System includes a warning in the summary screen: *"The bank ran out of [Resource] — you did not receive it."*
- Continue with remaining resources normally.

**Alternate Flow B – Settlement Adjacent Only to Desert:**
- At step 2, all adjacent hexes are filtered out (settlement placed next to only the Desert).
- System distributes no resources.
- System displays: *"No resources received — your settlement only borders the Desert."*
- Active player taps **"Done"** and device handoff proceeds normally.

**Postconditions:**
- Active player's hand contains 1 resource for each non-Desert adjacent hex.
- Bank supply is decremented accordingly.
- Active player has reviewed and confirmed their starting resources before passing the device.
- After all players complete Round 2, the starting player (Round 1 first placer) begins the first full turn.