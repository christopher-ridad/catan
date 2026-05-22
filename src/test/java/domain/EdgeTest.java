package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {

    @Test
    void Constructor_WithNegativeId_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(-1, endpoint1, endpoint2));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void Constructor_WithLowerBoundaryId_NoExceptionThrown() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        assertDoesNotThrow(() -> new Edge(0, endpoint1, endpoint2));
    }

    @Test
    void Constructor_WithUpperBoundaryId_NoExceptionThrown() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        assertDoesNotThrow(() -> new Edge(71, endpoint1, endpoint2));
    }

    @Test
    void Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(72, endpoint1, endpoint2));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void Constructor_WithNullEndpoint1_ThrowsIllegalArgumentException() {
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(0, null, endpoint2));
        assertEquals("Endpoints cannot be null", exception.getMessage());
    }

    @Test
    void Constructor_WithNullEndpoint2_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(0, endpoint1, null));
        assertEquals("Endpoints cannot be null", exception.getMessage());
    }

    @Test
    void GetId_ReturnsCorrectId() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        Edge edge = new Edge(5, endpoint1, endpoint2);
        assertEquals(5, edge.getId());
    }

    @Test
    void HasRoad_WhenNoRoad_ReturnsFalse() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertFalse(edge.hasRoad());
    }

    @Test
    void HasRoad_WhenRoadPlaced_ReturnsTrue() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        Edge edge = new Edge(0, endpoint1, endpoint2);
        Player player = EasyMock.createMock(Player.class);
        edge.setOwner(player);
        assertTrue(edge.hasRoad());
    }

    @Test
    void GetOwner_WhenNoRoad_ReturnsNull() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        Edge edge = new Edge(0, endpoint1, endpoint2);
        Player owner = edge.getOwner();
        assertNull(owner);
    }
}
