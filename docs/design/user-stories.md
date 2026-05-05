# Catan: Setup User Stories

All user stories related to game setup, including board generation, player configuration, and the initial placement phase.

---

## Notes & Assumptions

- **This game is played on a single shared device.** Players pass the device to the active player when it is their turn. No networked or multi-device play is in scope.
- "Active player" refers to the player whose turn it currently is during the setup phase.
- All placement validation (Distance Rule, road adjacency) is enforced by the game engine, not left to players.
- Resource card counts in the bank start at their full supply values (Brick: 19, Lumber: 19, Ore: 19, Grain: 19, Wool: 19).
- 3-player games use only 3 of the 4 colors; the unused color's pieces are not in play (red).
- Player hands (resource cards, development cards) are not persistently visible on screen between turns — a player must actively open their hand view. This prevents other players from seeing hand contents when passing the device.

---

## Epic 1: Game Mode Selection

**US-01** – As a player, I want to choose between a **Beginner (Fixed)** and **Experienced (Variable)** board setup so that I can play with a predictable layout when learning or a randomized one for replayability.

**US-02** – As a player, I want to select the **number of players (3–4)** before the game begins so that the correct number of seats, colors, and pieces are prepared.

---

## Epic 2: Board Generation

### 2a. Fixed (Beginner) Setup

**US-03** – As a player using the Beginner setup, I want the 19 terrain hexes and number tokens placed exactly according to the beginner's map so that the board matches the standard introductory layout.

### 2b. Variable (Experienced) Setup

**US-04** – As a player using the Variable setup, I want the 19 terrain hexes placed randomly in the standard 3-4-5-4-3 hex grid arrangement so that each game has a unique board.

**US-05** – As a player using the Variable setup, I want the 9 harbor pieces assigned randomly to the 9 harbor slots on the frame so that trade port locations vary between games.

**US-06** – As a player using the Variable setup, I want the 18 number tokens placed alphabetically (A–R) in a counterclockwise spiral from a corner toward the center, skipping the Desert hex, so that token placement follows official rules.

**US-07** – As a player using the Variable setup, I want the game to enforce that no two red number tokens (6 and 8) are adjacent to each other so that the board is valid and balanced.

**US-08** – As a player, I want to see the board visually regenerated with a **"Randomize Board"** action before the game starts so that I can re-roll the layout if I want a different configuration.

---

## Epic 3: Pre-Game Component Setup

**US-09** – As a player, I want to see 5 resource card stacks (Brick, Grain, Lumber, Ore, Wool) displayed beside the board with their current counts so that I can track the bank supply at all times.

**US-10** – As a player, I want the development cards shuffled and placed face-down as a deck so that their contents are hidden from all players during play.

**US-11** – As a player, I want the "Longest Road" and "Largest Army" special cards displayed beside the board so that players know they are available and unclaimed.

**US-12** – As a player, I want the **robber** automatically placed on the Desert hex at game start so that setup matches official rules.

---

## Epic 4: Player Configuration

**US-13** – As the host, I want to step through each player's color selection **one player at a time** (Player 1 picks, then Player 2, etc.) so that each player can choose privately without seeing future options being locked out ahead of time.

**US-14** – As a player selecting a color, I want already-chosen colors to be visually disabled so that I can only pick from colors that are still available.

**US-15** – As a player, I want each player to start with 5 settlements, 4 cities, and 15 roads tracked in the UI so that remaining piece counts are always visible.

**US-16** – As a player, I want each player to have a **Building Costs reference card** accessible in the UI so that I can check costs without leaving the game screen.

---

## Epic 5: Starting Player Determination

**US-17** – As a player, I want the UI to prompt each player in sequence to tap a **"Roll Dice"** button (passing the device between players) so that each player rolls for starting order on the shared device.

**US-18** – As a player, I want the UI to display all players' roll results together after everyone has rolled and clearly indicate the **highest roller as the starting player** so that the outcome is transparent to everyone at the table.

**US-19** – As a player, I want ties in the starting roll to trigger a re-roll prompt for only the tied players so that there is always a single, unambiguous starting player.

---

## Epic 6: Device Handoff

**US-20** – As a player, I want the UI to display a clear **"Pass the device to [Player Name]"** screen between turns during the setup phase so that the correct player is always acting on their turn.

**US-21** – As the incoming active player, I want to tap a **"I'm ready"** confirmation button to dismiss the handoff screen and begin my turn so that I control when my turn starts and no actions are taken accidentally.

---

## Epic 7: Initial Placement Phase – Round 1 (Clockwise)

**US-22** – As the active player in Round 1, I want the UI to highlight valid intersections for settlement placement so that I can see only legal locations (satisfying the Distance Rule).

**US-23** – As the active player in Round 1, I want to click a highlighted intersection to place my first settlement so that placement is intuitive.

**US-24** – As the active player in Round 1, after placing my settlement, I want the UI to highlight only the paths adjacent to that settlement for road placement so that I am guided to place my road legally.

**US-25** – As the active player in Round 1, I want to click a highlighted path to place my first road so that the placement is confirmed and the device handoff screen is shown for the next player.

**US-26** – As a player, I want the Distance Rule enforced automatically so that I cannot place a settlement within 2 intersections of any existing settlement.

**US-27** – As a player, I want turn order to proceed **clockwise** through all players in Round 1 so that each player places exactly one settlement and one road.

---

## Epic 8: Initial Placement Phase – Round 2 (Counter-Clockwise)

**US-28** – As a player, I want Round 2 to begin with the player who went **last** in Round 1 and proceed **counter-clockwise** so that the snake-draft order is correctly applied.

**US-29** – As the active player in Round 2, I want the same placement UI from Round 1 (valid intersection highlights → road placement) so that the interaction is consistent.

**US-30** – As the active player in Round 2, I want to **immediately receive 1 resource card for each terrain hex adjacent to my second settlement** after placing it so that my starting hand is correctly populated.

**US-31** – As a player, I want to see a clearly indicated resource distribution summary after placing my second settlement so that I can verify I received the correct resources before passing the device.

**US-32** – As a player, I want the UI to confirm when all players have completed Round 2 and indicate that the **Round 1 starting player now begins the first full turn** so that the transition into normal play is unambiguous.

---

## Epic 9: Setup Validation & Error Handling

**US-33** – As a player, I want the game to prevent me from confirming setup if any required step (color selection, board generation) has not been completed so that the game never starts in an invalid state.

**US-34** – As a player, I want to see a clear error or warning message if I attempt an illegal placement (e.g., Distance Rule violation, road not adjacent to my settlement) so that I understand why the action was rejected.

**US-35** – As a player using Variable setup, I want the game to automatically re-generate the board if the red token adjacency constraint (no adjacent 6 and 8) cannot be resolved so that I never get stuck on an invalid board state.