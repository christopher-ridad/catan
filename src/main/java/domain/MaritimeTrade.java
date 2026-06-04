package domain;

public class MaritimeTrade {
    private final Player player;
    private final ResourceType giving;
    private final int amount;
    private final ResourceType receiving;

    public MaritimeTrade(Player player, ResourceType giving, int amount,
                         ResourceType receiving, Board board) {

        validateGivingAndReceivingResources(giving, receiving);

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

}
