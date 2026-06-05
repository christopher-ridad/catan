package domain;

import java.util.Objects;

public class DevelopmentCard {
    private final DevelopmentCardType type;

    public DevelopmentCard(DevelopmentCardType type) {
        if (type == null) {
            throw new IllegalArgumentException("DevelopmentCard type cannot be null");
        }
        this.type = type;
    }
}