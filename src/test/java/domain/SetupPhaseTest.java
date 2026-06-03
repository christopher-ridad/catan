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

        hexes.add(new Hex(TerrainType.FIELDS, 5));
        hexes.add(new Hex(TerrainType.FIELDS, 10));
        hexes.add(new Hex(TerrainType.FIELDS, 4));
        hexes.add(new Hex(TerrainType.FIELDS, 8));

        hexes.add(new Hex(TerrainType.PASTURE, 9));
        hexes.add(new Hex(TerrainType.PASTURE, 3));
        hexes.add(new Hex(TerrainType.PASTURE, 11));
        hexes.add(new Hex(TerrainType.PASTURE, 6));

        hexes.add(new Hex(TerrainType.FOREST, 2));
        hexes.add(new Hex(TerrainType.FOREST, 12));
        hexes.add(new Hex(TerrainType.FOREST, 5));
        hexes.add(new Hex(TerrainType.FOREST, 9));

        hexes.add(new Hex(TerrainType.MOUNTAINS, 4));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 8));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 10));

        hexes.add(new Hex(TerrainType.HILLS, 6));
        hexes.add(new Hex(TerrainType.HILLS, 3));
        hexes.add(new Hex(TerrainType.HILLS, 11));

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
    public void constructor_validGame_2Players_buildsCorrectPlacementOrder() {
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

    @Test
    void placeSettlement_nullPlayer_throwsIllegalArgument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> phase4.placeSettlement(null, 0)
        );
    }

    @Test
    void placeSettlement_wrongPlayer_throwsIllegalState() {
        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeSettlement(p2, 0)
        );
    }

    @Test
    void placeSettlement_invalidVertexId_throwsIllegalArgument() {
        assertThrows(
                IllegalArgumentException.class,
                () -> phase4.placeSettlement(p1, 999)
        );
    }

    @Test
    void placeSettlement_occupiedVertex_throwsIllegalState() {
        Vertex v0 = board.getVertex(0);
        Vertex v1 = board.getVertex(1);

        Edge roadEdge = null;
        for (Edge edge : board.getEdges()) {
            if (edge.connectsTo(v0) && !edge.hasRoad()) {
                roadEdge = edge;
                break;
            }
        }
        assertNotNull(roadEdge);

        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, roadEdge.getId());

        assertEquals(p2, phase4.getCurrentPlayer());
        assertTrue(v0.isOccupied());

        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeSettlement(p2, 0)
        );
    }

    @Test
    void placeSettlement_vertexAdjacentToExistingSettlement_throwsIllegalState() {
        Vertex v0 = board.getVertex(0);

        Edge roadEdge = null;
        for (Edge edge : board.getEdges()) {
            if (edge.connectsTo(v0) && !edge.hasRoad()) {
                roadEdge = edge;
                break;
            }
        }
        assertNotNull(roadEdge);

        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, roadEdge.getId());

        assertEquals(p2, phase4.getCurrentPlayer());

        assertTrue(board.getVertex(0).getAdjacentVertices().contains(board.getVertex(3)));

        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeSettlement(p2, 3)
        );
    }

    @Test
    void placeSettlement_validRound1_placesSuccessfully() {
        phase4.placeSettlement(p1, 0);

        assertTrue(board.getVertex(0).isOccupied());
        assertEquals(p1, board.getVertex(0).getOwner().get());
        assertEquals(1, phase4.getCurrentRound());
        assertEquals(p1, phase4.getCurrentPlayer());
    }

    @Test
    void placeSettlement_validRound2_placesSuccessfully() {
        completeRound1();

        assertEquals(2, phase4.getCurrentRound());
        assertEquals(p4, phase4.getCurrentPlayer());

        phase4.placeSettlement(p4, 38);

        assertTrue(board.getVertex(38).isOccupied());
        assertEquals(p4, board.getVertex(38).getOwner().get());
    }

    @Test
    void placeRoad_nullPlayer_throwsIllegalArgument() {
        phase4.placeSettlement(p1, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> phase4.placeRoad(null, 0)
        );
    }

    @Test
    void placeRoad_wrongPlayer_throwsIllegalState() {
        phase4.placeSettlement(p1, 0);

        Edge roadEdge = findAdjacentEdge(board.getVertex(0));

        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeRoad(p2, roadEdge.getId())
        );
    }

    @Test
    void placeRoad_beforeSettlementPlaced_throwsIllegalState() {
        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeRoad(p1, 0)
        );
    }

    @Test
    void placeRoad_invalidEdgeId_throwsIllegalArgument() {
        phase4.placeSettlement(p1, 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> phase4.placeRoad(p1, 999)
        );
    }

    @Test
    void placeRoad_occupiedEdge_throwsIllegalState() {
        phase4.placeSettlement(p1, 0);
        Edge roadEdge = findAdjacentEdge(board.getVertex(0));
        phase4.placeRoad(p1, roadEdge.getId());

        phase4.placeSettlement(p2, 10);

        int occupiedEdgeId = roadEdge.getId();
        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeRoad(p2, occupiedEdgeId)
        );
    }

    @Test
    void placeRoad_notAdjacentToCurrentSettlement_throwsIllegalState() {
        phase4.placeSettlement(p1, 0);

        Edge nonAdjacentEdge = findNonAdjacentEdge(board.getVertex(0));

        assertThrows(
                IllegalStateException.class,
                () -> phase4.placeRoad(p1, nonAdjacentEdge.getId())
        );
    }

    @Test
    void placeRoad_round1LastPlayer_transitionsToRound2() {
        completeRound1();

        assertEquals(p4, phase4.getCurrentPlayer());
        assertEquals(2, phase4.getCurrentRound());
    }

    @Test
    void placeRoad_round2_placesAndAdvancesCounterClockwise() {
        completeRound1();

        phase4.placeSettlement(p4, 38);
        phase4.placeRoad(p4, findAdjacentEdge(board.getVertex(38)).getId());

        assertEquals(p3, phase4.getCurrentPlayer());
        assertEquals(2, phase4.getCurrentRound());
    }

    @Test
    void placeRoad_round2LastPlayer_completesPhase() {
        completeRound1();
        completeRound2Except(null);

        assertTrue(phase4.isComplete());
    }

    @Test
    void placeRoad_validRound1_placesAndAdvancesClockwise() {
        phase4.placeSettlement(p1, 0);
        Edge roadEdge = findAdjacentEdge(board.getVertex(0));

        phase4.placeRoad(p1, roadEdge.getId());

        assertTrue(roadEdge.hasRoad());
        assertEquals(p2, phase4.getCurrentPlayer());
        assertEquals(1, phase4.getCurrentRound());
    }

    @Test
    void getCurrentRound_placementIndices0ToN_returnsRound1() {
        assertEquals(1, phase4.getCurrentRound());

        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, findAdjacentEdge(board.getVertex(0)).getId());

        assertEquals(1, phase4.getCurrentRound());
    }

    @Test
    void getCurrentRound_placementIndicesNTo2N_returnsRound2() {
        completeRound1();

        assertEquals(2, phase4.getCurrentRound());
    }

    @Test
    void getCurrentPlayer_atStart_returnsFirstPlayerOfRound1() {
        assertEquals(p1, phase4.getCurrentPlayer());
    }

    @Test
    void getCurrentPlayer_midRound1_returnsCorrectClockwisePlayer() {
        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, findAdjacentEdge(board.getVertex(0)).getId());

        assertEquals(p2, phase4.getCurrentPlayer());

        phase4.placeSettlement(p2, 10);
        phase4.placeRoad(p2, findAdjacentEdge(board.getVertex(10)).getId());

        assertEquals(p3, phase4.getCurrentPlayer());
    }

    @Test
    void getCurrentPlayer_lastOfRound1_returnsLastPlayer() {
        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, findAdjacentEdge(board.getVertex(0)).getId());

        phase4.placeSettlement(p2, 10);
        phase4.placeRoad(p2, findAdjacentEdge(board.getVertex(10)).getId());

        phase4.placeSettlement(p3, 20);
        phase4.placeRoad(p3, findAdjacentEdge(board.getVertex(20)).getId());

        assertEquals(p4, phase4.getCurrentPlayer());
    }

    private Edge findAdjacentEdge(Vertex vertex) {
        for (Edge edge : board.getEdges()) {
            if (edge.connectsTo(vertex) && !edge.hasRoad()) {
                return edge;
            }
        }
        throw new IllegalStateException("No available adjacent edge found for vertex " + vertex.getId());
    }

    private Edge findNonAdjacentEdge(Vertex vertex) {
        for (Edge edge : board.getEdges()) {
            if (!edge.connectsTo(vertex) && !edge.hasRoad()) {
                return edge;
            }
        }
        throw new IllegalStateException("No non-adjacent edge found");
    }

    private void completeRound1() {
        phase4.placeSettlement(p1, 0);
        phase4.placeRoad(p1, findAdjacentEdge(board.getVertex(0)).getId());

        phase4.placeSettlement(p2, 10);
        phase4.placeRoad(p2, findAdjacentEdge(board.getVertex(10)).getId());

        phase4.placeSettlement(p3, 20);
        phase4.placeRoad(p3, findAdjacentEdge(board.getVertex(20)).getId());

        phase4.placeSettlement(p4, 30);
        phase4.placeRoad(p4, findAdjacentEdge(board.getVertex(30)).getId());
    }

    private void completeRound2Except(Player skip) {
        if (p4 != skip) {
            phase4.placeSettlement(p4, 38);
            phase4.placeRoad(p4, findAdjacentEdge(board.getVertex(38)).getId());
        }
        if (p3 != skip) {
            phase4.placeSettlement(p3, 42);
            phase4.placeRoad(p3, findAdjacentEdge(board.getVertex(42)).getId());
        }
        if (p2 != skip) {
            phase4.placeSettlement(p2, 32);
            phase4.placeRoad(p2, findAdjacentEdge(board.getVertex(32)).getId());
        }
        if (p1 != skip) {
            phase4.placeSettlement(p1, 11);
            phase4.placeRoad(p1, findAdjacentEdge(board.getVertex(11)).getId());
        }
    }
}
