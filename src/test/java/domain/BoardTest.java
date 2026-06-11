package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private List<Hex> hexes;
    private Board board;

    @BeforeEach
    void setUp() {
        hexes = new ArrayList<>();
        hexes.add(new Hex(TerrainType.DESERT, 0));
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.FIELDS, 9));
        }
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.PASTURE, 10));
        }
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.FOREST, 11));
        }
        for (int i = 0; i < 3; i++) {
            hexes.add(new Hex(TerrainType.MOUNTAINS, 12));
        }
        for (int i = 0; i < 3; i++) {
            hexes.add(new Hex(TerrainType.HILLS, 4));
        }
        board = new Board(hexes);
    }

    @Test
    void Constructor_With18Hexes_ThrowsIllegalArgumentException() {
        int desertHexToRemoveIndex = 0;
        hexes.remove(desertHexToRemoveIndex);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 19 hexes", exception.getMessage());
    }

    @Test
    void Constructor_With19Hexes_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With20Hexes_ThrowsIllegalArgumentException() {
        hexes.add(new Hex(TerrainType.HILLS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 19 hexes", exception.getMessage());
    }

    @Test
    void Constructor_WithNoDesert_ThrowsIllegalArgumentException() {
        int desertHexToRemoveIndex = 0;
        hexes.remove(desertHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 1 DESERT hexes", exception.getMessage());
    }

    @Test
    void Constructor_WithOneDesert_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_WithTwoDeserts_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.DESERT, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 1 DESERT hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Fields_ThrowsIllegalArgumentException() {
        int fieldsHexToRemoveIndex = 1;
        hexes.remove(fieldsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FIELDS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With4Fields_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With5Fields_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.FIELDS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FIELDS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Pasture_ThrowsIllegalArgumentException() {
        int pastureHexToRemoveIndex = 5;
        hexes.remove(pastureHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS,6));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 PASTURE hexes", exception.getMessage());
    }

    @Test
    void Constructor_With4Pasture_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With5Pasture_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.PASTURE, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 PASTURE hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Forest_ThrowsIllegalArgumentException() {
        int forestHexToRemoveIndex = 9;
        hexes.remove(forestHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS,5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FOREST hexes", exception.getMessage());
    }

    @Test
    void Constructor_With4Forest_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With5Forest_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.FOREST, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FOREST hexes", exception.getMessage());
    }

    @Test
    void Constructor_With2Mountains_ThrowsIllegalArgumentException() {
        int mountainsHexToRemoveIndex = 13;
        hexes.remove(mountainsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 3 MOUNTAINS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Mountains_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With4Mountains_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.MOUNTAINS, 5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 3 MOUNTAINS hexes", exception.getMessage());
    }

    @Test
    void GetHexes_OnValidBoard_Returns19Hexes() {
        assertEquals(19, board.getHexes().size());
    }

    @Test
    void GetVertices_OnValidBoard_Returns54Vertices() {
        assertEquals(54, board.getVertices().size());
    }

    @Test
    void Constructor_OnValidBoard_PopulatesAdjacentVerticesForEveryVertex() {
        List<Vertex> adjacentVertices = board.getVertex(0).getAdjacentVertices();
        List<Integer> adjacentIds = new ArrayList<>();
        for (Vertex vertex : adjacentVertices) {
            adjacentIds.add(vertex.getId());
        }

        assertAll(
                () -> assertEquals(2, adjacentVertices.size()),
                () -> assertTrue(adjacentIds.contains(3)),
                () -> assertTrue(adjacentIds.contains(4))
        );
    }

    @Test
    void GetEdges_OnValidBoard_Returns72Edges() {
        assertEquals(72, board.getEdges().size());
    }

    @Test
    void GetVertex_WithNegativeId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> board.getVertex(-1));
        assertEquals("Vertex id must be between 0 and 53", exception.getMessage());
    }

    @Test
    void GetVertex_WithLowerBoundaryId_ReturnsVertex() {
        assertEquals(0, board.getVertex(0).getId());
    }

    @Test
    void GetVertex_WithUpperBoundaryId_ReturnsVertex() {
        assertEquals(53, board.getVertex(53).getId());
    }

    @Test
    void GetVertex_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> board.getVertex(54));
        assertEquals("Vertex id must be between 0 and 53", exception.getMessage());
    }

    @Test
    void GetEdge_WithNegativeId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> board.getEdge(-1));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void GetEdge_WithLowerBoundaryId_ReturnsEdge() {
        assertEquals(0, board.getEdge(0).getId());
    }

    @Test
    void GetEdge_WithUpperBoundaryId_ReturnsEdge() {
        assertEquals(71, board.getEdge(71).getId());
    }

    @Test
    void GetEdge_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> board.getEdge(72));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void GetHexCount_ForDesert_ReturnsOne() {
        assertEquals(1, board.getHexCount(TerrainType.DESERT));
    }

    @Test
    void GetHexCount_ForFields_ReturnsFour() {
        assertEquals(4, board.getHexCount(TerrainType.FIELDS));
    }

    @Test
    void GetHexCount_ForPasture_ReturnsFour() {
        assertEquals(4, board.getHexCount(TerrainType.PASTURE));
    }

    @Test
    void GetHexCount_ForForest_ReturnsFour() {
        assertEquals(4, board.getHexCount(TerrainType.FOREST));
    }

    @Test
    void GetHexCount_ForMountains_ReturnsThree() {
        assertEquals(3, board.getHexCount(TerrainType.MOUNTAINS));
    }

    @Test
    void GetHexCount_ForHills_ReturnsThree() {
        assertEquals(3, board.getHexCount(TerrainType.HILLS));
    }

    @Test
    void SatisfiesDistanceRule_WhenAllNeighborsEmpty_ReturnsTrue() {
        Vertex neighbor1 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Vertex vertex = new Vertex(0, new ArrayList<>(), List.of(neighbor1));
        assertTrue(board.satisfiesDistanceRule(vertex));
    }

    @Test
    void SatisfiesDistanceRule_WhenOneNeighborOccupied_ReturnsFalse() {
        Player player = EasyMock.createMock(Player.class);
        Vertex neighbor1 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        neighbor1.setOwner(player);
        Vertex vertex = new Vertex(0, new ArrayList<>(), List.of(neighbor1));
        assertFalse(board.satisfiesDistanceRule(vertex));
    }

    @Test
    void SatisfiesDistanceRule_WhenTargetVertexIsOccupied_ReturnsFalse() {
        Player player = EasyMock.createMock(Player.class);
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        vertex.setOwner(player);
        assertFalse(board.satisfiesDistanceRule(vertex));
    }

    @Test
    void PackagePrivateConstructor_ExternalMutationOfEdgeListDoesNotAffectBoard() {
        Vertex v1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex v2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, v1, v2));
        Board testBoard = new Board(hexes, new ArrayList<>(), edges);
        edges.clear();
        assertEquals(1, testBoard.getEdges().size());
    }

    @Test
    void IsConnectedToPlayer_WhenNoAdjacentRoads_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex other = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        Edge edge = new Edge(0, vertex, other);
        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(edge));
        assertFalse(testBoard.isConnectedToPlayer(vertex, player));
    }

    @Test
    void IsConnectedToPlayer_WhenAdjacentRoadOwnedByPlayer_ReturnsTrue() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex other = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        Edge edge = new Edge(0, vertex, other);
        edge.setOwner(player);
        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(edge));
        assertTrue(testBoard.isConnectedToPlayer(vertex, player));
    }

    @Test
    void IsConnectedToPlayer_WhenAdjacentRoadOwnedByDifferentPlayer_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex other = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        Player otherPlayer = EasyMock.createMock(Player.class);
        Edge edge = new Edge(0, vertex, other);
        edge.setOwner(otherPlayer);
        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(edge));
        assertFalse(testBoard.isConnectedToPlayer(vertex, player));
    }

    @Test
    void IsConnectedToPlayer_WhenRoadOnNonAdjacentEdge_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex other1 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Vertex other2 = new Vertex(2, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        Edge nonAdjacentEdge = new Edge(0, other1, other2);
        nonAdjacentEdge.setOwner(player);
        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(nonAdjacentEdge));
        assertFalse(testBoard.isConnectedToPlayer(vertex, player));
    }

    @Test
    void getHarborType_ForNonHarborVertex_ReturnsEmpty() {
        assertTrue(board.getHarborType(board.getVertex(4)).isEmpty());
    }

    @Test
    void getHarborType_ForGenericHarborVertex_ReturnsGeneric() {
        assertEquals(HarborType.GENERIC, board.getHarborType(board.getVertex(0)).orElseThrow());
    }

    @Test
    void getHarborType_ForGrainHarborVertex_ReturnsGrain() {
        assertEquals(HarborType.GRAIN, board.getHarborType(board.getVertex(1)).orElseThrow());
    }

    @Test
    void getHarborType_ForOreHarborVertex_ReturnsOre() {
        assertEquals(HarborType.ORE, board.getHarborType(board.getVertex(10)).orElseThrow());
    }

    @Test
    void getHarborType_ForWoolHarborVertex_ReturnsWool() {
        assertEquals(HarborType.WOOL, board.getHarborType(board.getVertex(42)).orElseThrow());
    }

    @Test
    void getHarborType_ForBrickHarborVertex_ReturnsBrick() {
        assertEquals(HarborType.BRICK, board.getHarborType(board.getVertex(33)).orElseThrow());
    }

    @Test
    void getHarborType_ForLumberHarborVertex_ReturnsLumber() {
        assertEquals(HarborType.LUMBER, board.getHarborType(board.getVertex(11)).orElseThrow());
    }

    @Test
    void getRobberHex_NewBoard_StartsOnDesertHex() {
        assertEquals(TerrainType.DESERT, board.getRobberHex().getTerrainType());
    }

    @Test
    void setRobberHex_WithNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> board.setRobberHex(null));
    }

    @Test
    void setRobberHex_WithHexNotOnBoard_ThrowsIllegalArgumentException() {
        Hex foreignHex = new Hex(TerrainType.HILLS, 6);

        assertThrows(IllegalArgumentException.class, () -> board.setRobberHex(foreignHex));
    }

    @Test
    void setRobberHex_WithHexOnBoard_UpdatesRobberHex() {
        Hex target = board.getHexes().get(1);

        board.setRobberHex(target);

        assertEquals(target, board.getRobberHex());
    }
}