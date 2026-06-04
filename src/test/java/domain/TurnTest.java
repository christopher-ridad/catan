package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {
    private Board board;
    private Game game;
    private List<Player> playerList;
    private Player p1, p2, p3, p4;
    private DiceRoll dice;
    private Bank bank;

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

        p1 = new Player("first", PlayerColor.RED);
        p2 = new Player("second", PlayerColor.BLUE);
        p3 = new Player("third", PlayerColor.WHITE);
        p4 = new Player("fourth", PlayerColor.ORANGE);

        playerList = Arrays.asList(p1, p2, p3, p4);

        this.game = new Game(playerList, board);

        this.bank = new Bank();

        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.replay(mockRandom);
        this.dice = new DiceRoll(mockRandom);
    }

    @Test
    public void Constructor_WithNullGame_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(null, p1, dice, bank);
        });
    }

    @Test
    public void Constructor_WithNullActivePlayer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, null, dice, bank);
        });
    }

    @Test
    public void Constructor_WithNullDice_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, p1, null, bank);
        });
    }

    @Test
    public void Constructor_WithNullBank_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Turn turn = new Turn(game, p1, dice, null);
        });
    }

    @Test
    public void Constructor_WithValidArgs_SetsPhaseToProduction() {
        Turn turn = new Turn(game, p1, dice, bank);
        TurnPhase expected = TurnPhase.PRODUCTION;
        assertEquals(expected, turn.getPhase());
    }

    @Test
    public void BuildRoad_PlayerDoesNotHaveBrick_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int edgeId = 2;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p2);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_PlayerDoesNotHaveLumber_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.BRICK, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int edgeId = 2;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p2);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_PlayerHasExactlyOneBrickAndLumber_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId = 2;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p3);

        turn.buildRoad(edgeId);

        assertAll(
                () -> assertEquals(0, p3.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(0, p3.getResourceCount(ResourceType.LUMBER))
        );
    }

    @Test
    public void BuildRoad_EdgeIsOccupied_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p3);
        board.getEdge(edgeId).setOwner(p4);
        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_PlayerHasFourteenRoads_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);
        int edgeId = 15;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p3);

        for (int i = 1; i < 15; i++) {
            board.getEdge(i).setOwner(p3);
        }
        assertDoesNotThrow(() -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_PlayerHasFifteenRoads_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        for (int i = 1; i < 16; i++) {
            board.getEdge(i).setOwner(p3);
        }

        int edgeId = 17;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_EdgeIsNotConnectedToExistingNetwork_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId = 17;

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_RoadIsConnectedToRoad_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId1 = 11;
        int edgeId2 = 12;

        game.getBoard().getEdge(edgeId1).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildRoad(edgeId2);
        });

        assertEquals(game.getBoard().getEdge(edgeId2).getOwner().orElse(null), p3);
    }

    @Test
    public void BuildRoad_RoadIsConnectedToSettlement_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId1 = 11;
        int edgeId2 = 12;

        game.getBoard().getEdge(edgeId1).getEndpoints().get(1).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildRoad(edgeId2);
        });

        assertEquals(game.getBoard().getEdge(edgeId2).getOwner().orElse(null), p3);
    }

    @Test
    public void BuildRoad_RoadIsConnectedToCity_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId1 = 3;
        int edgeId2 = 4;

        game.getBoard().getEdge(edgeId1).getEndpoints().get(1).setOwner(p3);
        game.getBoard().getEdge(edgeId1).getEndpoints().get(1).upgradeToCity();

        assertDoesNotThrow(() -> {
            turn.buildRoad(edgeId2);
        });

        assertEquals(game.getBoard().getEdge(edgeId2).getOwner().orElse(null), p3);
    }

    @Test
    public void BuildRoad_ConnectedToRoadButBlockedByEnemySettlement_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId1 = 11;
        int edgeId2 = 12;

        game.getBoard().getEdge(edgeId1).setOwner(p3);
        game.getBoard().getEdge(edgeId1).getEndpoints().get(1).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId2);
        });
    }

    @Test
    public void BuildSettlement_PlayerDoesNotHaveBrick_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.LUMBER, 1);
        p2.addResources(ResourceType.WOOL, 1);
        p2.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerDoesNotHaveLumber_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.BRICK, 1);
        p2.addResources(ResourceType.WOOL, 1);
        p2.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerDoesNotHaveWool_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.BRICK, 1);
        p2.addResources(ResourceType.LUMBER, 1);
        p2.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerDoesNotHaveGrain_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.BRICK, 1);
        p2.addResources(ResourceType.LUMBER, 1);
        p2.addResources(ResourceType.WOOL, 1);
        Turn turn = new Turn(game, p2, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerHasExactlyOneOfEachRequiredResource_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        turn.buildSettlement(vertexId);

        assertAll(
                () -> assertEquals(0, p3.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(0, p3.getResourceCount(ResourceType.LUMBER)),
                () -> assertEquals(0, p3.getResourceCount(ResourceType.WOOL)),
                () -> assertEquals(0, p3.getResourceCount(ResourceType.GRAIN))
        );
    }

    @Test
    public void BuildSettlement_VertexIsNotConnectedToExistingRoad_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_VertexIsSurroundedByEnemyRoads_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 33;
        int edgeId1 = 39;
        int edgeId2 = 40;
        int edgeId3 = 49;
        game.getBoard().getEdge(edgeId1).setOwner(p4);
        game.getBoard().getEdge(edgeId2).setOwner(p4);
        game.getBoard().getEdge(edgeId3).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_VertexIsConnectedToExistingRoad_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildSettlement(vertexId);
        });

        assertEquals(p3, game.getBoard().getVertex(vertexId).getOwner().orElse(null));
    }

    @Test
    public void BuildSettlement_DoesNotSatisfyDistanceRuleSettlement_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);
        game.getBoard().getVertex(vertexId).getAdjacentVertices().get(0).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_DoesNotSatisfyDistanceRuleCity_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);
        game.getBoard().getVertex(vertexId).getAdjacentVertices().get(0).setOwner(p4);
        game.getBoard().getVertex(vertexId).getAdjacentVertices().get(0).upgradeToCity();

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_SatisfiesDistanceRuleSettlement_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildSettlement(vertexId);
        });

        assertEquals(p3, game.getBoard().getVertex(vertexId).getOwner().orElse(null));
    }

    @Test
    public void BuildSettlement_VertexAlreadyOccupied_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);
        game.getBoard().getVertex(vertexId).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerHasFiveSettlements_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        for (int i = 10; i < 15; i++) {
            game.getBoard().getVertex(i).setOwner(p3);
        }

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildSettlement(vertexId);
        });
    }

    @Test
    public void BuildSettlement_PlayerHasFourSettlements_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        for (int i = 10; i < 14; i++) {
            game.getBoard().getVertex(i).setOwner(p3);
        }

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildSettlement(vertexId);
        });

        assertEquals(p3, game.getBoard().getVertex(vertexId).getOwner().orElse(null));
    }

    @Test
    public void BuildCity_PlayerDoesNotHaveEnoughOre_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 2);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_PlayerDoesNotHaveEnoughGrain_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_PlayerHasExactAmountOfEachRequiredResource_NoExceptionThrown() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        turn.buildCity(vertexId);

        assertAll(
                () -> assertEquals(0, p3.getResourceCount(ResourceType.ORE)),
                () -> assertEquals(0, p3.getResourceCount(ResourceType.GRAIN))
        );
    }

    @Test
    public void BuildCity_PlayerHasFourCities_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        for (int i = 10; i < 14; i++) {
            game.getBoard().getVertex(i).setOwner(p3);
            game.getBoard().getVertex(i).upgradeToCity();
        }

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_PlayerHasThreeCities_NoExceptionThrown() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        for (int i = 10; i < 13; i++) {
            game.getBoard().getVertex(i).setOwner(p3);
            game.getBoard().getVertex(i).upgradeToCity();
        }

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildCity(vertexId);
        });

        assertEquals(p3, game.getBoard().getVertex(vertexId).getOwner().orElse(null));
        assertTrue(game.getBoard().getVertex(vertexId).isCity());
    }

    @Test
    public void BuildCity_PlayerDoesNotHaveExistingSettlement_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_VertexOccupiedByEnemySettlement_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }
}


