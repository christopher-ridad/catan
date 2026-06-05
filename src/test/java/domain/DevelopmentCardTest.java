package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardTest {

    @Test
    void Constructor_WithNullType_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new DevelopmentCard(null));
        assertEquals("DevelopmentCard type cannot be null", exception.getMessage());
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
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                card::markAsPlayed);
        assertEquals("Card has already been played", exception.getMessage());
    }

    @Test
    void MarkAsPlayed_OnVictoryPointCard_ThrowsIllegalStateException() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT);
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                card::markAsPlayed);
        assertEquals("Victory Point cards cannot be marked as played", exception.getMessage());
    }
}
