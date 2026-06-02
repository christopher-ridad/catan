package domain;

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
        if (activePlayer.getResourceCount(ResourceType.BRICK) < 1) {
            throw new IllegalStateException("Player does not have enough brick for road");
        }
    }
}