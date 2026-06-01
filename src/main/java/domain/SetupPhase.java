package domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class SetupPhase {
    private final Game game;
    private final List<Player> placementOrder;
    private int currentPlacementIndex;


    public SetupPhase(Game game) {
        validateGame(game);
        this.game = game;
        this.placementOrder = buildPlacementOrder();
        this.currentPlacementIndex = 0;
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
    }

    public void placeRoad(Player player, int edgeId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void distributeStartingResources(Player player) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
