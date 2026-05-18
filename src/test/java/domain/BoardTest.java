package domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private List<Hex> hexes;

    @BeforeEach
    void setUp() {
        hexes = new ArrayList<>();
        hexes.add(new Hex(TerrainType.DESERT));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FIELDS));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.PASTURE));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FOREST));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.MOUNTAINS));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.HILLS));
    }

    @Test
    void Constructor_With18Hexes_ThrowsIllegalArgumentException() {
        int desertHexToRemoveIndex = 0;
        hexes.remove(desertHexToRemoveIndex);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 19 hexes", exception.getMessage());
    }

    @Test
    void Constructor_With19Hexes_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With20Hexes_ThrowsIllegalArgumentException() {
        hexes.add(new Hex(TerrainType.HILLS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 19 hexes", exception.getMessage());
    }

    @Test
    void Constructor_WithNoDesert_ThrowsIllegalArgumentException() {
        int desertHexToRemoveIndex = 0;
        hexes.remove(desertHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 1 DESERT hex", exception.getMessage());
    }

    @Test
    void Constructor_WithOneDesert_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_WithTwoDeserts_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.DESERT));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 1 DESERT hex", exception.getMessage());
    }

    @Test
    void Constructor_With3Fields_ThrowsIllegalArgumentException() {
        int fieldsHexToRemoveIndex = 1;
        hexes.remove(fieldsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FIELDS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With4Fields_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With5Fields_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.FIELDS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 FIELDS hexes", exception.getMessage());
    }

    @Test
    void Constructor_With3Pasture_ThrowsIllegalArgumentException() {
        int pastureHexToRemoveIndex = 5;
        hexes.remove(pastureHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.HILLS));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 PASTURE hexes", exception.getMessage());
    }

    @Test
    void Constructor_With4Pasture_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(hexes));
    }

    @Test
    void Constructor_With5Pasture_ThrowsIllegalArgumentException() {
        int hillsHexToRemoveIndex = hexes.size() - 1;
        hexes.remove(hillsHexToRemoveIndex);
        hexes.add(new Hex(TerrainType.PASTURE));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
        assertEquals("Board must have exactly 4 PASTURE hexes", exception.getMessage());
    }
}