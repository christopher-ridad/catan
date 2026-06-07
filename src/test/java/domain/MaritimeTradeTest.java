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
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.FIELDS, 9));
        }
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.PASTURE, 10));
        }
        for (int i = 0; i < 4; i++) {
            hexes.add(new Hex(TerrainType.FOREST, 11));
        }
        for (int i = 0; i < 3; i++) {
            hexes.add(new Hex(TerrainType.MOUNTAINS, 12));
        }
        for (int i = 0; i < 3; i++) {
            hexes.add(new Hex(TerrainType.HILLS, 4));
        }
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

    @Test
    void maritimeTradeConstructor_amountAboveRateGenericHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_playerCannotAffordGiving_specialHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(33, Map.of(
                ResourceType.BRICK, 1,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 2, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_playerHasExactlyRequiredAmount_specialHarbor_objectCreated() {
        Player player = playerWithSettlementAt(33, Map.of(
                ResourceType.BRICK, 2,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.BRICK, 2, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_amountBelowRateSpecialHarbor_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(33, Map.of(
                ResourceType.BRICK, 2,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, ResourceType.BRICK, 1, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_specialHarborDoesNotApplyToOtherResource_usesDefaultRate() {
        Player player = playerWithSettlementAt(33, Map.of(
                ResourceType.BRICK, 0,
                ResourceType.LUMBER, 4,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.LUMBER, 4, ResourceType.BRICK, board));
    }

    @Test
    void maritimeTradeConstructor_genericHarborBeatsDefault_usesGenericRate() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 0,
                ResourceType.LUMBER, 3,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.LUMBER, 3, ResourceType.BRICK, board));
    }

    @Test
    void maritimeTradeConstructor_specialHarborBeatsGenericHarbor_usesSpecialRate() {
        Player player = new Player("Alice", PlayerColor.RED, Map.of(
                ResourceType.BRICK, 2,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        board.getVertex(0).setOwner(player);
        board.getVertex(33).setOwner(player);
        assertDoesNotThrow(() ->
                new MaritimeTrade(player, ResourceType.BRICK, 2, ResourceType.LUMBER, board));
    }

    @Test
    void maritimeTradeConstructor_givingNull_throwsIllegalArgument() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        assertThrows(IllegalArgumentException.class, () ->
                new MaritimeTrade(player, null, 4, ResourceType.LUMBER, board));
    }

    @Test
    void getPlayer_validMaritimeTrade_returnsCorrectPlayer() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board);
        assertEquals(player, trade.getPlayer());
    }

    @Test
    void getGiving_validMaritimeTrade_returnsCorrectResource() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board);
        assertEquals(ResourceType.BRICK, trade.getGiving());
    }

    @Test
    void getAmount_validMaritimeTrade_returnsCorrectAmount() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board);
        assertEquals(4, trade.getAmount());
    }

    @Test
    void getReceiving_validMaritimeTrade_returnsCorrectResource() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board);
        assertEquals(ResourceType.LUMBER, trade.getReceiving());
    }

    @Test
    void getRate_noHarbor_returnsFour() {
        Player player = playerWithSettlementAt(4, Map.of(
                ResourceType.BRICK, 4,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 4, ResourceType.LUMBER, board);
        assertEquals(4, trade.getRate());
    }

    @Test
    void getRate_genericHarbor_returnsThree() {
        Player player = playerWithSettlementAt(0, Map.of(
                ResourceType.BRICK, 3,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 3, ResourceType.LUMBER, board);
        assertEquals(3, trade.getRate());
    }

    @Test
    void getRate_specialHarbor_returnsTwo() {
        Player player = playerWithSettlementAt(33, Map.of(
                ResourceType.BRICK, 2,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
        MaritimeTrade trade = new MaritimeTrade(player, ResourceType.BRICK, 2, ResourceType.LUMBER, board);
        assertEquals(2, trade.getRate());
    }


    private Player playerWithSettlementAt(int vertexId, Map<ResourceType, Integer> resources) {
        Player player = new Player("Alice", PlayerColor.RED, resources);
        board.getVertex(vertexId).setOwner(player);
        return player;
    }
}
