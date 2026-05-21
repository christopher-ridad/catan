package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    void GetOwner_WhenNoOwner_ReturnsNull() {
        Vertex vertex = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Player owner = vertex.getOwner();
        assertNull(owner);
    }
}
