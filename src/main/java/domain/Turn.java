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

        if (game.getBoard().getEdge(edgeId).hasRoad()) {
            throw new IllegalStateException("Edge is already occupied by road");
        }
        if (playerRoadCount() == 15) {
            throw new IllegalStateException("Player has already built maximum number of roads");
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
}