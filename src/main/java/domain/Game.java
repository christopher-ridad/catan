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
 */

public final class Game {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final List<Player> players; // Change type to <Player> once PR #17 gets merged
    private final Board board;

    // Creates a new Game with the given ordered list of players.
    // Throws NullPointerException if {@code players} is null or contains a null entry
    // Throws IllegalArgumentException if the player count is outside [2, 4]
    // Throws IllegalArgumentException if any two players share the same color

    public Game(List<Player> players, Board board) {
        Objects.requireNonNull(players, "Player list must not be null");
        Objects.requireNonNull(board, "Board must not be null");
        validatePlayerCount(players.size());
        validateNoNullPlayers(players);
        validateUniqueColors(players);

        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer(int index) {
        return players.get(index);
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

    private void validateUniqueColors(List<Player> players) {
        java.util.Set<PlayerColor> seen = new java.util.HashSet<>();
        for (Player p : players) {
            if (!seen.add(p.getColor())) {
                throw new IllegalArgumentException(
                        "All players must have unique colors, but found duplicate: " + p.getColor());
            }
        }
    }
}