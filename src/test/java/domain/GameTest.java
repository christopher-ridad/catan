package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

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

    @Test
    void gameConstructor_withFivePlayers_throwsIllegalArgument() {
        Player eve = new Player("Eve", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, bob, charlie, diana, eve), board));
    }

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

    @Test
    void getPlayers_returnsPlayersInOriginalOrder() {
        List<Player> input = Arrays.asList(alice, bob, charlie);
        Game game = new Game(input, board);
        List<Player> result = game.getPlayers();
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

    @Test
    void getPlayerCount_withTwoPlayers_returnsTwo() {
        assertEquals(2, new Game(Arrays.asList(alice, bob), board).getPlayerCount());
    }

    @Test
    void getPlayerCount_withFourPlayers_returnsFour() {
        assertEquals(4, new Game(Arrays.asList(alice, bob, charlie, diana), board).getPlayerCount());
    }

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

    @Test
    void getBoard_returnsValidBoardObject() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertSame(board, game.getBoard());
    }

    @Test
    void gameConstructor_withTwoPlayersOfSameColor_throwsIllegalArgument() {
        Player aliceRed = new Player("Alice2", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, aliceRed), board));
    }

    @Test
    void gameConstructor_withDuplicateColorAmongFourPlayers_throwsIllegalArgument() {
        Player charlie2 = new Player("Charlie2", PlayerColor.BLUE);
        assertThrows(IllegalArgumentException.class,
                () -> new Game(Arrays.asList(alice, bob, charlie2, diana), board));
    }

    @Test
    void gameConstructor_withAllUniqueColors_doesNotThrow() {
        assertDoesNotThrow(() -> new Game(Arrays.asList(alice, bob, charlie, diana), board));
    }

    @Test
    void getRemainingDevelopmentCardCount_newGame_returnsTwentyFive() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertEquals(25, game.getRemainingDevelopmentCardCount());
    }

    @Test
    void drawDevelopmentCard_removesCardFromDeck_decrementsRemainingCount() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        int before = game.getRemainingDevelopmentCardCount();

        DevelopmentCard card = game.drawDevelopmentCard();

        assertNotNull(card);
        assertEquals(before - 1, game.getRemainingDevelopmentCardCount());
    }

    @Test
    void drawDevelopmentCard_emptyDeck_throwsIllegalState() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        for (int i = 0; i < 25; i++) {
            game.drawDevelopmentCard();
        }

        assertThrows(IllegalStateException.class, game::drawDevelopmentCard);
    }

    @Test
    void getPlayerHand_newGame_returnsEmptyHand() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertTrue(game.getPlayerHand(alice).isEmpty());
    }

    @Test
    void getPlayerHand_playerNotInGame_throwsIllegalArgument() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        assertThrows(IllegalArgumentException.class, () -> game.getPlayerHand(charlie));
    }

    @Test
    void addDevelopmentCardToHand_addsCardToPlayersHand() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        DevelopmentCard card = game.drawDevelopmentCard();

        game.addDevelopmentCardToHand(alice, card);

        assertEquals(List.of(card), game.getPlayerHand(alice));
    }

    @Test
    void addDevelopmentCardToHand_playerNotInGame_throwsIllegalArgument() {
        Game game = new Game(Arrays.asList(alice, bob), board);
        DevelopmentCard card = game.drawDevelopmentCard();

        assertThrows(IllegalArgumentException.class, () -> game.addDevelopmentCardToHand(charlie, card));
    }

}
