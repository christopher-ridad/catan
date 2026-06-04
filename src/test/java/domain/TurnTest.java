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


//
//            - **TC11: BuildRoad_EdgeIsNotConnectedToExistingNetwork_ThrowsIllegalStateException** ( :x: )
//            - State of the system: valid `edgeId`, edge is not connected to the player's existing network
//            - Expected output: `IllegalStateException`
//
//            - **TC12: BuildRoad_RoadIsConnectedToRoad_NoExceptionThrown** ( :x: )
//            - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it
//            - Expected output: `getOwner` called on `edgeId` returns `activePlayer`
//
//            - **TC13: BuildRoad_RoadIsConnectedToSettlement_NoExceptionThrown** ( :x: )
//            - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's settlement built on it
//            - Expected output: `getOwner` called on `edgeId` returns `activePlayer`
//
//            - **TC14: BuildRoad_RoadIsConnectedToCity_NoExceptionThrown** ( :x: )
//            - State of the system: valid `edgeId`, edge is connected to a vertex with activePlayer's city built on it
//            - Expected output: `getOwner` called on `edgeId` returns `activePlayer`
//
//            - **TC15: BuildRoad_ConnectedToRoadButBlockedByEnemySettlement_ThrowsIllegalStateException** ( :x: )
//            - State of the system: valid `edgeId`, edge is connected to another edge with activePlayer's road built on it, but edge is connected to a vertex with an enemy settlement on it
//            - Expected output: `IllegalStateException`
//
//            - **TC16 BuildRoad_TurnPhaseNotBuilding_ThrowsIllegalStateException** ( :x: )
//            - State of the system: valid `edgeId`, phase is set to `TRADING`
//            - Expected output: `IllegalStateException`
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
}


