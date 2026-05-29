package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TDD tests for Game class (written before implementation)
 *
 * Adresses issue #9: Implement Game setup with player count validation.
 *
 * Board is constructed via its private constructor
 * to bypass validation (no need to instantiate 19 hexes).
 *
 * Player is stubbed below — remove the stub once PR #17 is merged.
 */

public class GameTest {

    // Stub — remove once PR #17 (wip/player-class) is merged
    static class Player {
        private final String name;
        private final PlayerColor color;

        Player(String name, PlayerColor color) {
            this.name = name;
            this.color = color;
        }

        public String getName()       {
            return name;
        }

        public PlayerColor getColor() {
            return color;
        }
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

    @Test
    void gameConstructor_withNullPlayerInList_throwsNullPointer() {
        assertThrows(NullPointerException.class,
                () -> new Game(Arrays.asList(alice, null), board));
    }

    @Test
    void gameConstructor_withNullBoard_throwsNullPointer() {
        assertThrows(NullPointerException.class,
                () -> new Game(Arrays.asList(alice, bob), null));
    }

    //
    // Fetching player list
    //

    @Test
    void getPlayers_returnsPlayersInOriginalOrder() {
        List<Object> input = Arrays.asList(alice, bob, charlie);
        Game game = new Game(input, board);
        List<Object> result = game.getPlayers();
        assertEquals(input.size(), result.size());
        for (int i = 0; i < input.size(); i++) {
            assertSame(input.get(i), result.get(i));
        }
    }

    @Test
    void getPlayers_returnsUnmodifiableList() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertThrows(UnsupportedOperationException.class,
                () -> game.getPlayers().add(charlie));
    }

    //
    // Player count
    //

    @Test
    void getPlayerCount_withTwoPlayers_returnsTwo() {
        assertEquals(2, new Game(Arrays.asList(alice, bob), board).getPlayerCount());
    }

    @Test
    void getPlayerCount_withFourPlayers_returnsFour() {
        assertEquals(4, new Game(Arrays.asList(alice, bob, charlie, diana), board).getPlayerCount());
    }

    //
    // getPlayer by index
    //

    @Test
    void getPlayer_withIndexZero_returnsFirstPlayer() {
        Game game = new Game(Arrays.asList(alice, bob, charlie), board);
        assertSame(alice, game.getPlayer(0));
    }

    @Test
    void getPlayer_withLastIndex_returnsLastPlayer() {
        Game game = new Game(Arrays.asList(alice, bob, charlie), board);
        assertSame(charlie, game.getPlayer(2));
    }

    //
    // Fetch board
    //

    @Test
    void getBoard_returnsValidBoardObject() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertSame(board, game.getBoard());
    }

    //
    // Duplicate color validation
    //

    @Test
    void gameConstructor_withTwoPlayersOfSameColor_throwsIllegalArgument() {
        Player aliceRed = new Player("Alice2", PlayerColor.RED); // RED same as alice
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, aliceRed), board));
    }

    @Test
    void gameConstructor_withDuplicateColorAmongFourPlayers_throwsIllegalArgument() {
        Player charlie2 = new Player("Charlie2", PlayerColor.BLUE); // BLUE same as bob
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, bob, charlie2, diana), board));
    }

    @Test
    void gameConstructor_withAllUniqueColors_doesNotThrow() {
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob, charlie, diana), board));
    }

}
