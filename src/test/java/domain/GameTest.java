package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameTest {
    // -----------------------------------------------------------------------
    // Stubs — delete these once PR #17 (Player) is merged into your branch
    // -----------------------------------------------------------------------
    enum PlayerColor { RED, BLUE, ORANGE, WHITE }

    static class Player {
        private final String name;
        private final PlayerColor color;

        Player(String name, PlayerColor color) {
            if (name == null) {
                throw new IllegalArgumentException("Player name cannot be null");
            }
            if (name.isBlank()) {
                throw new IllegalArgumentException("Player name cannot be blank");
            }
            if (color == null) {
                throw new IllegalArgumentException("Player color cannot be null");
            }
            if (name.length() > 50) {
                throw new IllegalArgumentException("Player name cannot exceed 50 characters");
            }
            this.name = name;
            this.color = color;
        }

        String getName()       { return name; }
        PlayerColor getColor() { return color; }
    }
    // -----------------------------------------------------------------------
    // End stubs
    // -----------------------------------------------------------------------

    private Player alice;
    private Player bob;
    private Player charlie;
    private Player diana;

    @BeforeEach
    void setUp() {
        alice   = new Player("Alice",   PlayerColor.RED);
        bob     = new Player("Bob",     PlayerColor.BLUE);
        charlie = new Player("Charlie", PlayerColor.ORANGE);
        diana   = new Player("Diana",   PlayerColor.WHITE);
    }

    //
    // Valid construction - boundary values: 2 (min) and 4 (max)
    //

    @Test
    void gameConstructor_withTwoPlayers_doesNotThrow() {
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob)));
    }

}
