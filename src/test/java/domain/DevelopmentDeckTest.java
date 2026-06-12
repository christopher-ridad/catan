package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DevelopmentDeckTest {
    @Test
    void Constructor_CreatesFullDeck_HasTwentyFiveCards() {
        DevelopmentDeck deck = new DevelopmentDeck();
        assertEquals(25, deck.getRemainingCount());
    }

    @Test
    void Draw_OnFullDeck_ReturnsCard() {
        DevelopmentDeck deck = new DevelopmentDeck();
        DevelopmentCard card = deck.draw(1);
        assertEquals(24, deck.getRemainingCount());
    }

    @Test
    void Draw_SetsCorrectTurnPurchased() {
        DevelopmentDeck deck = new DevelopmentDeck();
        DevelopmentCard card = deck.draw(3);
        assertEquals(3, card.getTurnPurchased());
    }
}
