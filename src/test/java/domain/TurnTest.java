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
        bank.deduct(ResourceType.BRICK, 19);
        bank.deduct(ResourceType.LUMBER, 19);
        bank.deduct(ResourceType.WOOL, 19);
        bank.deduct(ResourceType.ORE, 19);
        bank.deduct(ResourceType.GRAIN, 19);

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

    private DiceRoll mockDiceRoll(int die1Value, int die2Value) {
        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(die1Value - 1).andReturn(die2Value - 1);
        EasyMock.replay(mockRandom);
        return new DiceRoll(mockRandom);
    }

    private Turn newTurnInBuildPhase(Player player) {
        Turn turn = new Turn(game, player, mockDiceRoll(4, 4), bank);
        turn.rollDice();
        turn.advanceToBuild();
        return turn;
    }

    private Vertex findVertexAdjacentToNumber(int number) {
        for (Vertex vertex : board.getVertices()) {
            for (Hex hex : vertex.getAdjacentHexes()) {
                if (hex.getNumberToken() == number && hex.producesResource()) {
                    return vertex;
                }
            }
        }
        throw new IllegalStateException("No vertex found adjacent to a hex with number token " + number);
    }

    private Vertex findVertexAdjacentToExactlyOneHexWithNumber(int number) {
        for (Vertex vertex : board.getVertices()) {
            long matches = vertex.getAdjacentHexes().stream()
                    .filter(hex -> hex.getNumberToken() == number && hex.producesResource())
                    .count();
            if (matches == 1) {
                return vertex;
            }
        }
        throw new IllegalStateException("No vertex found adjacent to exactly one hex with number token " + number);
    }

    @Test
    public void RollDice_ValidCall_ReturnsRollValue() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);

        assertEquals(8, turn.rollDice());
    }

    @Test
    public void RollDice_ValidCall_AdvancesPhaseToTrade() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);

        turn.rollDice();

        assertEquals(TurnPhase.TRADE, turn.getPhase());
    }

    @Test
    public void RollDice_CalledOutsideProductionPhase_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);

        turn.rollDice();

        assertThrows(IllegalStateException.class, turn::rollDice);
    }

    @Test
    public void RollDice_NonSevenRoll_DistributesResourcesToSettlementOwner() {
        Vertex vertex = findVertexAdjacentToNumber(8);
        vertex.setOwner(p1);
        ResourceType expectedResource = vertex.getAdjacentHexes().stream()
                .filter(hex -> hex.getNumberToken() == 8 && hex.producesResource())
                .findFirst().get().getTerrainType().getResourceType();

        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, new Bank());

        turn.rollDice();

        assertTrue(p1.getResourceCount(expectedResource) > 0);
    }

    @Test
    public void RollDice_RollOfSeven_NoResourcesDistributed() {
        Vertex vertex = findVertexAdjacentToNumber(8);
        vertex.setOwner(p1);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);

        turn.rollDice();

        assertEquals(0, p1.getTotalResourceCount());
    }

    @Test
    public void RollDice_NumberMatchesRobberHex_DoesNotProduceFromThatHex() {
        Vertex vertex = findVertexAdjacentToExactlyOneHexWithNumber(8);
        vertex.setOwner(p1);
        Hex robbedHex = vertex.getAdjacentHexes().stream()
                .filter(hex -> hex.getNumberToken() == 8 && hex.producesResource())
                .findFirst().get();
        board.setRobberHex(robbedHex);

        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, new Bank());

        turn.rollDice();

        assertEquals(0, p1.getTotalResourceCount());
    }

    @Test
    public void IsSevenRolled_BeforeRollingDice_ReturnsFalse() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertFalse(turn.isSevenRolled());
    }

    @Test
    public void IsSevenRolled_AfterRollingSeven_ReturnsTrue() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);

        turn.rollDice();

        assertTrue(turn.isSevenRolled());
    }

    @Test
    public void IsSevenRolled_AfterRollingNonSeven_ReturnsFalse() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);

        turn.rollDice();

        assertFalse(turn.isSevenRolled());
    }

    private Vertex findVertexAdjacentToHex(Hex hex) {
        for (Vertex vertex : board.getVertices()) {
            if (vertex.getAdjacentHexes().contains(hex)) {
                return vertex;
            }
        }
        throw new IllegalStateException("No vertex found adjacent to the given hex");
    }

    private Hex findNonDesertHex() {
        for (Hex hex : board.getHexes()) {
            if (!hex.isDesert()) {
                return hex;
            }
        }
        throw new IllegalStateException("No non-desert hex found");
    }

    @Test
    public void RollDice_RollOfSeven_PlayerWithEightCards_DiscardsFour() {
        p2.addResources(ResourceType.BRICK, 8);
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);

        turn.rollDice();

        assertEquals(4, p2.getTotalResourceCount());
    }

    @Test
    public void RollDice_RollOfSeven_PlayerWithNineCards_DiscardsFour() {
        p2.addResources(ResourceType.BRICK, 5);
        p2.addResources(ResourceType.LUMBER, 4);
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);

        turn.rollDice();

        assertEquals(5, p2.getTotalResourceCount());
    }

    @Test
    public void RollDice_RollOfSeven_PlayerWithSevenCards_NoDiscard() {
        p2.addResources(ResourceType.BRICK, 7);
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);

        turn.rollDice();

        assertEquals(7, p2.getTotalResourceCount());
    }

    @Test
    public void GetRobbingCandidates_OpponentSettlementAdjacentToRobberHex_IncludesOpponent() {
        Vertex vertex = findVertexAdjacentToHex(board.getRobberHex());
        vertex.setOwner(p2);
        Turn turn = new Turn(game, p1, dice, bank);

        assertTrue(turn.getRobbingCandidates().contains(p2));
    }

    @Test
    public void GetRobbingCandidates_ActivePlayerSettlementAdjacentToRobberHex_ExcludesActivePlayer() {
        Vertex vertex = findVertexAdjacentToHex(board.getRobberHex());
        vertex.setOwner(p1);
        Turn turn = new Turn(game, p1, dice, bank);

        assertFalse(turn.getRobbingCandidates().contains(p1));
    }

    @Test
    public void GetRobbingCandidates_NoSettlementsAdjacentToRobberHex_ReturnsEmptyList() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertTrue(turn.getRobbingCandidates().isEmpty());
    }

    @Test
    public void MoveRobber_BeforeSevenRolled_ThrowsIllegalStateException() {
        Turn turn = new Turn(game, p1, dice, bank);
        Hex target = findNonDesertHex();

        assertThrows(IllegalStateException.class, () -> turn.moveRobber(board.getHexes().indexOf(target)));
    }

    @Test
    public void MoveRobber_ToCurrentRobberHex_ThrowsIllegalArgumentException() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        int currentRobberHexId = board.getHexes().indexOf(board.getRobberHex());

        assertThrows(IllegalArgumentException.class, () -> turn.moveRobber(currentRobberHexId));
    }

    @Test
    public void MoveRobber_WithInvalidHexId_ThrowsIllegalArgumentException() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();

        assertThrows(IllegalArgumentException.class, () -> turn.moveRobber(99));
    }

    @Test
    public void MoveRobber_ToValidHex_UpdatesBoardRobberHex() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        Hex target = findNonDesertHex();

        turn.moveRobber(board.getHexes().indexOf(target));

        assertEquals(target, board.getRobberHex());
    }

    @Test
    public void MoveRobber_CalledTwice_ThrowsIllegalStateException() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        Hex firstTarget = findNonDesertHex();
        turn.moveRobber(board.getHexes().indexOf(firstTarget));

        assertThrows(IllegalStateException.class, () -> turn.moveRobber(99));
    }

    @Test
    public void Steal_BeforeMovingRobber_ThrowsIllegalStateException() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();

        assertThrows(IllegalStateException.class, () -> turn.steal(p2));
    }

    @Test
    public void Steal_WhenNoRobbingCandidates_ThrowsIllegalStateException() {
        Hex target = findNonDesertHex();
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        assertThrows(IllegalStateException.class, () -> turn.steal(p2));
    }

    @Test
    public void Steal_FromPlayerNotAdjacentToRobberHex_ThrowsIllegalArgumentException() {
        Hex target = findNonDesertHex();
        Vertex vertex = findVertexAdjacentToHex(target);
        vertex.setOwner(p3);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        assertThrows(IllegalArgumentException.class, () -> turn.steal(p2));
    }

    @Test
    public void Steal_TargetHasNoResourceCards_CompletesWithNoTransferAndResolvesRobber() {
        Hex target = findNonDesertHex();
        Vertex vertex = findVertexAdjacentToHex(target);
        vertex.setOwner(p2);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        turn.steal(p2);

        assertAll(
                () -> assertEquals(0, p2.getTotalResourceCount()),
                () -> assertEquals(0, p1.getTotalResourceCount()),
                () -> assertDoesNotThrow(turn::advanceToBuild)
        );
    }

    @Test
    public void Steal_FromValidCandidate_TransfersOneResourceCardToActivePlayer() {
        Hex target = findNonDesertHex();
        Vertex vertex = findVertexAdjacentToHex(target);
        vertex.setOwner(p2);
        p2.addResources(ResourceType.ORE, 1);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        turn.steal(p2);

        assertAll(
                () -> assertEquals(0, p2.getResourceCount(ResourceType.ORE)),
                () -> assertEquals(1, p1.getResourceCount(ResourceType.ORE))
        );
    }

    @Test
    public void AdvanceToBuild_FromTradePhase_SetsPhaseToBuild() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        turn.advanceToBuild();

        assertEquals(TurnPhase.BUILD, turn.getPhase());
    }

    @Test
    public void AdvanceToBuild_FromProductionPhase_ThrowsIllegalStateException() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertThrows(IllegalStateException.class, turn::advanceToBuild);
    }

    @Test
    public void AdvanceToBuild_FromBuildPhase_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        turn.advanceToBuild();

        assertThrows(IllegalStateException.class, turn::advanceToBuild);
    }

    @Test
    public void AdvanceToBuild_WithRobberMovePending_ThrowsIllegalStateException() {
        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();

        assertThrows(IllegalStateException.class, turn::advanceToBuild);
    }

    @Test
    public void AdvanceToBuild_WithStealPending_ThrowsIllegalStateException() {
        Hex target = findNonDesertHex();
        Vertex vertex = findVertexAdjacentToHex(target);
        vertex.setOwner(p2);
        p2.addResources(ResourceType.ORE, 1);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        assertThrows(IllegalStateException.class, turn::advanceToBuild);
    }

    @Test
    public void AdvanceToBuild_AfterRobberFullyResolved_SetsPhaseToBuild() {
        Hex target = findNonDesertHex();
        Vertex vertex = findVertexAdjacentToHex(target);
        vertex.setOwner(p2);
        p2.addResources(ResourceType.ORE, 1);

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));
        turn.steal(p2);

        turn.advanceToBuild();

        assertEquals(TurnPhase.BUILD, turn.getPhase());
    }

    @Test
    public void AdvanceToBuild_AfterRobberMovedWithNoCandidates_SetsPhaseToBuild() {
        Hex target = findNonDesertHex();

        DiceRoll sevenDice = mockDiceRoll(3, 4);
        Turn turn = new Turn(game, p1, sevenDice, bank);
        turn.rollDice();
        turn.moveRobber(board.getHexes().indexOf(target));

        turn.advanceToBuild();

        assertEquals(TurnPhase.BUILD, turn.getPhase());
    }

    @Test
    public void AdvanceToBuild_WithPendingTrade_RejectsAndClearsTheOffer() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        turn.advanceToBuild();

        assertAll(
                () -> assertEquals(TradeOffer.TradeStatus.REJECTED, offer.getStatus()),
                () -> assertEquals(Optional.empty(), turn.getPendingTrade())
        );
    }

    @Test
    public void EndTurn_FromBuildPhase_SetsPhaseToDone() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        turn.advanceToBuild();

        turn.endTurn();

        assertEquals(TurnPhase.DONE, turn.getPhase());
    }

    @Test
    public void EndTurn_FromTradePhase_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        assertThrows(IllegalStateException.class, turn::endTurn);
    }

    @Test
    public void EndTurn_FromProductionPhase_ThrowsIllegalStateException() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertThrows(IllegalStateException.class, turn::endTurn);
    }

    @Test
    public void EndTurn_CalledTwice_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        turn.advanceToBuild();
        turn.endTurn();

        assertThrows(IllegalStateException.class, turn::endTurn);
    }

    @Test
    public void BuildRoad_OutsideBuildPhase_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int edgeId = 2;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> turn.buildRoad(edgeId));
    }

    @Test
    public void BuildRoad_PlayerDoesNotHaveBrick_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.LUMBER, 1);
        Turn turn = newTurnInBuildPhase(p2);

        int edgeId = 2;
        game.getBoard().getEdge(edgeId).getEndpoints().get(0).setOwner(p2);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_PlayerDoesNotHaveLumber_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.BRICK, 1);
        Turn turn = newTurnInBuildPhase(p2);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);
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
        Turn turn = newTurnInBuildPhase(p3);
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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

        int edgeId = 17;

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId);
        });
    }

    @Test
    public void BuildRoad_RoadIsConnectedToRoad_NoExceptionThrown() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

        int edgeId1 = 11;
        int edgeId2 = 12;

        game.getBoard().getEdge(edgeId1).setOwner(p3);
        game.getBoard().getEdge(edgeId1).getEndpoints().get(1).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildRoad(edgeId2);
        });
    }

    @Test
    public void BuildSettlement_OutsideBuildPhase_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.BRICK, 1);
        p3.addResources(ResourceType.LUMBER, 1);
        p3.addResources(ResourceType.WOOL, 1);
        p3.addResources(ResourceType.GRAIN, 1);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        int edgeId = 4;
        game.getBoard().getEdge(edgeId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> turn.buildSettlement(vertexId));
    }

    @Test
    public void BuildSettlement_PlayerDoesNotHaveBrick_ThrowsIllegalStateException() {
        p2.addResources(ResourceType.LUMBER, 1);
        p2.addResources(ResourceType.WOOL, 1);
        p2.addResources(ResourceType.GRAIN, 1);
        Turn turn = newTurnInBuildPhase(p2);

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
        Turn turn = newTurnInBuildPhase(p2);

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
        Turn turn = newTurnInBuildPhase(p2);

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
        Turn turn = newTurnInBuildPhase(p2);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
    public void BuildCity_OutsideBuildPhase_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = new Turn(game, p3, dice, bank);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertThrows(IllegalStateException.class, () -> turn.buildCity(vertexId));
    }

    @Test
    public void BuildCity_PlayerDoesNotHaveEnoughOre_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 2);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

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
        Turn turn = newTurnInBuildPhase(p3);

        int vertexId = 2;

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_VertexOccupiedByEnemySettlement_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = newTurnInBuildPhase(p3);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p4);

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_VertexOccupiedByEnemyCity_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = newTurnInBuildPhase(p3);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p4);
        game.getBoard().getVertex(vertexId).upgradeToCity();

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_VertexOccupiedByOwnCity_ThrowsIllegalStateException() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = newTurnInBuildPhase(p3);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);
        game.getBoard().getVertex(vertexId).upgradeToCity();

        assertThrows(IllegalStateException.class, () -> {
            turn.buildCity(vertexId);
        });
    }

    @Test
    public void BuildCity_VertexOccupiedByOwnSettlement_NoExceptionThrown() {
        p3.addResources(ResourceType.ORE, 3);
        p3.addResources(ResourceType.GRAIN, 2);
        Turn turn = newTurnInBuildPhase(p3);

        int vertexId = 2;
        game.getBoard().getVertex(vertexId).setOwner(p3);

        assertDoesNotThrow(() -> {
            turn.buildCity(vertexId);
        });

        assertEquals(p3, game.getBoard().getVertex(vertexId).getOwner().orElse(null));
        assertTrue(game.getBoard().getVertex(vertexId).isCity());
    }

    @Test
    public void ProposeTrade_OutsideTradePhase_ThrowsIllegalStateException() {
        Turn turn = new Turn(game, p1, dice, bank);

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);

        assertThrows(IllegalStateException.class, () -> turn.proposeTrade(p2, offering, requesting));
    }

    @Test
    public void ProposeTrade_RecipientNotInGame_ThrowsIllegalArgumentException() {
        Player outsider = new Player("outsider", PlayerColor.RED);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);

        assertThrows(IllegalArgumentException.class, () -> turn.proposeTrade(outsider, offering, requesting));
    }

    @Test
    public void ProposeTrade_WhenTradeAlreadyPending_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        turn.proposeTrade(p2, offering, requesting);

        assertThrows(IllegalStateException.class, () -> turn.proposeTrade(p3, offering, requesting));
    }

    @Test
    public void ProposeTrade_OffererCannotAffordOffering_ThrowsIllegalArgumentException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);

        assertThrows(IllegalArgumentException.class, () -> turn.proposeTrade(p2, offering, requesting));
    }

    @Test
    public void ProposeTrade_OffererHasExactlyOfferedAmount_CreatesAndStoresPendingTrade() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);

        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        assertAll(
                () -> assertTrue(offer.isPending()),
                () -> assertEquals(Optional.of(offer), turn.getPendingTrade())
        );
    }

    @Test
    public void ProposeTrade_AfterPriorOfferResolved_CreatesNewPendingTrade() {
        p1.addResources(ResourceType.BRICK, 1);
        p1.addResources(ResourceType.LUMBER, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering1 = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting1 = Map.of(ResourceType.WOOL, 1);
        TradeOffer first = turn.proposeTrade(p2, offering1, requesting1);
        turn.rejectTrade(first);

        Map<ResourceType, Integer> offering2 = Map.of(ResourceType.LUMBER, 1);
        Map<ResourceType, Integer> requesting2 = Map.of(ResourceType.ORE, 1);
        TradeOffer second = turn.proposeTrade(p3, offering2, requesting2);

        assertAll(
                () -> assertTrue(second.isPending()),
                () -> assertEquals(Optional.of(second), turn.getPendingTrade())
        );
    }

    @Test
    public void AcceptTrade_NoMatchingPendingTrade_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer foreignOffer = new TradeOffer(p1, p2, offering, requesting);

        assertThrows(IllegalStateException.class, () -> turn.acceptTrade(foreignOffer));
    }

    @Test
    public void AcceptTrade_OffererCannotAffordOffering_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 1);
        p2.addResources(ResourceType.WOOL, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        p1.removeResources(ResourceType.BRICK, 1);

        assertThrows(IllegalStateException.class, () -> turn.acceptTrade(offer));
    }

    @Test
    public void AcceptTrade_RecipientCannotAffordRequesting_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        assertThrows(IllegalStateException.class, () -> turn.acceptTrade(offer));
    }

    @Test
    public void AcceptTrade_BothPlayersCanAfford_ExchangesResourcesAndClearsPendingTrade() {
        p1.addResources(ResourceType.BRICK, 1);
        p2.addResources(ResourceType.WOOL, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        turn.acceptTrade(offer);

        assertAll(
                () -> assertEquals(0, p1.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(1, p1.getResourceCount(ResourceType.WOOL)),
                () -> assertEquals(1, p2.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(0, p2.getResourceCount(ResourceType.WOOL)),
                () -> assertEquals(TradeOffer.TradeStatus.ACCEPTED, offer.getStatus()),
                () -> assertEquals(Optional.empty(), turn.getPendingTrade())
        );
    }

    @Test
    public void RejectTrade_NoMatchingPendingTrade_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer foreignOffer = new TradeOffer(p1, p2, offering, requesting);

        assertThrows(IllegalStateException.class, () -> turn.rejectTrade(foreignOffer));
    }

    @Test
    public void RejectTrade_PendingOffer_MarksRejectedAndClearsPendingTrade() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        turn.rejectTrade(offer);

        assertAll(
                () -> assertEquals(TradeOffer.TradeStatus.REJECTED, offer.getStatus()),
                () -> assertEquals(Optional.empty(), turn.getPendingTrade())
        );
    }

    @Test
    public void GetPendingTrade_NoOfferProposed_ReturnsEmpty() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertEquals(Optional.empty(), turn.getPendingTrade());
    }

    @Test
    public void GetPendingTrade_AfterProposeTrade_ReturnsTheOffer() {
        p1.addResources(ResourceType.BRICK, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        Map<ResourceType, Integer> offering = Map.of(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> requesting = Map.of(ResourceType.WOOL, 1);
        TradeOffer offer = turn.proposeTrade(p2, offering, requesting);

        assertEquals(Optional.of(offer), turn.getPendingTrade());
    }

    @Test
    public void SubmitMaritimeTrade_OutsideTradePhase_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 4);
        Turn turn = newTurnInBuildPhase(p1);
        MaritimeTrade trade = new MaritimeTrade(p1, ResourceType.BRICK, 4, ResourceType.WOOL, board);

        assertThrows(IllegalStateException.class, () -> turn.submitMaritimeTrade(trade));
    }

    @Test
    public void SubmitMaritimeTrade_BankHasNoneOfReceivingResource_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.BRICK, 4);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        MaritimeTrade trade = new MaritimeTrade(p1, ResourceType.BRICK, 4, ResourceType.WOOL, board);

        assertThrows(IllegalStateException.class, () -> turn.submitMaritimeTrade(trade));
    }

    @Test
    public void SubmitMaritimeTrade_BankHasExactlyOneOfReceivingResource_ExecutesTrade() {
        p1.addResources(ResourceType.BRICK, 4);
        bank.collect(ResourceType.WOOL, 1);
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        int bankBrickBefore = bank.getResourceCount(ResourceType.BRICK);
        MaritimeTrade trade = new MaritimeTrade(p1, ResourceType.BRICK, 4, ResourceType.WOOL, board);

        turn.submitMaritimeTrade(trade);

        assertAll(
                () -> assertEquals(0, p1.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(1, p1.getResourceCount(ResourceType.WOOL)),
                () -> assertEquals(bankBrickBefore + 4, bank.getResourceCount(ResourceType.BRICK)),
                () -> assertEquals(0, bank.getResourceCount(ResourceType.WOOL))
        );
    }

    private void giveDevelopmentCardCost(Player player) {
        player.addResources(ResourceType.ORE, 1);
        player.addResources(ResourceType.WOOL, 1);
        player.addResources(ResourceType.GRAIN, 1);
    }

    private void cycleDevelopmentCardCostThroughBank(Player player, Bank bank) {
        bank.deduct(ResourceType.ORE, 1);
        player.addResources(ResourceType.ORE, 1);
        bank.deduct(ResourceType.WOOL, 1);
        player.addResources(ResourceType.WOOL, 1);
        bank.deduct(ResourceType.GRAIN, 1);
        player.addResources(ResourceType.GRAIN, 1);
    }

    private DevelopmentCard buyUntilNonVictoryPointCard(Turn turn, Player player) {
        DevelopmentCard card;
        do {
            giveDevelopmentCardCost(player);
            turn.buyDevelopmentCard();
            List<DevelopmentCard> hand = turn.getPlayerHand(player);
            card = hand.get(hand.size() - 1);
        } while (card.getType() == DevelopmentCardType.VICTORY_POINT);
        return card;
    }

    @Test
    public void BuyDevelopmentCard_OutsideBuildPhase_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();

        assertThrows(IllegalStateException.class, turn::buyDevelopmentCard);
    }

    @Test
    public void BuyDevelopmentCard_PlayerCannotAffordCost_ThrowsIllegalStateException() {
        p1.addResources(ResourceType.WOOL, 1);
        p1.addResources(ResourceType.GRAIN, 1);
        Turn turn = newTurnInBuildPhase(p1);

        assertThrows(IllegalStateException.class, turn::buyDevelopmentCard);
    }

    @Test
    public void BuyDevelopmentCard_NoCardsRemainInDeck_ThrowsIllegalStateException() {
        Bank freshBank = new Bank();
        Turn turn = new Turn(game, p1, mockDiceRoll(4, 4), freshBank);
        turn.rollDice();
        turn.advanceToBuild();
        int deckSize = turn.getRemainingDeckSize();
        for (int i = 0; i < deckSize; i++) {
            cycleDevelopmentCardCostThroughBank(p1, freshBank);
            turn.buyDevelopmentCard();
        }

        assertEquals(0, turn.getRemainingDeckSize());
        assertThrows(IllegalStateException.class, turn::buyDevelopmentCard);
    }

    @Test
    public void BuyDevelopmentCard_PlayerHasExactCostAndOneCardRemains_DeductsCostAndAddsCardToHand() {
        Bank freshBank = new Bank();
        Turn turn = new Turn(game, p1, mockDiceRoll(4, 4), freshBank);
        turn.rollDice();
        turn.advanceToBuild();
        int deckSize = turn.getRemainingDeckSize();
        for (int i = 0; i < deckSize - 1; i++) {
            cycleDevelopmentCardCostThroughBank(p1, freshBank);
            turn.buyDevelopmentCard();
        }
        assertEquals(1, turn.getRemainingDeckSize());
        int handSizeBefore = turn.getPlayerHand(p1).size();
        cycleDevelopmentCardCostThroughBank(p1, freshBank);

        turn.buyDevelopmentCard();

        assertAll(
                () -> assertEquals(0, p1.getResourceCount(ResourceType.ORE)),
                () -> assertEquals(0, p1.getResourceCount(ResourceType.WOOL)),
                () -> assertEquals(0, p1.getResourceCount(ResourceType.GRAIN)),
                () -> assertEquals(0, turn.getRemainingDeckSize()),
                () -> assertEquals(handSizeBefore + 1, turn.getPlayerHand(p1).size())
        );
    }

    @Test
    public void PlayDevelopmentCard_OutsideTradeOrBuildPhase_ThrowsIllegalStateException() {
        Turn turn = new Turn(game, p1, dice, bank);
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);

        assertThrows(IllegalStateException.class, () -> turn.playDevelopmentCard(p1, card));
    }

    @Test
    public void PlayDevelopmentCard_DevCardAlreadyPlayedThisTurn_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        DevelopmentCard first = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        DevelopmentCard second = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        game.addDevelopmentCardToHand(p1, first);
        game.addDevelopmentCardToHand(p1, second);
        turn.playDevelopmentCard(p1, first);

        assertThrows(IllegalStateException.class, () -> turn.playDevelopmentCard(p1, second));
    }

    @Test
    public void PlayDevelopmentCard_NonVictoryPointCardPurchasedThisTurn_ThrowsIllegalStateException() {
        Turn turn = newTurnInBuildPhase(p1);
        DevelopmentCard purchased = buyUntilNonVictoryPointCard(turn, p1);

        assertThrows(IllegalStateException.class, () -> turn.playDevelopmentCard(p1, purchased));
    }

    @Test
    public void PlayDevelopmentCard_CardAlreadyPlayed_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        card.markAsPlayed();
        game.addDevelopmentCardToHand(p1, card);

        assertThrows(IllegalStateException.class, () -> turn.playDevelopmentCard(p1, card));
    }

    @Test
    public void PlayDevelopmentCard_VictoryPointCard_ThrowsIllegalStateException() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.VICTORY_POINT);
        game.addDevelopmentCardToHand(p1, card);

        assertThrows(IllegalStateException.class, () -> turn.playDevelopmentCard(p1, card));
    }

    @Test
    public void PlayDevelopmentCard_UnplayedCardFromPriorTurn_MarksCardAsPlayed() {
        DiceRoll fixedDice = mockDiceRoll(4, 4);
        Turn turn = new Turn(game, p1, fixedDice, bank);
        turn.rollDice();
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);
        game.addDevelopmentCardToHand(p1, card);

        turn.playDevelopmentCard(p1, card);

        assertTrue(card.isPlayed());
    }

    @Test
    public void GetPlayerHand_PlayerWithNoCards_ReturnsEmptyList() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertTrue(turn.getPlayerHand(p1).isEmpty());
    }

    @Test
    public void GetPlayerHand_AfterBuyingOneCard_ReturnsListContainingPurchasedCard() {
        giveDevelopmentCardCost(p1);
        Turn turn = newTurnInBuildPhase(p1);

        turn.buyDevelopmentCard();

        assertEquals(1, turn.getPlayerHand(p1).size());
    }

    @Test
    public void GetPlayerHand_ReturnedList_IsUnmodifiable() {
        Turn turn = new Turn(game, p1, dice, bank);
        List<DevelopmentCard> hand = turn.getPlayerHand(p1);

        assertThrows(UnsupportedOperationException.class,
                () -> hand.add(new DevelopmentCard(DevelopmentCardType.KNIGHT)));
    }

    @Test
    public void GetRemainingDeckSize_NewTurn_Returns25() {
        Turn turn = new Turn(game, p1, dice, bank);

        assertEquals(25, turn.getRemainingDeckSize());
    }

    @Test
    public void GetRemainingDeckSize_AfterBuyingOneCard_DecreasesByOne() {
        giveDevelopmentCardCost(p1);
        Turn turn = newTurnInBuildPhase(p1);

        turn.buyDevelopmentCard();

        assertEquals(24, turn.getRemainingDeckSize());
    }
}


