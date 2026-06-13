package domain;

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
}
