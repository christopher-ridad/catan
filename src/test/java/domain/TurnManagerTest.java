package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TurnManagerTest {
    private Board board;
    private Game game;
    private List<Player> playerList;
    private Player p1, p2, p3, p4;
    private DiceRoll dice;
    private Bank bank;
    private Random mockRandom;
    private TurnManager turnManager;

    private Board createBoard() {
        List<Hex> hexes = new ArrayList<>();

        hexes.add(new Hex(TerrainType.PASTURE, 2));
        hexes.add(new Hex(TerrainType.PASTURE, 4));
        hexes.add(new Hex(TerrainType.PASTURE, 5));
        hexes.add(new Hex(TerrainType.PASTURE, 11));
        hexes.add(new Hex(TerrainType.FOREST, 3));
        hexes.add(new Hex(TerrainType.FOREST, 8));
        hexes.add(new Hex(TerrainType.FOREST, 9));
        hexes.add(new Hex(TerrainType.FOREST, 11));
        hexes.add(new Hex(TerrainType.DESERT, 0));
        hexes.add(new Hex(TerrainType.FIELDS, 4));
        hexes.add(new Hex(TerrainType.FIELDS, 6));
        hexes.add(new Hex(TerrainType.FIELDS, 9));
        hexes.add(new Hex(TerrainType.FIELDS, 12));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 3));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 8));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 10));
        hexes.add(new Hex(TerrainType.HILLS, 5));
        hexes.add(new Hex(TerrainType.HILLS, 6));
        hexes.add(new Hex(TerrainType.HILLS, 10));

        return new Board(hexes);
    }

    @BeforeEach
    void setUp() {
        this.board = createBoard();

        p1 = new Player("first", PlayerColor.RED);
        p2 = new Player("second", PlayerColor.BLUE);
        p3 = new Player("third", PlayerColor.WHITE);
        p4 = new Player("fourth", PlayerColor.ORANGE);

        playerList = Arrays.asList(p1, p2, p3, p4);

        this.game = new Game(playerList, board);
        this.bank = new Bank();

        mockRandom = EasyMock.createMock(Random.class);
        // Always return a die value of 2 (1-indexed: 3), so every roll is
        // 3 + 3 = 6 and never triggers the robber (a roll of 7).
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(2).anyTimes();
        EasyMock.replay(mockRandom);
        this.dice = new DiceRoll(mockRandom);

        this.turnManager = new TurnManager(game, bank, dice);
    }

    /** Advances {@code turn} from PRODUCTION all the way to the BUILD phase. */
    private void advanceToBuild(Turn turn) {
        turn.rollDice();
        turn.advanceToBuild();
    }

    /** Gives {@code player} exactly 10 VP via 4 settlements and 3 cities. */
    private void giveTenVictoryPoints(Player player) {
        for (int i = 0; i < 4; i++) {
            board.getVertex(i).setOwner(player);
        }
        for (int i = 4; i < 7; i++) {
            Vertex vertex = board.getVertex(i);
            vertex.setOwner(player);
            vertex.upgradeToCity();
        }
    }

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------

    @Test
    public void Constructor_WithNullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TurnManager(null, bank, dice));
    }

    @Test
    public void Constructor_WithNullBank_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TurnManager(game, null, dice));
    }

    @Test
    public void Constructor_WithNullDice_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TurnManager(game, bank, null));
    }

    @Test
    public void Constructor_InitializesCurrentPlayerToFirstPlayer() {
        assertEquals(p1, turnManager.getCurrentPlayer());
    }

    @Test
    public void Constructor_InitializesTurnNumberToZero() {
        assertEquals(0, turnManager.getCurrentTurnNumber());
    }

    @Test
    public void Constructor_DoesNotStartFirstTurnAutomatically() {
        assertTrue(turnManager.getCurrentTurn().isEmpty());
    }

    @Test
    public void Constructor_GameIsNotOverInitially() {
        assertFalse(turnManager.isGameOver());
        assertTrue(turnManager.getWinner().isEmpty());
    }

}
