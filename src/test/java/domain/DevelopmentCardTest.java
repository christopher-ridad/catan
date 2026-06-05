package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevelopmentCardTest {

    @Test
    void Constructor_WithNullType_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DevelopmentCard(null));
    }

    @Test
    void Constructor_WithValidType_NoExceptionThrown() {
        assertDoesNotThrow(() -> new DevelopmentCard(DevelopmentCardType.KNIGHT));
    }
}
