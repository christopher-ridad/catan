package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        PlayerColor playerOneColor = PlayerColor.WHITE;
        assertThrows(IllegalArgumentException.class, () -> {
            Player playerOne = new Player(playerOneName, playerOneColor);
        });
    }

    @Test
    public void constructor_WithNameLengthEqualToMax_NoExceptionThrown() {
        String playerOneName = "BOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBOBO";
        PlayerColor playerOneColor = PlayerColor.RED;
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

    @Test
    public void constructor_WithCorrectName_NoExceptionThrown() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.ORANGE;
        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(playerOneColor, playerOne.getColor());
        assertEquals(playerOneName, playerOne.getName());
    }

    @Test
    public void getName_OnValidPlayer_ReturnsPlayerNameString() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.BLUE;
        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(playerOneName, playerOne.getName());
    }

    @Test
    public void getColor_OnValidPlayer_ReturnsPlayerColor() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;
        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(playerOneColor, playerOne.getColor());
    }

    @Test
    public void getResourceCount_brickType_ReturnsZero() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;
        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(0, playerOne.getResourceCount(ResourceType.BRICK));
    }

    @Test
    public void getResourceCount_lumberType_ReturnsOne() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;
        Player playerOne = new Player(playerOneName, playerOneColor);

        playerOne.addResources(ResourceType.LUMBER, 1);
        assertEquals(1, playerOne.getResourceCount(ResourceType.LUMBER));
    }
}
