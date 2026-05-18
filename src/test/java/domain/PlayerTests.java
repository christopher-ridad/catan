package domain;

import org.junit.jupiter.api.Test;

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
}
