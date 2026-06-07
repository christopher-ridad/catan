package domain;

import java.util.ArrayList;
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
    private boolean robberPendingMove;
    private boolean robberPendingSteal;
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
        } else {
            enforceDiscard();
            robberPendingMove = true;
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

    private void enforceDiscard() {
        for (Player player : game.getPlayers()) {
            int total = player.getTotalResourceCount();
            if (total > 7) {
                discardHalf(player, total / 2);
            }
        }
    }

    private void discardHalf(Player player, int amount) {
        int remaining = amount;
        for (ResourceType type : ResourceType.values()) {
            int toRemove = Math.min(player.getResourceCount(type), remaining);
            if (toRemove > 0) {
                player.removeResources(type, toRemove);
                bank.collect(type, toRemove);
                remaining -= toRemove;
            }
        }
    }

    public void moveRobber(int hexId) {
        if (!robberPendingMove) {
            throw new IllegalStateException("Robber can only be moved immediately after rolling a 7");
        }

        Board board = game.getBoard();
        List<Hex> hexes = board.getHexes();
        if (hexId < 0 || hexId >= hexes.size()) {
            throw new IllegalArgumentException("Invalid hex id: " + hexId);
        }

        Hex target = hexes.get(hexId);
        if (target == board.getRobberHex()) {
            throw new IllegalArgumentException("Robber is already on that hex");
        }

        board.setRobberHex(target);
        robberPendingMove = false;
        robberPendingSteal = !getRobbingCandidates().isEmpty();
    }

    public void steal(Player target) {
        if (!robberPendingSteal) {
            throw new IllegalStateException("Stealing is only allowed immediately after moving the robber onto a hex with eligible targets");
        }
        if (!getRobbingCandidates().contains(target)) {
            throw new IllegalArgumentException("Target must have a settlement or city adjacent to the robber's hex");
        }

        ResourceType stolen = pickResourceToSteal(target);
        target.removeResources(stolen, 1);
        activePlayer.addResources(stolen, 1);
        robberPendingSteal = false;
    }

    private ResourceType pickResourceToSteal(Player target) {
        for (ResourceType type : ResourceType.values()) {
            if (target.getResourceCount(type) > 0) {
                return type;
            }
        }
        throw new IllegalStateException("Target has no resource cards to steal");
    }

    public List<Player> getRobbingCandidates() {
        Hex robberHex = game.getBoard().getRobberHex();
        List<Player> candidates = new ArrayList<>();
        for (Vertex vertex : game.getBoard().getVertices()) {
            vertex.getOwner().ifPresent(owner -> {
                if (owner != activePlayer
                        && vertex.getAdjacentHexes().contains(robberHex)
                        && !candidates.contains(owner)) {
                    candidates.add(owner);
                }
            });
        }
        return candidates;
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