package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DevelopmentDeckTest {
    @Test
    void Constructor_CreatesFullDeck_HasTwentyFiveCards() {
        DevelopmentDeck deck = new DevelopmentDeck();
        assertEquals(25, deck.getRemainingCount());
    }
}
