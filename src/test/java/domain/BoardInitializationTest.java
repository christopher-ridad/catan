package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardInitializationTest {

    @Test
    void getAdjacentVertexIds_negativeVertexId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BoardInitialization.getAdjacentVertexIds(-1));
    }

    @Test
    void getAdjacentVertexIds_vertexIdTooLarge_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BoardInitialization.getAdjacentVertexIds(54));
    }

    @Test
    void getAdjacentHexIndices_negativeVertexId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BoardInitialization.getAdjacentHexIndices(-1));
    }

    @Test
    void getAdjacentHexIndices_vertexIdTooLarge_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> BoardInitialization.getAdjacentHexIndices(54));
    }
}
