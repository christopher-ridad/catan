package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverse;

public class SetupPhase {
    private final Game game;
    private final List<Player> placementOrder;
    private int currentPlacementIndex;
    private Vertex lastPlacedSettlement;
    private final Map<Player, List<Vertex>> placedSettlements;



    public SetupPhase(Game game) {
        validateGame(game);
        this.game = game;
        this.placementOrder = buildPlacementOrder();
        this.currentPlacementIndex = 0;
        this.lastPlacedSettlement = null;
        this.placedSettlements = new HashMap<>();
    }

    private void validateGame(Game game) {
       if (game == null) {
           throw new IllegalArgumentException("Game cannot be null");
       }
    }

    private List<Player> buildPlacementOrder() {
        List<Player> order = new ArrayList<>();
        List<Player> players = game.getPlayers();

        order.addAll(players);

        for (int i = players.size() - 1; i >= 0; i--) {
            order.add(players.get(i));
        }

        return order;
    }

    public List<Player> getPlacementOrder() {
        return placementOrder;
    }

    public Player getCurrentPlayer() {
        if (isComplete()) {
            throw new IllegalStateException("Setup phase is complete");
        }
        return placementOrder.get(currentPlacementIndex);
    }

    public int getCurrentRound() {
        int playerCount = game.getPlayerCount();
        if (currentPlacementIndex < playerCount) {
            return 1;
        }
        return 2;
    }

    public boolean isComplete() {
        return currentPlacementIndex >= placementOrder.size();
    }

    public void placeSettlement(Player player, int vertexId) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        if (vertexId < 0 || vertexId > 53) {
            throw new IllegalArgumentException("vertexId must be between 0 and 53");
        }

        if (!player.equals(getCurrentPlayer())) {
            throw new IllegalStateException(
                    "It is not " + player.getName() + "'s turn. Current player is " +
                            getCurrentPlayer().getName()
            );
        }

        // Board.getVertex handles range validation
        Vertex vertex = game.getBoard().getVertex(vertexId);

        if (vertex.isOccupied()) {
            throw new IllegalStateException(
                    "Vertex " + vertexId + " is already occupied by " +
                            vertex.getOwner().get().getName()
            );
        }

        vertex.setOwner(player);
        lastPlacedSettlement = vertex;

        placedSettlements.computeIfAbsent(player, k -> new ArrayList<>()).add(vertex);
    }

    public void placeRoad(Player player, int edgeId) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        if (!player.equals(getCurrentPlayer())) {
            throw new IllegalStateException(
                    "It is not " + player.getName() + "'s turn. Current player is " +
                            getCurrentPlayer().getName()
            );
        }

        if (lastPlacedSettlement == null) {
            throw new IllegalStateException(
                    "Settlement must be placed before road. No settlement placed in current turn."
            );
        }

        Edge edge = game.getBoard().getEdge(edgeId);

        if (edge.hasRoad()) {
            throw new IllegalStateException(
                    "Edge " + edgeId + " already has a road owned by " +
                            edge.getOwner().get().getName()
            );
        }

        if (!edge.connectsTo(lastPlacedSettlement)) {
            throw new IllegalStateException(
                    "Road at edge " + edgeId + " is not adjacent to the placed settlement. " +
                            "Road must connect to an endpoint of the settlement vertex."
            );
        }

        edge.setOwner(player);

        advanceTurn();
    }

    private void advanceTurn() {
        lastPlacedSettlement = null;
        currentPlacementIndex++;
    }

    public void distributeStartingResources(Player player) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
