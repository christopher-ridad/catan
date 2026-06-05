package domain;

import java.util.Objects;

public class DevelopmentCard {
    private final DevelopmentCardType type;
    private boolean played = false;

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
}