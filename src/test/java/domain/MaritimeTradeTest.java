package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MaritimeTradeTest {

    private Board board;

    @BeforeEach
    void setUp() {
        ArrayList<Hex> hexes = new ArrayList<>();
        hexes.add(new Hex(TerrainType.DESERT, 0));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FIELDS, 9));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.PASTURE, 10));
        for (int i = 0; i < 4; i++) hexes.add(new Hex(TerrainType.FOREST, 11));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.MOUNTAINS, 12));
        for (int i = 0; i < 3; i++) hexes.add(new Hex(TerrainType.HILLS, 4));
        board = new Board(hexes);
    }

    @Test
    void maritimeTradeConstructor_givingEqualsReceiving_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.BRICK, board));
    }

    @Test
    void maritimeTradeConstructor_playerCannotAffordGiving_noHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 3,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_playerHasExactlyRequiredAmount_noHarbor_objectCreated() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_amountBelowRateNoHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 3, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_amountAboveRateNoHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 5,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 5, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_playerCannotAffordGiving_genericHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 2,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 3, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_playerHasExactlyRequiredAmount_genericHarbor_objectCreated() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 3,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.BRICK, 3, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_amountBelowRateGenericHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 3,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 2, ResourceType.LUMBER, board));
    }

    private Player playerWithSettlementAt(int vertexId, Map<ResourceType, Integer> resources) {
        Player player = new Player("Alice", PlayerColor.RED, resources);
        board.getVertex(vertexId).setOwner(player);
        return player;
    }
}
