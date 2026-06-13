package domain;

import java.util.Objects;
import java.util.Optional;

public class SpecialCardTracker {

    private Player longestRoadHolder;
    private int longestRoadLength;
    private Player largestArmyHolder;
    private int largestArmySize;

    public SpecialCardTracker() {
        this.longestRoadHolder = null;
        this.longestRoadLength = 0;
        this.largestArmyHolder = null;
        this.largestArmySize = 0;
    }

    public Optional<Player> getLongestRoadHolder() {
        return Optional.ofNullable(this.longestRoadHolder);
    }

    public int getLongestRoadLength() {
        return this.longestRoadLength;
    }

    public Optional<Player> getLargestArmyHolder() {
        return Optional.ofNullable(this.largestArmyHolder);
    }

    public int getLargestArmySize() {
        return this.largestArmySize;
    }

    public void updateLongestRoad(Player candidate, int roadLength) {
        Objects.requireNonNull(candidate, "Candidate cannot be null");
        if (roadLength < 0) {
            throw new IllegalArgumentException("Road length cannot be negative");
        }
        if (this.longestRoadHolder == null) {
            if (roadLength >= 5) {
                this.longestRoadHolder = candidate;
                this.longestRoadLength = roadLength;
            }
        } else if (this.longestRoadHolder.equals(candidate)) {
            if (roadLength >= 5) {
                this.longestRoadLength = roadLength;
            } else {
                this.longestRoadHolder = null;
                this.longestRoadLength = 0;
            }
        } else if (roadLength > this.longestRoadLength) {
            this.longestRoadHolder = candidate;
            this.longestRoadLength = roadLength;
        }
    }

    public boolean holdsLongestRoad(Player player) {
        return this.longestRoadHolder != null && this.longestRoadHolder.equals(player);
    }

    public void updateLargestArmy(Player candidate, int knightCount) {
        Objects.requireNonNull(candidate, "Candidate cannot be null");
        if (knightCount < 0) {
            throw new IllegalArgumentException("Knight count cannot be negative");
        }
        if (this.largestArmyHolder == null) {
            if (knightCount >= 3) {
                this.largestArmyHolder = candidate;
                this.largestArmySize = knightCount;
            }
        } else if (knightCount > this.largestArmySize) {
            this.largestArmyHolder = candidate;
            this.largestArmySize = knightCount;
        }
    }

    public boolean holdsLargestArmy(Player player) {
        return this.largestArmyHolder != null && this.largestArmyHolder.equals(player);
    }
}
