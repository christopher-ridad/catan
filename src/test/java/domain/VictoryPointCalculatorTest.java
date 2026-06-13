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
    private Game game;
    private SpecialCardTracker tracker;

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

    private void buildRoadsPath1(Player player, int count) {
        int[] path = {0, 6, 11, 19, 25, 34, 40, 49, 54, 62, 66};
        for (int i = 0; i < count; i++) {
            board.getEdge(path[i]).setOwner(player);
        }
    }

    private void buildRoadsPath2(Player player, int count) {
        int[] path = {1, 2, 3, 4, 5, 9, 17};
        for (int i = 0; i < count; i++) {
            board.getEdge(path[i]).setOwner(player);
        }
    }

    private void playKnights(Player player, int count) {
        for (int i = 0; i < count; i++) {
            DevelopmentCard knight = new DevelopmentCard(DevelopmentCardType.KNIGHT);
            knight.markAsPlayed();
            player.addDevelopmentCard(knight);
        }
    }

    @BeforeEach
    void setUp() {
        this.board = createBoard();
        this.p1 = new Player("first", PlayerColor.RED);
        this.p2 = new Player("second", PlayerColor.BLUE);
        this.calc = new VictoryPointCalculator();
        this.tracker = new SpecialCardTracker();
        this.game = new Game(List.of(p1, p2), board);
    }

    @Test
    public void GetTotalVP_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getTotalVP(null, board, tracker);
        });
    }

    @Test
    public void GetTotalVP_WithNullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getTotalVP(p1, null, tracker);
        });
    }

    @Test
    public void GetTotalVP_WithNullTracker_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getTotalVP(p1, board, null);
        });
    }

    @Test
    public void GetTotalVP_NoVpSources_ReturnsZero() {
        int vp = calc.getTotalVP(p1, board, tracker);
        assertEquals(0, vp);
    }

    @Test
    public void GetTotalVP_OnlySettlements_ReturnsSum() {
        int[] settlementVertices = {0, 2, 11, 15};
        for (int id : settlementVertices) {
            board.getVertex(id).setOwner(p1);
        }

        int vp = calc.getTotalVP(p1, board, tracker);
        assertEquals(4, vp);
    }

    @Test
    public void GetTotalVP_OnlyCities_ReturnsSum() {
        int[] cityVertices = {0, 2, 11};
        for (int id : cityVertices) {
            board.getVertex(id).setOwner(p1);
            board.getVertex(id).upgradeToCity();
        }

        int vp = calc.getTotalVP(p1, board, tracker);
        assertEquals(6, vp);
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

    @Test
    public void GetDevCardVP_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.getDevCardVP(null);
        });
    }

    @Test
    public void GetDevCardVP_ZeroVPCards_ReturnsZero() {
        int vp = calc.getDevCardVP(p1);
        assertEquals(0, vp);
    }

    @Test
    public void GetDevCardVP_OneVPCard_ReturnsOne() {
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));

        int vp = calc.getDevCardVP(p1);
        assertEquals(1, vp);
    }

    @Test
    public void GetDevCardVP_MultipleVPCards_ReturnsSum() {
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));

        int vp = calc.getDevCardVP(p1);
        assertEquals(3, vp);
    }

    @Test
    public void GetDevCardVP_MaximumVPCards_ReturnsMax() {
        for (int i = 0; i < 5; i++) {
            p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        }

        int vp = calc.getDevCardVP(p1);
        assertEquals(5, vp);
    }

    @Test
    public void GetDevCardVP_OnlyNonVpDevCards_ReturnsZero() {
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.MONOPOLY));

        int vp = calc.getDevCardVP(p1);
        assertEquals(0, vp);
    }

    @Test
    public void GetDevCardVP_MixedDevCards_CountsOnlyVPCards() {
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));

        int vp = calc.getDevCardVP(p1);
        assertEquals(2, vp);
    }

    @Test
    public void GetDevCardVP_CardsAreUnplayed_AreStillCounted() {
        DevelopmentCard vpCard1 = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT);
        DevelopmentCard vpCard2 = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT);

        p1.addDevelopmentCard(vpCard1);
        p1.addDevelopmentCard(vpCard2);

        int vp = calc.getDevCardVP(p1);
        assertEquals(2, vp);
    }

    @Test
    public void ComputeLongestRoad_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.computeLongestRoad(null, board);
        });
    }

    @Test
    public void ComputeLongestRoad_WithNullBoard_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.computeLongestRoad(p1, null);
        });
    }

    @Test
    public void ComputeLongestRoad_ZeroRoadsPlaced_ReturnsZero() {
        int length = calc.computeLongestRoad(p1, board);
        assertEquals(0, length);
    }

    @Test
    public void ComputeLongestRoad_OneRoadPlaced_ReturnsOne() {
        board.getEdge(0).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(1, length);
    }

    @Test
    public void ComputeLongestRoad_RoadBelowClaimThreshold_ReturnsLength() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(4, length);
    }

    @Test
    public void ComputeLongestRoad_RoadAtClaimThreshold_ReturnsLength() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);
        board.getEdge(25).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(5, length);
    }

    @Test
    public void ComputeLongestRoad_DisconnectedRoads_ReturnsLongestSegment() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);

        board.getEdge(45).setOwner(p1);
        board.getEdge(52).setOwner(p1);
        board.getEdge(60).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(4, length);
    }

    @Test
    public void ComputeLongestRoad_BranchingPath_ReturnsLongestBranchOnly() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);

        board.getEdge(10).setOwner(p1);
        board.getEdge(18).setOwner(p1);

        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);
        board.getEdge(25).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(5, length);
    }

    @Test
    public void ComputeLongestRoad_CircularPath_EdgesVisitedOnce_ReturnsLength() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(12).setOwner(p1);
        board.getEdge(7).setOwner(p1);
        board.getEdge(1).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(6, length);
    }

    @Test
    public void ComputeLongestRoad_CircularPathWithTail_ReturnsTotalLength() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(12).setOwner(p1);
        board.getEdge(7).setOwner(p1);
        board.getEdge(1).setOwner(p1);

        board.getEdge(19).setOwner(p1);
        board.getEdge(25).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(8, length);
    }

    @Test
    public void ComputeLongestRoad_PathBrokenByOpponentSettlement_ReturnsLongestHalf() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);
        board.getEdge(25).setOwner(p1);
        board.getEdge(34).setOwner(p1);

        board.getVertex(12).setOwner(p2);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(3, length);
    }

    @Test
    public void ComputeLongestRoad_PathNotBrokenByOwnSettlement_ReturnsTotalLength() {
        board.getEdge(0).setOwner(p1);
        board.getEdge(6).setOwner(p1);
        board.getEdge(11).setOwner(p1);
        board.getEdge(19).setOwner(p1);
        board.getEdge(25).setOwner(p1);
        board.getEdge(34).setOwner(p1);

        board.getVertex(12).setOwner(p1);

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(6, length);
    }

    @Test
    public void ComputeLongestRoad_MaximumRoadsPlaced_ReturnsMax() {
        int[] snakeEdges = {
                0,
                6,
                10,
                18,
                23,
                33,
                39,
                49,
                54,
                62,
                66,
                67,
                63,
                56,
                50
        };

        for (int edgeId : snakeEdges) {
            board.getEdge(edgeId).setOwner(p1);
        }

        int length = calc.computeLongestRoad(p1, board);
        assertEquals(15, length);
    }

    @Test
    public void ComputeKnightCount_WithNullPlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calc.computeKnightCount(null);
        });
    }

    @Test
    public void ComputeKnightCount_ZeroKnightsPlayed_ReturnsZero() {
        int knights = calc.computeKnightCount(p1);
        assertEquals(0, knights);
    }

    @Test
    public void ComputeKnightCount_OneKnightPlayed_ReturnsOne() {
        DevelopmentCard knight = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        knight.markAsPlayed();
        p1.addDevelopmentCard(knight);

        int knights = calc.computeKnightCount(p1);
        assertEquals(1, knights);
    }

    @Test
    public void ComputeKnightCount_MultipleKnightsPlayed_ReturnsCount() {
        for (int i = 0; i < 3; i++) {
            DevelopmentCard knight = new DevelopmentCard(DevelopmentCardType.KNIGHT);
            knight.markAsPlayed();
            p1.addDevelopmentCard(knight);
        }

        int knights = calc.computeKnightCount(p1);
        assertEquals(3, knights);
    }

    @Test
    public void ComputeKnightCount_MaximumKnightsPlayed_ReturnsMax() {
        for (int i = 0; i < 14; i++) {
            DevelopmentCard knight = new DevelopmentCard(DevelopmentCardType.KNIGHT);
            knight.markAsPlayed();
            p1.addDevelopmentCard(knight);
        }

        int knights = calc.computeKnightCount(p1);
        assertEquals(14, knights);
    }

    @Test
    public void ComputeKnightCount_UnplayedKnights_AreNotCounted() {
        DevelopmentCard playedKnight = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        playedKnight.markAsPlayed();
        p1.addDevelopmentCard(playedKnight);

        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        p1.addDevelopmentCard(new DevelopmentCard(DevelopmentCardType.KNIGHT));

        int knights = calc.computeKnightCount(p1);
        assertEquals(1, knights);
    }

    @Test
    public void ComputeKnightCount_MixedPlayedDevCards_CountsOnlyKnights() {
        DevelopmentCard knight1 = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        knight1.markAsPlayed();
        p1.addDevelopmentCard(knight1);

        DevelopmentCard knight2 = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        knight2.markAsPlayed();
        p1.addDevelopmentCard(knight2);

        DevelopmentCard monopoly = new DevelopmentCard(DevelopmentCardType.MONOPOLY);
        monopoly.markAsPlayed();
        p1.addDevelopmentCard(monopoly);

        int knights = calc.computeKnightCount(p1);
        assertEquals(2, knights);
    }
}