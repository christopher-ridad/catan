package domain;

public final class DevelopmentCard {
    private final DevelopmentCardType type;
    private boolean played = false;
    private int turnPurchased = 0;

    public DevelopmentCard(DevelopmentCardType type) {
        if (type == null) {
            throw new IllegalArgumentException("DevelopmentCard type cannot be null");
        }
        this.type = type;
    }

    public boolean isPlayed() {
        return played;
    }

    public DevelopmentCardType getType() {
        return type;
    }

    public void markAsPlayed() {
        if (type == DevelopmentCardType.VICTORY_POINT) {
            throw new IllegalStateException("Victory Point cards cannot be marked as played");
        }
        if (played) {
            throw new IllegalStateException("Card has already been played");
        }
        this.played = true;
    }

    public int getTurnPurchased() {
        return turnPurchased;
    }

    void setTurnPurchased(int turn) {
        this.turnPurchased = turn;
    }

    public boolean isPlayableOnTurn(int currentTurn) {
        if (type == DevelopmentCardType.VICTORY_POINT) {
            return false;
        }
        return currentTurn > turnPurchased && !played;
    }
}