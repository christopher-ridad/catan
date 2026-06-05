package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardTest {

    @Test
    void Constructor_WithNullType_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DevelopmentCard(null));
    }

    @Test
    void Constructor_WithValidType_NoExceptionThrown() {
        assertDoesNotThrow(() -> new DevelopmentCard(DevelopmentCardType.KNIGHT));
    }

    @Test
    void GetType_ReturnsCorrectType() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.ROAD_BUILDING);
        assertEquals(DevelopmentCardType.ROAD_BUILDING, card.getType());
    }

    @Test
    void IsPlayed_WhenNewCard_ReturnsFalse() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY);
        assertFalse(card.isPlayed());
    }

    @Test
    void IsPlayed_AfterMarkAsPlayed_ReturnsTrue() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY);
        card.markAsPlayed();
        assertTrue(card.isPlayed());
    }

    @Test
    void MarkAsPlayed_OnUnplayedCard_NoExceptionThrown() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.MONOPOLY);
        assertDoesNotThrow(card::markAsPlayed);
    }

    @Test
    void MarkAsPlayed_OnAlreadyPlayedCard_ThrowsIllegalStateException() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.MONOPOLY);
        card.markAsPlayed();
        assertThrows(IllegalStateException.class, card::markAsPlayed);
    }
}
