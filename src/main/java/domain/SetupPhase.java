package domain;

import java.util.List;

public class SetupPhase {
    private final Game game;

    public SetupPhase(Game game) {
        validateGame(game);
        this.game = game;
    }

    private void validateGame(Game game) {
       if (game == null) {
           throw new IllegalArgumentException("Game cannot be null");
       }
    }

    public List<Player> getPlacementOrder() {
        throw new UnsupportedOperationException("Not implemented yet");
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
