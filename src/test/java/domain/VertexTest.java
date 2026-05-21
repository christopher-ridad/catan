package domain;

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
}
