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
}
