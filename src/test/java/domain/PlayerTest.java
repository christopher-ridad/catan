package domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
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

    @Test
    public void getResourceCount_woolType_ReturnsMoreThanOne() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;
        Player playerOne = new Player(playerOneName, playerOneColor);

        playerOne.addResources(ResourceType.WOOL, 1);
        playerOne.addResources(ResourceType.WOOL, 1);
        playerOne.addResources(ResourceType.WOOL, 1);
        assertEquals(3, playerOne.getResourceCount(ResourceType.WOOL));
    }

    @Test
    public void getResourceCount_oreType_ReturnsMaxPossibleInteger() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;
        Map<ResourceType, Integer> maxIntOre = new HashMap<>(Map.of(
                ResourceType.BRICK, 0,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, Integer.MAX_VALUE,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));

        Player playerOne = new Player(playerOneName, playerOneColor, maxIntOre);
        assertEquals(Integer.MAX_VALUE, playerOne.getResourceCount(ResourceType.ORE));
    }

    @Test
    public void getResourceCount_nullResourceType_ThrowsIllegalArgumentException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertThrows(IllegalArgumentException.class, () -> {
            playerOne.getResourceCount(null);
        });
    }

    @Test
    public void addResources_nullResourceType_ThrowsIllegalArgumentException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertThrows(IllegalArgumentException.class, () -> {
            playerOne.addResources(null, 2);
        });
    }

    @Test
    public void addResources_woolType_amountLessThanZero_ThrowsIllegalArgumentException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertThrows(IllegalArgumentException.class, () -> {
            playerOne.addResources(ResourceType.WOOL, -1);
        });
    }

    @Test
    public void addResources_brickType_amountZero_noExceptionThrown() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertDoesNotThrow(() -> {
            playerOne.addResources(ResourceType.BRICK, 0);
        });

        assertEquals(0, playerOne.getResourceCount(ResourceType.BRICK));
    }

    @Test
    public void addResources_lumberType_amountOne_noExceptionThrown() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(0, playerOne.getResourceCount(ResourceType.LUMBER));
        assertDoesNotThrow(() -> {
            playerOne.addResources(ResourceType.LUMBER, 1);
        });

        assertEquals(1, playerOne.getResourceCount(ResourceType.LUMBER));
    }

    @Test
    public void getTotalResourceCount_ReturnsZero() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.ORANGE;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertEquals(0, playerOne.getTotalResourceCount());
    }

    @Test
    public void getTotalResourceCount_ReturnsOne() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.ORANGE;

        Player playerOne = new Player(playerOneName, playerOneColor);

        playerOne.addResources(ResourceType.ORE, 1);
        assertEquals(1, playerOne.getTotalResourceCount());
    }

    @Test
    public void getTotalResourceCount_MaxPossibleValue() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.BLUE;
        Map<ResourceType, Integer> maxResourcesMap = new HashMap<>(Map.of(
                ResourceType.BRICK, 0,
                ResourceType.LUMBER, Integer.MAX_VALUE - 1,
                ResourceType.ORE, 1,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));

        Player playerOne = new Player(playerOneName, playerOneColor, maxResourcesMap);
        assertEquals(Integer.MAX_VALUE, playerOne.getTotalResourceCount());
    }



//            - **TC27: removeResources_lumberType_playerHasEnough_noExceptionThrown** ( not implemented )
//            - State of the system: removeResources called with 3 as amount integer value, lumber as resourceType input, player has 5 lumber
//  - Expected output: `getResourceCount` called on player returns 2

    @Test
    public void removeResources_nullResourceType_throwsIllegalArgumentException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertThrows(IllegalArgumentException.class, () -> {
            playerOne.removeResources(null, 2);
        });
    }

    @Test
    public void removeResources_woolType_amountLessThanZero_ThrowsIllegalArgumentException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        assertThrows(IllegalArgumentException.class, () -> {
            playerOne.removeResources(ResourceType.WOOL, -1);
        });
    }

    @Test
    public void removeResources_brickType_amountZero_noExceptionThrown() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.RED;

        Player playerOne = new Player(playerOneName, playerOneColor);
        playerOne.addResources(ResourceType.BRICK, 1);
        assertDoesNotThrow(() -> {
            playerOne.removeResources(ResourceType.BRICK, 0);
        });

        assertEquals(1, playerOne.getResourceCount(ResourceType.BRICK));
    }

    @Test
    public void removeResources_grainType_playerDoesNotHaveEnough_ThrowsIllegalStateException() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.ORANGE;

        Player playerOne = new Player(playerOneName, playerOneColor);
        playerOne.addResources(ResourceType.GRAIN, 1);
        assertThrows(IllegalStateException.class, () -> {
            playerOne.removeResources(ResourceType.GRAIN, 3);
        });
    }

    @Test
    public void removeResources_lumberType_playerHasEnough_noExceptionThrown() {
        String playerOneName = "Bob";
        PlayerColor playerOneColor = PlayerColor.ORANGE;

        Player playerOne = new Player(playerOneName, playerOneColor);
        playerOne.addResources(ResourceType.LUMBER, 5);
        playerOne.removeResources(ResourceType.LUMBER, 3);

        assertEquals(2, playerOne.getResourceCount(ResourceType.LUMBER));
    }
}
