package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VertexTest {
    @Test
    void Constructor_WithNegativeId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Vertex(-1, new ArrayList<>(), new ArrayList<>()));
        assertEquals("Vertex id must be between 0 and 53", exception.getMessage());
    }
}
