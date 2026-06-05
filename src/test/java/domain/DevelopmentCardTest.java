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
}
