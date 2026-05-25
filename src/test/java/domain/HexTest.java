package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HexTest {
    @Test
    void hexConstructor_tokenBelowMin_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(TerrainType.FOREST, 1));
    }

    @Test
    void hexConstructor_tokenAtMin_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 2));
    }

    @Test
    void hexConstructor_tokenAtTopOfLowerRange_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 6));
    }

    @Test
    void hexConstructor_tokenAtGap_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(TerrainType.FOREST, 7));
    }

    @Test
    void hexConstructor_tokenAtBottomOfUpperRange_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 8));
    }

    @Test
    void hexConstructor_tokenAtMax_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.FOREST, 12));
    }

    @Test
    void hexConstructor_tokenAboveMax_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(TerrainType.FOREST, 13));
    }

    @Test
    void hexConstructor_desertWithNoToken_doesNotThrow() {
        assertDoesNotThrow(() -> new Hex(TerrainType.DESERT, 0));
    }

    @Test
    void hexConstructor_desertWithToken_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(TerrainType.DESERT, 8));
    }

    @Test
    void hexConstructor_nonDesertWithNoToken_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(TerrainType.FOREST, 0));
    }

    @Test
    void getNumberToken_onNonDesertHex_returnsToken() {
        Hex hex = new Hex(TerrainType.FOREST, 6);
        assertEquals(6, hex.getNumberToken());
    }
}
