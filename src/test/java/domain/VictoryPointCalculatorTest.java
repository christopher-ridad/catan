package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VictoryPointCalculatorTest {

    private Board board;
    private Player p1, p2;
    private VictoryPointCalculator calc;

    private Board createBoard() {
        List<Hex> hexes = new ArrayList<>();

        hexes.add(new Hex(TerrainType.PASTURE, 2));
        hexes.add(new Hex(TerrainType.PASTURE, 4));
        hexes.add(new Hex(TerrainType.PASTURE, 5));
        hexes.add(new Hex(TerrainType.PASTURE, 11));
        hexes.add(new Hex(TerrainType.FOREST, 3));
        hexes.add(new Hex(TerrainType.FOREST, 8));
        hexes.add(new Hex(TerrainType.FOREST, 9));
        hexes.add(new Hex(TerrainType.FOREST, 11));
        hexes.add(new Hex(TerrainType.DESERT, 0));
        hexes.add(new Hex(TerrainType.FIELDS, 4));
        hexes.add(new Hex(TerrainType.FIELDS, 6));
        hexes.add(new Hex(TerrainType.FIELDS, 9));
        hexes.add(new Hex(TerrainType.FIELDS, 12));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 3));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 8));
        hexes.add(new Hex(TerrainType.MOUNTAINS, 10));
        hexes.add(new Hex(TerrainType.HILLS, 5));
        hexes.add(new Hex(TerrainType.HILLS, 6));
        hexes.add(new Hex(TerrainType.HILLS, 10));

        return new Board(hexes);
    }

    @BeforeEach
    void setUp() {
        this.board = createBoard();
        this.p1 = new Player("first", PlayerColor.RED);
        this.p2 = new Player("second", PlayerColor.BLUE);
        this.calc = new VictoryPointCalculator();
    }

    @Test
    public void GetSettlementVP_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getSettlementVP(null, board);
        });
    }

    @Test
    public void GetSettlementVP_WithNullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getSettlementVP(p1, null);
        });
    }

    @Test
    public void GetSettlementVP_EmptyBoard_ReturnsZero() {
        int vp = calc.getSettlementVP(p1, board);
        assertEquals(0, vp);
    }

    @Test
    public void GetSettlementVP_PlayerHasZeroSettlements_ReturnsZero() {
        board.getVertex(0).setOwner(p2);
        board.getVertex(2).setOwner(p2);

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(0, vp);
    }

    @Test
    public void GetSettlementVP_PlayerHasOneSettlement_ReturnsOne() {
        board.getVertex(0).setOwner(p1);

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(1, vp);
    }

    @Test
    public void GetSettlementVP_PlayerHasMultipleSettlements_ReturnsSum() {
        board.getVertex(0).setOwner(p1);
        board.getVertex(11).setOwner(p1);
        board.getVertex(21).setOwner(p1);

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(3, vp);
    }

    @Test
    public void GetSettlementVP_PlayerHasMaximumSettlements_ReturnsMax() {
        int[] safeVertices = {0, 2, 11, 15, 21};
        for (int id : safeVertices) {
            board.getVertex(id).setOwner(p1);
        }

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(5, vp);
    }

    @Test
    public void GetSettlementVP_PlayerHasCities_CitiesAreNotCounted() {
        // Settlements
        board.getVertex(0).setOwner(p1);
        board.getVertex(2).setOwner(p1);

        // Cities
        board.getVertex(11).setOwner(p1);
        board.getVertex(11).upgradeToCity();
        board.getVertex(15).setOwner(p1);
        board.getVertex(15).upgradeToCity();

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(2, vp);
    }

    @Test
    public void GetSettlementVP_MixedOwnership_CountsOnlyTargetPlayer() {
        board.getVertex(0).setOwner(p1);
        board.getVertex(2).setOwner(p1);

        board.getVertex(11).setOwner(p2);
        board.getVertex(15).setOwner(p2);
        board.getVertex(21).setOwner(p2);

        int vp = calc.getSettlementVP(p1, board);
        assertEquals(2, vp);
    }

    @Test
    public void GetCityVP_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getCityVP(null, board);
        });
    }

    @Test
    public void GetCityVP_WithNullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getCityVP(p1, null);
        });
    }

    @Test
    public void GetCityVP_EmptyBoard_ReturnsZero() {
        int vp = calc.getCityVP(p1, board);
        assertEquals(0, vp);
    }

    @Test
    public void GetCityVP_PlayerHasZeroCities_ReturnsZero() {
        board.getVertex(0).setOwner(p2);
        board.getVertex(0).upgradeToCity();

        int vp = calc.getCityVP(p1, board);
        assertEquals(0, vp);
    }

    @Test
    public void GetCityVP_PlayerHasOneCity_ReturnsTwo() {
        board.getVertex(0).setOwner(p1);
        board.getVertex(0).upgradeToCity();

        int vp = calc.getCityVP(p1, board);
        assertEquals(2, vp);
    }

    @Test
    public void GetCityVP_PlayerHasMultipleCities_ReturnsSum() {
        board.getVertex(0).setOwner(p1);
        board.getVertex(0).upgradeToCity();

        board.getVertex(11).setOwner(p1);
        board.getVertex(11).upgradeToCity();

        int vp = calc.getCityVP(p1, board);
        assertEquals(4, vp);
    }

    @Test
    public void GetCityVP_PlayerHasMaximumCities_ReturnsMax() {
        int[] safeVertices = {0, 2, 11, 15};
        for (int id : safeVertices) {
            board.getVertex(id).setOwner(p1);
            board.getVertex(id).upgradeToCity();
        }

        int vp = calc.getCityVP(p1, board);
        assertEquals(8, vp);
    }

    @Test
    public void GetCityVP_PlayerHasSettlements_SettlementsAreNotCounted() {
        // Cities
        board.getVertex(0).setOwner(p1);
        board.getVertex(0).upgradeToCity();
        board.getVertex(2).setOwner(p1);
        board.getVertex(2).upgradeToCity();

        // Settlements
        board.getVertex(11).setOwner(p1);
        board.getVertex(15).setOwner(p1);
        board.getVertex(21).setOwner(p1);

        int vp = calc.getCityVP(p1, board);
        assertEquals(4, vp);
    }

    @Test
    public void GetCityVP_MixedOwnership_CountsOnlyTargetPlayer() {
        board.getVertex(0).setOwner(p1);
        board.getVertex(0).upgradeToCity();
        board.getVertex(2).setOwner(p1);
        board.getVertex(2).upgradeToCity();

        board.getVertex(11).setOwner(p2);
        board.getVertex(11).upgradeToCity();

        int vp = calc.getCityVP(p1, board);
        assertEquals(4, vp);
    }
}