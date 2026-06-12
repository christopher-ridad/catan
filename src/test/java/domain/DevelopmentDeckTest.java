package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void Draw_OnEmptyDeck_ThrowsIllegalStateException() {
        DevelopmentDeck deck = new DevelopmentDeck();
        for (int i = 0; i < 25; i++) {
            deck.draw(1);
        }
        assertThrows(IllegalStateException.class, () -> deck.draw(1));
    }

    @Test
    void IsEmpty_OnFullDeck_ReturnsFalse() {
        DevelopmentDeck deck = new DevelopmentDeck();
        assertFalse(deck.isEmpty());
    }

    @Test
    void IsEmpty_AfterAllCardsDrawn_ReturnsTrue() {
        DevelopmentDeck deck = new DevelopmentDeck();
        for (int i = 0; i < 25; i++) {
            deck.draw(1);
        }
        assertTrue(deck.isEmpty());
    }

    @Test
    void GetRemainingCount_OnFullDeck_Returns25() {
        DevelopmentDeck deck = new DevelopmentDeck();
        assertEquals(25, deck.getRemainingCount());
    }

    @Test
    void GetRemainingCount_AfterOneDraw_Returns24() {
        DevelopmentDeck deck = new DevelopmentDeck();
        deck.draw(1);
        assertEquals(24, deck.getRemainingCount());
    }
}
