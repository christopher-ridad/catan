package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Game {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final List<Player> players;
    private int currentPlayerIndex;

    // Creates a new Game with the given ordered list of players.
    // Throws NullPointerException if {@code players} is null or contains a null entry
    // Throws IllegalArgumentException if the player count is outside [2, 4]

    public Game(List<Player> players, Board board) {
        Objects.requireNonNull(players, "Player list must not be null");
        validatePlayerCount(players.size());
        validateNoNullPlayers(players);

        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.currentPlayerIndex = 0;
    }

    public List<Player> getPlayers() { throw new UnsupportedOperationException("Not implemented yet"); }

    public int getPlayerCount() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Board getBoard() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Player getPlayer(int index) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Private helpers (following the Clean Code style used in Board/Vertex/Edge)

    private void validatePlayerCount(int count) {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Player count must be between " + MIN_PLAYERS
                            + " and " + MAX_PLAYERS + ", but was " + count);
        }
    }

    private void validateNoNullPlayers(List<Player> players) {
        for (Player p : players) {
            Objects.requireNonNull(p, "Player list must not contain null entries");
        }
    }
}
