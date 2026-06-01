package domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class SetupPhase {
    private final Game game;
    private final List<Player> placementOrder;

    public SetupPhase(Game game) {
        validateGame(game);
        this.game = game;
        this.placementOrder = buildPlacementOrder();
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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getCurrentRound() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isComplete() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void placeSettlement(Player player, int vertexId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void placeRoad(Player player, int edgeId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void distributeStartingResources(Player player) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
