package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Game of Catan
 *
 * Adresses issue #9: Implement Game setup with player count validation.
 *
 * NOTE: Player is typed as Object temporarily until PR #17 (domain/Player) gets merged.
 */

public class Game {

    // NOTE: All instances of 'Object' should be reverted to type 'Player' after PR #17 is merged

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final List<Object> players; // Change type to <Player> once PR #17 gets merged
    private final Board board;
    private int currentPlayerIndex;

    // Creates a new Game with the given ordered list of players.
    // Throws NullPointerException if {@code players} is null or contains a null entry
    // Throws IllegalArgumentException if the player count is outside [2, 4]

    public Game(List<Object> players, Board board) {
        Objects.requireNonNull(players, "Player list must not be null");
        Objects.requireNonNull(board, "Board must not be null");
        validatePlayerCount(players.size());
        validateNoNullPlayers(players);

        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = board;
        this.currentPlayerIndex = 0;
    }

    public List<Object> getPlayers() { return players; }

    public int getPlayerCount() {
        return players.size();
    }


    public Board getBoard() { return board; }

    public Object getCurrentPlayer() { return players.get(currentPlayerIndex); }

    // Private helpers (following the Clean Code style used in Board/Vertex/Edge)

    private void validatePlayerCount(int count) {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Player count must be between " + MIN_PLAYERS
                            + " and " + MAX_PLAYERS + ", but was " + count);
        }
    }

    private void validateNoNullPlayers(List<Object> players) {
        for (Object p : players) {
            Objects.requireNonNull(p, "Player list must not contain null entries");
        }
    }
}
