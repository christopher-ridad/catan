package domain;

import java.util.List;

public class MaritimeTrade {
    private final Player player;
    private final ResourceType giving;
    private final int amount;
    private final ResourceType receiving;
    private final int rate;

    public MaritimeTrade(Player player, ResourceType giving, int amount,
                         ResourceType receiving, Board board) {

        validateGivingAndReceivingResources(giving, receiving);

        this.rate = computeBestRate(player, giving, board);

        validateAmountVsRate(rate, amount);

        validateGivingAmountNeeded(player, giving, amount);

        this.player = player;
        this.giving = giving;
        this.amount = amount;
        this.receiving = receiving;
    }

    public Player getPlayer() {
          return player;
    }

    public ResourceType getGiving() {
        return giving;
    }

    public int getAmount() {
        return amount;
    }

    public ResourceType getReceiving() {
        return receiving;
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

    private int computeBestRate(Player player, ResourceType giving, Board board) {
        int bestRate = 4;

        List<Vertex> vertices = board.getVertices();
        for (Vertex vertex : vertices) {
            if (vertex.getOwner().filter(o -> o == player).isEmpty()) {
                continue;
            }
            if (vertex.getHarborType().isEmpty()) {
                continue;
            }

            HarborType harbor = vertex.getHarborType().get();

            if (harbor == HarborType.GENERIC) {
                bestRate = Math.min(bestRate, 3);
            } else if (resourceMatchesHarbor(giving, harbor)) {
                bestRate = Math.min(bestRate, 2);
            }

            if (bestRate == 2) {
                break;
            }
        }

        return bestRate;
    }

    private boolean resourceMatchesHarbor(ResourceType resource, HarborType harbor) {
        switch (resource) {
            case BRICK:  return harbor == HarborType.BRICK;
            case LUMBER: return harbor == HarborType.LUMBER;
            case ORE:    return harbor == HarborType.ORE;
            case GRAIN:  return harbor == HarborType.GRAIN;
            case WOOL:   return harbor == HarborType.WOOL;
            default:     throw new IllegalArgumentException("Unknown resource type: " + resource);
        }
    }

    private void  validateAmountVsRate(int rate, int amount) {
        if (amount != rate) {
            throw new IllegalArgumentException(
                    "Amount must equal the player's best available rate of " + rate + " for " + giving + ".");
        }
    }
}
