package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class TurnManager {

    private final Game game;
    private final Bank bank;
    private final DiceRoll dice;
    private final VictoryPointCalculator victoryPointCalculator;
    private final SpecialCardTracker specialCardTracker;
    private final Map<Player, Integer> playerTurnCounts;

    private int currentTurnNumber;
    private int currentPlayerIndex;
    private Turn currentTurn;
    private Player winner;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Game is a shared mutable entity intentionally referenced by the turn manager.")
    public TurnManager(Game game, Bank bank, DiceRoll dice) {
        validateGame(game);
        validateBank(bank);
        validateDice(dice);

        this.game = game;
        this.bank = bank;
        this.dice = dice;
        this.victoryPointCalculator = new VictoryPointCalculator();
        this.specialCardTracker = new SpecialCardTracker();
        this.playerTurnCounts = initializeTurnCounts(game);

        this.currentTurnNumber = 0;
        this.currentPlayerIndex = 0;
        this.currentTurn = null;
        this.winner = null;
    }

    public Turn startNextTurn() {
        if (winner != null) {
            throw new IllegalStateException("Cannot start a new turn: the game is already over");
        }
        if (currentTurn != null && currentTurn.getPhase() != TurnPhase.DONE) {
            throw new IllegalStateException("Cannot start a new turn until the previous turn is complete");
        }

        Player activePlayer = getCurrentPlayer();
        currentTurn = new Turn(game, activePlayer, dice, bank);
        return currentTurn;
    }

    public void endCurrentTurn() {
        if (currentTurn == null) {
            throw new IllegalStateException("No turn is currently in progress");
        }

        TurnPhase phase = currentTurn.getPhase();
        if (phase != TurnPhase.BUILD && phase != TurnPhase.DONE) {
            throw new IllegalStateException(
                    "Cannot end the current turn while in the " + phase + " phase");
        }

        if (phase == TurnPhase.BUILD) {
            currentTurn.endTurn();
        }

        recordCompletedTurn();
        advanceToNextPlayer();
        checkForWinner();

        currentTurn = null;
    }

    public Player getCurrentPlayer() {
        return game.getPlayers().get(currentPlayerIndex);
    }

    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    public Optional<Turn> getCurrentTurn() {
        return Optional.ofNullable(currentTurn);
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    public int getPlayerTurnCount(Player player) {
        Integer count = playerTurnCounts.get(player);
        if (count == null) {
            throw new IllegalArgumentException("Player is not part of this game");
        }
        return count;
    }

    public SpecialCardTracker getSpecialCardTracker() {
        return specialCardTracker;
    }

    private void recordCompletedTurn() {
        Player activePlayer = getCurrentPlayer();
        playerTurnCounts.merge(activePlayer, 1, Integer::sum);
        currentTurnNumber++;
    }

    private void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % game.getPlayerCount();
    }

    private void checkForWinner() {
        Board board = game.getBoard();
        Player activePlayer = getCurrentPlayer();
        victoryPointCalculator.updateSpecialCards(game, board, specialCardTracker);
        winner = victoryPointCalculator.getWinner(game, board, specialCardTracker, activePlayer).orElse(null);
    }

    private Map<Player, Integer> initializeTurnCounts(Game game) {
        Map<Player, Integer> counts = new HashMap<>();
        for (Player player : game.getPlayers()) {
            counts.put(player, 0);
        }
        return counts;
    }

    private void validateGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
    }

    private void validateBank(Bank bank) {
        if (bank == null) {
            throw new IllegalArgumentException("Bank cannot be null");
        }
    }

    private void validateDice(DiceRoll dice) {
        if (dice == null) {
            throw new IllegalArgumentException("Dice cannot be null");
        }
    }
}