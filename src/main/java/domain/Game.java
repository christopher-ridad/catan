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

    // Creates a new Game with the given ordered list of players.
    // Throws NullPointerException if {@code players} is null or contains a null entry
    // Throws IllegalArgumentException if the player count is outside [2, 4]
    // Throws IllegalArgumentException if any two players share the same color

    public Game(List<Object> players, Board board) {
        Objects.requireNonNull(players, "Player list must not be null");
        Objects.requireNonNull(board, "Board must not be null");
        validatePlayerCount(players.size());
        validateNoNullPlayers(players);
        validateUniqueColors(players);

        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = board;
    }

    public List<Object> getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Board getBoard() {
        return board;
    }

    public Object getPlayer(int index) {
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

    private void validateNoNullPlayers(List<Object> players) {
        for (Object p : players) {
            Objects.requireNonNull(p, "Player list must not contain null entries");
        }
    }

    private void validateUniqueColors(List<Object> players) {
        java.util.Set<Object> seen = new java.util.HashSet<>();
        for (Object p : players) {
            try {
                Object color = p.getClass().getMethod("getColor").invoke(p);
                if (!seen.add(color)) {
                    throw new IllegalArgumentException(
                            "All players must have unique colors, but found duplicate: " + color);
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Player object must have a getColor() method", e);
            }
        }
    }
}