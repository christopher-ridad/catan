package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class HexTest {
    @Test
    void hexConstructor_tokenBelowMin_throwsIllegalArgument() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 1));
    }

    @Test
    void hexConstructor_tokenAtMin_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 2));
    }

    @Test
    void hexConstructor_tokenAtTopOfLowerRange_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 6));
    }
}
