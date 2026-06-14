package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Turn {

    private static final Map<ResourceType, Integer> DEVELOPMENT_CARD_COST = Map.of(
            ResourceType.ORE, 1, ResourceType.WOOL, 1, ResourceType.GRAIN, 1);

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
    private final Random random;
    private final BuildManager buildManager;
    private final ResourceProduction resourceProduction;
    private TradeOffer pendingTrade;
    private final List<DevelopmentCard> cardsPurchasedThisTurn;

    Turn(Game game, Player activePlayer, DiceRoll dice, Bank bank) {
        this(game, activePlayer, dice, bank, new Random());
    }

    Turn(Game game, Player activePlayer, DiceRoll dice, Bank bank, Random random) {
        validateGame(game);
        validatePlayer(activePlayer);
        validateDice(dice);
        validateBank(bank);
        validateRandom(random);

        this.game = game;
        this.activePlayer = activePlayer;
        this.dice = dice;
        this.bank = bank;
        this.random = random;
        this.phase = TurnPhase.PRODUCTION;
        this.buildManager = new BuildManager(game, activePlayer, bank);
        this.resourceProduction = new ResourceProduction();
        this.cardsPurchasedThisTurn = new ArrayList<>();
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

    private void validateRandom(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
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
        Board board = game.getBoard();
        resourceProduction.distributeResources(roll, board.getVertices(), board.getRobberHex(), bank);
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
        List<ResourceType> heldCards = expandHeldResources(player);
        Collections.shuffle(heldCards, random);
        for (int i = 0; i < amount; i++) {
            ResourceType type = heldCards.get(i);
            player.removeResources(type, 1);
            bank.collect(type, 1);
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

        if (target.getTotalResourceCount() > 0) {
            ResourceType stolen = pickResourceToSteal(target);
            target.removeResources(stolen, 1);
            activePlayer.addResources(stolen, 1);
        }
        robberPendingSteal = false;
    }

    private ResourceType pickResourceToSteal(Player target) {
        List<ResourceType> heldCards = expandHeldResources(target);
        return heldCards.get(random.nextInt(heldCards.size()));
    }

    private List<ResourceType> expandHeldResources(Player player) {
        List<ResourceType> cards = new ArrayList<>();
        for (ResourceType type : ResourceType.values()) {
            for (int i = 0; i < player.getResourceCount(type); i++) {
                cards.add(type);
            }
        }
        return cards;
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

    public void advanceToBuild() {
        if (phase != TurnPhase.TRADE) {
            throw new IllegalStateException("Can only advance to the build phase from the trade phase");
        }
        validateRobberResolved();
        expirePendingTrade();
        phase = TurnPhase.BUILD;
    }

    private void expirePendingTrade() {
        if (pendingTrade != null) {
            pendingTrade.reject();
            pendingTrade = null;
        }
    }

    private void validateRobberResolved() {
        if (robberPendingMove || robberPendingSteal) {
            throw new IllegalStateException("Robber must be resolved before performing this action");
        }
    }

    private void validateInTradePhase(String wrongPhaseMessage) {
        if (phase != TurnPhase.TRADE) {
            throw new IllegalStateException(wrongPhaseMessage);
        }
        validateRobberResolved();
    }

    public void endTurn() {
        if (phase != TurnPhase.BUILD) {
            throw new IllegalStateException("Can only end the turn from the build phase");
        }
        phase = TurnPhase.DONE;
    }

    public void buildRoad(int edgeId) {
        validateInBuildPhase();
        buildManager.buildRoad(edgeId);
    }

    public void buildSettlement(int vertexId) {
        validateInBuildPhase();
        buildManager.buildSettlement(vertexId);
    }

    public void buildCity(int vertexId) {
        validateInBuildPhase();
        buildManager.buildCity(vertexId);
    }

    private void validateInBuildPhase() {
        if (phase != TurnPhase.BUILD) {
            throw new IllegalStateException("Build actions are only allowed during the build phase");
        }
    }

    public TradeOffer proposeTrade(Player recipient, Map<ResourceType, Integer> offering, Map<ResourceType, Integer> requesting) {
        validateInTradePhase("Trades can only be proposed during the trade phase");
        validateRecipientInGame(recipient);
        if (pendingTrade != null) {
            throw new IllegalStateException("A trade offer is already pending");
        }

        TradeOffer offer = new TradeOffer(activePlayer, recipient, offering, requesting);
        validateOffererCanAffordOffering(offer.getOffering());
        pendingTrade = offer;
        return offer;
    }

    public void acceptTrade(TradeOffer offer) {
        validateInTradePhase("Trades can only be accepted during the trade phase");
        validateMatchesPendingTrade(offer);

        Player offerer = offer.getOfferer();
        Player recipient = offer.getRecipient();
        validateCanAffordTrade(offerer, offer.getOffering());
        validateCanAffordTrade(recipient, offer.getRequesting());

        exchange(offerer, recipient, offer.getOffering());
        exchange(recipient, offerer, offer.getRequesting());

        offer.accept();
        pendingTrade = null;
    }

    public void rejectTrade(TradeOffer offer) {
        validateInTradePhase("Trades can only be rejected during the trade phase");
        validateMatchesPendingTrade(offer);
        offer.reject();
        pendingTrade = null;
    }

    public Optional<TradeOffer> getPendingTrade() {
        return Optional.ofNullable(pendingTrade);
    }

    private void validateRecipientInGame(Player recipient) {
        if (recipient == null || !game.getPlayers().contains(recipient)) {
            throw new IllegalArgumentException("Recipient must be another player in this game");
        }
    }

    private void validateMatchesPendingTrade(TradeOffer offer) {
        if (pendingTrade == null || offer != pendingTrade) {
            throw new IllegalStateException("There is no matching pending trade offer");
        }
    }

    private void validateOffererCanAffordOffering(Map<ResourceType, Integer> offering) {
        if (!canAfford(activePlayer, offering)) {
            throw new IllegalArgumentException("Active player cannot afford the offered resources");
        }
    }

    private void validateCanAffordTrade(Player player, Map<ResourceType, Integer> resources) {
        if (!canAfford(player, resources)) {
            throw new IllegalStateException("Player cannot afford this trade");
        }
    }

    private boolean canAfford(Player player, Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            if (player.getResourceCount(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private void exchange(Player from, Player to, Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            from.removeResources(entry.getKey(), entry.getValue());
            to.addResources(entry.getKey(), entry.getValue());
        }
    }

    public void submitMaritimeTrade(MaritimeTrade trade) {
        validateInTradePhase("Maritime trades can only be submitted during the trade phase");
        if (trade.getPlayer() != activePlayer) {
            throw new IllegalArgumentException("Maritime trades can only be submitted by the active player");
        }
        if (bank.getResourceCount(trade.getReceiving()) < 1) {
            throw new IllegalStateException("Bank has none of the requested resource");
        }

        Player player = trade.getPlayer();
        ResourceType giving = trade.getGiving();
        ResourceType receiving = trade.getReceiving();
        int amount = trade.getAmount();

        player.removeResources(giving, amount);
        bank.collect(giving, amount);
        bank.deduct(receiving, 1);
        player.addResources(receiving, 1);
    }

    public void buyDevelopmentCard() {
        validateInBuildPhase();
        if (!canAfford(activePlayer, DEVELOPMENT_CARD_COST)) {
            throw new IllegalStateException("Active player cannot afford a development card");
        }
        if (game.getRemainingDevelopmentCardCount() == 0) {
            throw new IllegalStateException("No development cards remain in the deck");
        }

        for (Map.Entry<ResourceType, Integer> entry : DEVELOPMENT_CARD_COST.entrySet()) {
            activePlayer.removeResources(entry.getKey(), entry.getValue());
            bank.collect(entry.getKey(), entry.getValue());
        }

        DevelopmentCard card = game.drawDevelopmentCard();
        game.addDevelopmentCardToHand(activePlayer, card);
        cardsPurchasedThisTurn.add(card);
    }

    public List<DevelopmentCard> getPlayerHand(Player player) {
        return game.getPlayerHand(player);
    }

    public int getRemainingDeckSize() {
        return game.getRemainingDevelopmentCardCount();
    }

    private void validateDevCardPlay(Player player, DevelopmentCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Development card cannot be null");
        }
        if (player != activePlayer) {
            throw new IllegalArgumentException("Only the active player can play a development card");
        }
        if (phase != TurnPhase.TRADE && phase != TurnPhase.BUILD) {
            throw new IllegalStateException("Development cards can only be played during the trade or build phase");
        }
        if (playedDevCardThisTurn) {
            throw new IllegalStateException("Only one development card can be played per turn");
        }
        if (!game.getPlayerHand(player).contains(card)) {
            throw new IllegalArgumentException("Player does not own this development card");
        }
        if (cardsPurchasedThisTurn.contains(card)) {
            throw new IllegalStateException("A development card cannot be played the same turn it was purchased");
        }
        if (card.isPlayed()) {
            throw new IllegalStateException("This development card has already been played");
        }
        if (card.getType() == DevelopmentCardType.VICTORY_POINT) {
            throw new IllegalStateException("Victory Point cards cannot be played");
        }
    }

    private void executeDevCardPlay(DevelopmentCard card) {
        card.markAsPlayed();
        playedDevCardThisTurn = true;
    }

    public void playKnightCard(Player player, DevelopmentCard card) {
        validateDevCardPlay(player, card);
        executeDevCardPlay(card);
        robberPendingMove = true;
    }

    public void playRoadBuildingCard(Player player, DevelopmentCard card, int edgeId1, int edgeId2) {
        validateDevCardPlay(player, card);
        executeDevCardPlay(card);
        buildManager.buildFreeRoad(edgeId1);
        buildManager.buildFreeRoad(edgeId2);
    }

    public void playYearOfPlenty(Player player, DevelopmentCard card, ResourceType r1, ResourceType r2) {
        validateDevCardPlay(player, card);
        executeDevCardPlay(card);
        bank.deduct(r1, 1);
        bank.deduct(r2, 1);
        activePlayer.addResources(r1, 1);
        activePlayer.addResources(r2, 1);
    }

    public void playMonopoly(Player player, DevelopmentCard card, ResourceType resource) {
        validateDevCardPlay(player, card);
        executeDevCardPlay(card);
        for (Player p : game.getPlayers()) {
            if (p != activePlayer) {
                int amount = p.getResourceCount(resource);
                p.removeResources(resource, amount);
                activePlayer.addResources(resource, amount);
            }
        }
    }
}