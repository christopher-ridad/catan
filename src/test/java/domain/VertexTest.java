package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VertexTest {
    @Test
    void Constructor_WithNegativeId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Vertex(-1, new ArrayList<>(), new ArrayList<>()));
        assertEquals("Vertex id must be between 0 and 53", exception.getMessage());
    }

    @Test
    void Constructor_WithLowerBoundaryId_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Vertex(0, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void Constructor_WithUpperBoundaryId_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Vertex(53, new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Vertex(54, new ArrayList<>(), new ArrayList<>()));
        assertEquals("Vertex id must be between 0 and 53", exception.getMessage());
    }

    @Test
    void Constructor_WithNullAdjacentHexes_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Vertex(0, null, new ArrayList<>()));
        assertEquals("Adjacent hexes cannot be null", exception.getMessage());
    }

    @Test
    void Constructor_WithNullAdjacentVertices_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Vertex(0, new ArrayList<>(), null));
        assertEquals("Adjacent vertices cannot be null", exception.getMessage());
    }

    @Test
    void GetId_ReturnsCorrectId() {
        Vertex vertex = new Vertex(5, new ArrayList<>(), new ArrayList<>());
        assertEquals(5, vertex.getId());
    }

    @Test
    void IsOccupied_WhenNoOwner_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        assertFalse(vertex.isOccupied());
    }

    @Test
    void IsOccupied_WhenOwnerSet_ReturnsTrue() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        vertex.setOwner(player);
        assertTrue(vertex.isOccupied());
    }

    @Test
    void GetOwner_WhenNoOwner_ReturnsEmpty() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        assertTrue(vertex.getOwner().isEmpty());
    }

    @Test
    void GetOwner_WhenOwnerSet_ReturnsOwner() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        vertex.setOwner(player);
        Player owner = vertex.getOwner().orElseThrow();
        assertEquals(player, owner);
    }

    @Test
    void GetAdjacentHexes_ReturnsAdjacentHexes() {
        Hex hex = EasyMock.createMock(Hex.class);
        List<Hex> adjacentHexes = List.of(hex);
        Vertex vertex = new Vertex(0, adjacentHexes, new ArrayList<>());
        assertEquals(adjacentHexes, vertex.getAdjacentHexes());
    }

    @Test
    void GetAdjacentVertices_ReturnsAdjacentVertices() {
        Vertex neighbor = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        List<Vertex> adjacentVertices = List.of(neighbor);
        Vertex vertex = new Vertex(0, new ArrayList<>(), adjacentVertices);
        assertEquals(adjacentVertices, vertex.getAdjacentVertices());
    }

    @Test
    void IsCity_WhenNoOwner_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        assertFalse(vertex.isCity());
    }

    @Test
    void IsCity_WhenOwnerSetButNotUpgraded_ReturnsFalse() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        vertex.setOwner(player);
        assertFalse(vertex.isCity());
    }

    @Test
    void IsCity_AfterUpgradeToCity_ReturnsTrue() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player player = EasyMock.createMock(Player.class);
        vertex.setOwner(player);
        vertex.upgradeToCity();
        assertTrue(vertex.isCity());
    }

    @Test
    void UpgradeToCity_WhenVertexIsUnoccupied_ThrowsIllegalStateException() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalStateException.class, vertex::upgradeToCity);
    }

    @Test
    void getHarborType_WhenNoHarborSet_ReturnsEmpty() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        assertTrue(vertex.getHarborType().isEmpty());
    }

    @Test
    void getHarborType_WhenHarborSet_ReturnsHarborType() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        vertex.setHarborType(HarborType.GENERIC);
        assertEquals(HarborType.GENERIC, vertex.getHarborType().orElseThrow());
    }

    @Test
    void setHarborType_OverwritesPreviousHarbor() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        vertex.setHarborType(HarborType.GENERIC);
        vertex.setHarborType(HarborType.WOOL);
        assertEquals(HarborType.WOOL, vertex.getHarborType().orElseThrow());
    }
}
