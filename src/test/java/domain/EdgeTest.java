package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EdgeTest {

    @Test
    void Constructor_WithNegativeId_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = EasyMock.createMock(Vertex.class);
        Vertex endpoint2 = EasyMock.createMock(Vertex.class);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(-1, endpoint1, endpoint2));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }
}
