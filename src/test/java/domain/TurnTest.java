package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TurnTest {
    private Board board;
    private Game game;
    private List<Player> playerList;
    private Player p1, p2, p3, p4;
    private DiceRoll dice;
    private Bank bank;

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

        p1 = new Player("first",   PlayerColor.RED);
        p2 = new Player("second",     PlayerColor.BLUE);
        p3 = new Player("third", PlayerColor.WHITE);
        p4 = new Player("fourth",   PlayerColor.ORANGE);

        playerList = Arrays.asList(p1, p2, p3, p4);

        this.game = new Game(playerList, board);

        this.bank = new Bank();

        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.replay(mockRandom);
        this.dice = new DiceRoll(mockRandom);
    }

    @Test
    public void Constructor_WithNullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(null, p1, dice, bank);
        });
    }

    @Test
    public void Constructor_WithNullActivePlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, null, dice, bank);
        });
    }

    @Test
    public void Constructor_WithNullDice_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, p1, null, bank);
        });
    }

    @Test
    public void Constructor_WithNullBank_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, p1, dice, null);
        });
    }

    @Test
    public void Constructor_WithValidArgs_SetsPhaseToProduction() {
        Turn turn = new Turn(game, p1, dice, bank);
        TurnPhase expected = TurnPhase.PRODUCTION;
        assertEquals(expected, turn.getPhase());
    }
}
