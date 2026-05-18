package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private List<Hex> hexes;
    private Board board;

    @BeforeEach
    void setUp() {
        hexes = new ArrayList<>();
        hexes.add(new Hex(TerrainType.DESERT));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FIELDS));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.PASTURE));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FOREST));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.MOUNTAINS));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.HILLS));
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
        hexes.add(new Hex(TerrainType.HILLS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 19 hexes", exception.getMessage());
    }

    @Test
    void Constructor_WithNoDesert_ThrowsIllegalArgumentException() {
        int desertHexToRemoveIndex = 0;
        hexes.remove(desertHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

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
        hexes.add(new Hex(TerrainType.DESERT));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 1 DESERT hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Fields_ThrowsIllegalArgumentException() {
        int fieldsHexToRemoveIndex = 1;
        hexes.remove(fieldsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

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
        hexes.add(new Hex(TerrainType.FIELDS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FIELDS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Pasture_ThrowsIllegalArgumentException() {
        int pastureHexToRemoveIndex = 5;
        hexes.remove(pastureHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));
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
        hexes.add(new Hex(TerrainType.PASTURE));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 PASTURE hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Forest_ThrowsIllegalArgumentException() {
        int forestHexToRemoveIndex = 9;
        hexes.remove(forestHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

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
        hexes.add(new Hex(TerrainType.FOREST));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FOREST hexes", exception.getMessage());
    }

    @Test
    void Constructor_With2Mountains_ThrowsIllegalArgumentException() {
        int mountainsHexToRemoveIndex = 13;
        hexes.remove(mountainsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

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
        hexes.add(new Hex(TerrainType.MOUNTAINS));

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
        Vertex neighbor1 = EasyMock.createMock(Vertex.class);
        EasyMock.expect(neighbor1.isOccupied()).andReturn(false);
        EasyMock.replay(neighbor1);

        Vertex vertex = new Vertex(0, new ArrayList<>(), List.of(neighbor1));
        assertTrue(board.satisfiesDistanceRule(vertex));

        EasyMock.verify(neighbor1);
    }

    @Test
    void SatisfiesDistanceRule_WhenOneNeighborOccupied_ReturnsFalse() {
        Vertex neighbor1 = EasyMock.createMock(Vertex.class);
        EasyMock.expect(neighbor1.isOccupied()).andReturn(true);
        EasyMock.replay(neighbor1);

        Vertex vertex = new Vertex(0, new ArrayList<>(), List.of(neighbor1));
        assertFalse(board.satisfiesDistanceRule(vertex));

        EasyMock.verify(neighbor1);
    }

    @Test
    void IsConnectedToPlayer_WhenNoAdjacentRoads_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);

        Edge mockEdge = EasyMock.createMock(Edge.class);
        EasyMock.expect(mockEdge.connectsTo(vertex)).andReturn(true);
        EasyMock.expect(mockEdge.hasRoad()).andReturn(false);
        EasyMock.replay(mockEdge);

        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(mockEdge));

        assertFalse(testBoard.isConnectedToPlayer(vertex, player));
        EasyMock.verify(mockEdge);
    }

    @Test
    void IsConnectedToPlayer_WhenAdjacentRoadOwnedByPlayer_ReturnsTrue() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);

        Edge mockEdge = EasyMock.createMock(Edge.class);
        EasyMock.expect(mockEdge.connectsTo(vertex)).andReturn(true);
        EasyMock.expect(mockEdge.hasRoad()).andReturn(true);
        EasyMock.expect(mockEdge.getOwner()).andReturn(player);
        EasyMock.replay(mockEdge);

        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(mockEdge));

        assertTrue(testBoard.isConnectedToPlayer(vertex, player));
        EasyMock.verify(mockEdge);
    }

    @Test
    void IsConnectedToPlayer_WhenAdjacentRoadOwnedByDifferentPlayer_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        Player otherPlayer = EasyMock.createMock(Player.class);

        Edge mockEdge = EasyMock.createMock(Edge.class);
        EasyMock.expect(mockEdge.connectsTo(vertex)).andReturn(true);
        EasyMock.expect(mockEdge.hasRoad()).andReturn(true);
        EasyMock.expect(mockEdge.getOwner()).andReturn(otherPlayer);
        EasyMock.replay(mockEdge);

        Board testBoard = new Board(hexes, new ArrayList<>(), List.of(mockEdge));

        assertFalse(testBoard.isConnectedToPlayer(vertex, player));
        EasyMock.verify(mockEdge);
    }
}