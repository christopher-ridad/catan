package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {
    @Test
    public void constructor_WithNullName_ThrowsIllegalArgumentException() {
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player(null, playerOneColor);
        });
    }

    @Test
    public void constructor_WithEmptyStringForName_ThrowsIllegalArgumentException() {
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player("", playerOneColor);
        });
    }

    @Test
    public void constructor_WithSpacesForName_ThrowsIllegalArgumentException() {
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player("   ", playerOneColor);
        });
    }

    @Test
    public void constructor_WithNullColor_ThrowsIllegalArgumentException() {
        String playerOneName = "Bob";
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player(playerOneName, null);
        });
    }

    @Test
    public void constructor_WithNameLengthGreaterThanMax_ThrowsIllegalArgumentException() {
        String playerOneName = "BOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOB";
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player(playerOneName, playerOneColor);
        });
    }

    @Test
    public void constructor_WithNameLengthEqualToMax_NoExceptionThrown() {
        String playerOneName = "BOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBO";
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertDoesNotThrow(() -> {
            Player playerOne = new Player(playerOneName, playerOneColor);
        });
    }

    @Test
    public void constructor_WithNameLengthLessThanMax_NoExceptionThrown() {
        String playerOneName = "BOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOB";
        PlayerColor playerOneColor = PlayerColor.BLUE;
        assertDoesNotThrow(() -> {
            Player playerOne = new Player(playerOneName, playerOneColor);
        });
    }
}
