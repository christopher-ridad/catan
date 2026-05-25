package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // Stub — remove once PR #17 (wip/player-class) is merged
    static class Player {
        private final String name;
        private final PlayerColor color;

        Player(String name, PlayerColor color) {
            this.name = name;
            this.color = color;
        }

        public String getName()       { return name; }
        public PlayerColor getColor() { return color; }
    }
    // End stub

    private Board board;
    private Player alice;
    private Player bob;
    private Player charlie;
    private Player diana;

    @BeforeEach
    void setUp() {
        board   = new Board(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
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
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob), board));
    }

    @Test
    void gameConstructor_withThreePlayers_doesNotThrow() {
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob, charlie), board));
    }

    @Test
    void gameConstructor_withFourPlayers_doesNotThrow() {
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob, charlie, diana), board));
    }

    //
    // Invalid player count - below min players (2)
    //

    @Test
    void gameConstructor_withNoPlayers_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Collections.emptyList(), board));
    }

    @Test
    void gameConstructor_withOnePlayer_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Collections.singletonList(alice), board));
    }

    //
    // Invalid player count — above max players (4)
    //

    @Test
    void gameConstructor_withFivePlayers_throwsIllegalArgument() {
        Player eve = new Player("Eve", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, bob, charlie, diana, eve), board));
    }

    //
    // Null inputs
    //

    @Test
    void gameConstructor_withNullList_throwsNullPointer() {
        assertThrows(NullPointerException.class, () -> new Game(null, board));
    }
}
