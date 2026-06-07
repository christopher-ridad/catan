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
    private int lastRoll;
    private final BuildManager buildManager;
    private final ResourceProduction resourceProduction;

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
        this.buildManager = new BuildManager(game, activePlayer, bank);
        this.resourceProduction = new ResourceProduction();
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

    public int rollDice() {
        validateCanRoll();

        int roll = dice.roll();
        rolledThisTurn = true;
        lastRoll = roll;

        if (roll != 7) {
            produceResources(roll);
        }

        phase = TurnPhase.TRADE;
        return roll;
    }

    public boolean isSevenRolled() {
        return rolledThisTurn && lastRoll == 7;
    }

    private void validateCanRoll() {
        if (phase != TurnPhase.PRODUCTION || rolledThisTurn) {
            throw new IllegalStateException("Dice can only be rolled once, at the start of a turn");
        }
    }

    private void produceResources(int roll) {
        resourceProduction.distributeResources(roll, game.getBoard().getVertices(), bank);
    }

    public void buildRoad(int edgeId) {
        buildManager.buildRoad(edgeId);
    }

    public void buildSettlement(int vertexId) {
        buildManager.buildSettlement(vertexId);
    }

    public void buildCity(int vertexId) {
        buildManager.buildCity(vertexId);
    }
}