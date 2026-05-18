package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {
    private List<Hex> validHexList() {
        List<Hex> hexes = new ArrayList<>();
        hexes.add(new Hex(TerrainType.DESERT));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FIELDS));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.PASTURE));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FOREST));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.MOUNTAINS));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.HILLS));
        return hexes;
    }

    @Test
    void Constructor_With18Hexes_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.remove(0);
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }

    @Test
    void Constructor_With19Hexes_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(validHexList()));
    }

    @Test
    void Constructor_With20Hexes_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.add(new Hex(TerrainType.HILLS));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }

    @Test
    void Constructor_WithNoDesert_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.remove(0);
        hexes.add(new Hex(TerrainType.HILLS));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }

    @Test
    void Constructor_WithOneDesert_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(validHexList()));
    }

    @Test
    void Constructor_WithTwoDeserts_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.remove(hexes.size() - 1);
        hexes.add(new Hex(TerrainType.DESERT));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }

    @Test
    void Constructor_With3Fields_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.remove(1);
        hexes.add(new Hex(TerrainType.HILLS));
        System.out.println("Size: " + hexes.size());

        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }

    @Test
    void Constructor_With4Fields_NoExceptionThrown() {
        assertDoesNotThrow(() -> new Board(validHexList()));
    }

    @Test
    void Constructor_With5Fields_ThrowsIllegalArgumentException() {
        List<Hex> hexes = validHexList();
        hexes.remove(hexes.size() - 1);
        hexes.add(new Hex(TerrainType.FIELDS));
        assertThrows(IllegalArgumentException.class, () -> new Board(hexes));
    }
}