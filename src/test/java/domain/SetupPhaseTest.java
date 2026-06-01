package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SetupPhaseTest {
    private Board board;
    private List<Player> players2;
    private List<Player> players4;
    private Player p1, p2, p3, p4;
    private Game game2Players;
    private Game game4Players;
    private SetupPhase phase2;
    private SetupPhase phase4;

    private Board createStandardBoard() {
        List<Hex> hexes = new ArrayList<>();

        // FIELDS (4)
        hexes.add(new Hex(TerrainType.FIELDS, 5));
        hexes.add(new Hex(TerrainType.FIELDS, 10));
        hexes.add(new Hex(TerrainType.FIELDS, 4));
        hexes.add(new Hex(TerrainType.FIELDS, 8));

        // PASTURE (4)
        hexes.add(new Hex(TerrainType.PASTURE, 9));
        hexes.add(new Hex(TerrainType.PASTURE, 3));
        hexes.add(new Hex(TerrainType.PASTURE, 11));
        hexes.add(new Hex(TerrainType.PASTURE, 6));

        // FOREST (4)
        hexes.add(new Hex(TerrainType.FOREST, 2));
        hexes.add(new Hex(TerrainType.FOREST, 12));
        hexes.add(new Hex(TerrainType.FOREST, 5));
        hexes.add(new Hex(TerrainType.FOREST, 9));

        // MOUNTAINS (3)
        hexes.add(new Hex(TerrainType.MOUNTAINS, 4));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 8));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 10));

        // HILLS (3)
        hexes.add(new Hex(TerrainType.HILLS, 6));
        hexes.add(new Hex(TerrainType.HILLS, 3));
        hexes.add(new Hex(TerrainType.HILLS, 11));

        // DESERT (1)
        hexes.add(new Hex(TerrainType.DESERT, 0));

        return new Board(hexes);
    }

    @BeforeEach
    void setUp() {
        board = createStandardBoard();

        p1 = new Player("Alice", PlayerColor.RED);
        p2 = new Player("Bob", PlayerColor.BLUE);
        p3 = new Player("Charlie", PlayerColor.WHITE);
        p4 = new Player("Diana", PlayerColor.ORANGE);

        players2 = Arrays.asList(p1, p2);
        players4 = Arrays.asList(p1, p2, p3, p4);

        game2Players = new Game(players2, board);
        game4Players = new Game(players4, board);

        phase2 = new SetupPhase(game2Players);
        phase4 = new SetupPhase(game4Players);
    }

    @Test
    public void constructor_nullGame_throwsIllegalArgument() {
        Game game = null;

        assertThrows(IllegalArgumentException.class, () -> new SetupPhase(game));
    }

    @Test
    public void constructor_validGame_2Players_buildsCorrectPlacementOrder(){
        SetupPhase phase = new SetupPhase(game2Players);

        List<Player> expectedOrder = Arrays.asList(p1, p2, p2, p1);
        assertEquals(expectedOrder, phase.getPlacementOrder());
        assertEquals(p1, phase.getCurrentPlayer());
        assertEquals(1, phase.getCurrentRound());
        assertFalse(phase.isComplete());
    }

    @Test
    void constructor_validGame_4Players_buildsCorrectPlacementOrder() {
        List<Player> expectedOrder = Arrays.asList(p1, p2, p3, p4, p4, p3, p2, p1);
        assertEquals(expectedOrder, phase4.getPlacementOrder());
        assertEquals(8, phase4.getPlacementOrder().size());
        assertEquals(p1, phase4.getCurrentPlayer());
        assertEquals(1, phase4.getCurrentRound());
        assertFalse(phase4.isComplete());
    }
}
