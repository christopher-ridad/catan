package domain;

import java.util.Optional;

/**
 * Tracks the current holders of the two special cards — Longest Road and
 * Largest Army — and the qualifying thresholds needed to claim or steal each.
 * Both cards are worth 2 VP to whoever holds them.
 *
 * This class is the single source of truth for which player currently holds
 * each card; VictoryPointCalculator reads from it to compute total VP.
 */
public class SpecialCardTracker {

    private Player longestRoadHolder;
    private int longestRoadLength;

    private Player largestArmyHolder;
    private int largestArmySize;

    public SpecialCardTracker() {
        // TODO: initialize all fields to null/0
    }

    public Optional<Player> getLongestRoadHolder() {
        // TODO: return the current Longest Road holder, or Optional.empty() if unclaimed
        return null;
    }

    public int getLongestRoadLength() {
        // TODO: return the current qualifying road length (0 if unclaimed)
        return 0;
    }

    public Optional<Player> getLargestArmyHolder() {
        // TODO: return the current Largest Army holder, or Optional.empty() if unclaimed
        return null;
    }

    public int getLargestArmySize() {
        // TODO: return the current qualifying knight count (0 if unclaimed)
        return 0;
    }

    /**
     * Awards the Longest Road card to candidate if their roadLength meets the
     * threshold (>= 5 to claim from unclaimed; > current holder's length to
     * steal). No-op otherwise.
     *
     * @throws IllegalArgumentException if roadLength < 0
     */
    public void updateLongestRoad(Player candidate, int roadLength) {
        // TODO: implement
    }

    /**
     * Awards the Largest Army card to candidate if their knightCount meets the
     * threshold (>= 3 to claim from unclaimed; > current holder's count to
     * steal). No-op otherwise.
     *
     * @throws IllegalArgumentException if knightCount < 0
     */
    public void updateLargestArmy(Player candidate, int knightCount) {
        // TODO: implement
    }

    public boolean holdsLongestRoad(Player player) {
        // TODO: return true if the given player currently holds the Longest Road card
        return false;
    }

    public boolean holdsLargestArmy(Player player) {
        // TODO: return true if the given player currently holds the Largest Army card
        return false;
    }
}