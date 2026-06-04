package domain;

public class MaritimeTrade {
    private final Player player;
    private final ResourceType giving;
    private final int amount;
    private final ResourceType receiving;

    public MaritimeTrade(Player player, ResourceType giving, int amount,
                         ResourceType receiving, Board board) {

        validateGivingAndReceivingResources(giving, receiving);

        validateGivingAmountNeeded(player, giving, amount);

        this.player = player;
        this.giving = giving;
        this.amount = amount;
        this.receiving = receiving;
    }

    private void validateGivingAndReceivingResources(ResourceType giving, ResourceType receiving) {
        if (giving == receiving) {
            throw new IllegalArgumentException("Giving and receiving resources must be different.");
        }
    }

    private void validateGivingAmountNeeded(Player player, ResourceType giving, int amount) {
        if (player.getResourceCount(giving) < amount) {
            throw new IllegalArgumentException(
                    "Player does not have enough " + giving + " to complete this trade.");
        }
    }

}
