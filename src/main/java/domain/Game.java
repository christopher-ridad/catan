package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class Game {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final List<Player> players;
    private final Board board;
    private final List<DevelopmentCard> developmentDeck;
    private final Map<Player, List<DevelopmentCard>> playerHands;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Board is a shared mutable entity intentionally referenced by Game and its collaborators.")
    public Game(List<Player> players, Board board) {
        Objects.requireNonNull(players, "Player list must not be null");
        Objects.requireNonNull(board, "Board must not be null");
        validatePlayerCount(players.size());
        validateNoNullPlayers(players);
        validateUniqueColors(players);

        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = board;
        this.developmentDeck = createShuffledDevelopmentDeck();
        this.playerHands = createEmptyPlayerHands(this.players);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getRemainingDevelopmentCardCount() {
        return developmentDeck.size();
    }

    DevelopmentCard drawDevelopmentCard() {
        if (developmentDeck.isEmpty()) {
            throw new IllegalStateException("No development cards remain in the deck");
        }
        return developmentDeck.remove(developmentDeck.size() - 1);
    }

    public List<DevelopmentCard> getPlayerHand(Player player) {
        List<DevelopmentCard> hand = playerHands.get(player);
        if (hand == null) {
            throw new IllegalArgumentException("Player is not part of this game");
        }
        return Collections.unmodifiableList(hand);
    }

    void addDevelopmentCardToHand(Player player, DevelopmentCard card) {
        List<DevelopmentCard> hand = playerHands.get(player);
        if (hand == null) {
            throw new IllegalArgumentException("Player is not part of this game");
        }
        hand.add(card);
        player.addDevelopmentCard(card);
    }

    private List<DevelopmentCard> createShuffledDevelopmentDeck() {
        List<DevelopmentCard> deck = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            deck.add(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        }
        for (int i = 0; i < 2; i++) {
            deck.add(new DevelopmentCard(DevelopmentCardType.ROAD_BUILDING));
            deck.add(new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY));
            deck.add(new DevelopmentCard(DevelopmentCardType.MONOPOLY));
        }
        for (int i = 0; i < 5; i++) {
            deck.add(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        }
        Collections.shuffle(deck);
        return deck;
    }

    private Map<Player, List<DevelopmentCard>> createEmptyPlayerHands(List<Player> players) {
        Map<Player, List<DevelopmentCard>> hands = new HashMap<>();
        for (Player player : players) {
            hands.put(player, new ArrayList<>());
        }
        return hands;
    }

    public int getPlayerCount() {
        return players.size();
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
            justification = "Board is a shared mutable entity intentionally referenced by Game and its collaborators.")
    public Board getBoard() {
        return board;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

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