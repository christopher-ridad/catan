package domain;

import java.util.List;

public class Turn {

    private final Game game;
    private final Player activePlayer;
    private final DiceRoll dice;
    private final Bank bank;
    private TurnPhase phase;
    private boolean rolledThisTurn;
    private boolean playedDevCardThisTurn;

    Turn(Game game, Player activePlayer, DiceRoll dice, Bank bank) {
        validateGame(game);
        validatePlayer(activePlayer);
        validateDice(dice);
        validateBank(bank);

        this.game = game;
        this.activePlayer = activePlayer;
        this.dice = dice;
        this.bank = bank;
        this.phase = TurnPhase.PRODUCTION;
    }

    private void validateGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    private void validateDice(DiceRoll dice) {
        if (dice == null) {
            throw new IllegalArgumentException("Dice cannot be null");
        }
    }

    private void validateBank(Bank bank) {
        if (bank == null) {
            throw new IllegalArgumentException("Bank cannot be null");
        }
    }

    public TurnPhase getPhase() {
        return this.phase;
    }

    public void buildRoad(int edgeId) {
        activePlayer.removeResources(ResourceType.BRICK, 1);
        activePlayer.removeResources(ResourceType.LUMBER, 1);

        Board board = game.getBoard();
        Edge edge = board.getEdge(edgeId);
        Vertex endpoint1 = edge.getEndpoints().get(0);
        Vertex endpoint2 = edge.getEndpoints().get(1);

        validateRoadConditions(edge, endpoint1, endpoint2, board);
        edge.setOwner(activePlayer);
    }

    private void validateRoadConditions(Edge edge, Vertex endpoint1, Vertex endpoint2, Board board) {
        if (edge.hasRoad()) {
            throw new IllegalStateException("Edge is already occupied by road");
        }
        if (playerRoadCount() == 15) {
            throw new IllegalStateException("Player has already built maximum number of roads");
        }
        if (!isConnected(endpoint1, endpoint2, board)) {
            throw new IllegalStateException("Road must be connected to existing network");
        }
    }

    private int playerRoadCount() {
        Board board = game.getBoard();
        List<Edge> edgeList = board.getEdges();

        int roadCount = 0;
        for (Edge edge : edgeList) {
            if (edge.getOwner().map(owner -> owner == activePlayer).orElse(false)) {
                roadCount += 1;
            }
        }
        return roadCount;
    }

    private boolean isConnected(Vertex endpoint1, Vertex endpoint2, Board board) {
        boolean connected = false;

        if (endpoint1.getOwner().filter(owner -> owner == activePlayer).isPresent() || endpoint2.getOwner().filter(owner -> owner == activePlayer).isPresent()) {
            connected = true;
        } else if (board.isConnectedToPlayer(endpoint1, activePlayer) && !endpoint1.isOccupied()) {
            connected = true;
        } else if (board.isConnectedToPlayer(endpoint2, activePlayer) && !endpoint2.isOccupied()) {
            connected = true;
        }

        return connected;
    }

    public void buildSettlement(int vertexId) {
        activePlayer.removeResources(ResourceType.BRICK, 1);
        activePlayer.removeResources(ResourceType.LUMBER, 1);
        activePlayer.removeResources(ResourceType.WOOL, 1);
        activePlayer.removeResources(ResourceType.GRAIN, 1);
    }


}