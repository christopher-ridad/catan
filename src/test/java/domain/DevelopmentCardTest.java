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

    @Test
    void GetTurnPurchased_OnNewCard_ReturnsZero() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        assertEquals(0, card.getTurnPurchased());
    }

    @Test
    void GetTurnPurchased_AfterSetTurnPurchased_ReturnsSetValue() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        card.setTurnPurchased(3);
        assertEquals(3, card.getTurnPurchased());
    }

    @Test
    void IsPlayableOnTurn_WhenCurrentTurnGreaterThanPurchased_ReturnsTrue() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        card.setTurnPurchased(1);
        assertTrue(card.isPlayableOnTurn(2));
    }

    @Test
    void IsPlayableOnTurn_WhenCurrentTurnEqualsToPurchased_ReturnsFalse() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        card.setTurnPurchased(1);
        assertFalse(card.isPlayableOnTurn(1));
    }

    @Test
    void IsPlayableOnTurn_WhenCardAlreadyPlayed_ReturnsFalse() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        card.setTurnPurchased(1);
        card.markAsPlayed();
        assertFalse(card.isPlayableOnTurn(2));
    }

    @Test
    void IsPlayableOnTurn_WhenVictoryPointCard_ReturnsFalse() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT);
        card.setTurnPurchased(1);
        assertFalse(card.isPlayableOnTurn(2));
    }
}
