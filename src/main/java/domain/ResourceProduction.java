package domain;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourceProduction {

    public void distributeResources(int roll, List<Vertex> vertices, Bank bank) {
        if (vertices == null) {
            throw new IllegalArgumentException("Vertices cannot be null");
        }
        if (bank == null) {
            throw new IllegalArgumentException("Bank cannot be null");
        }
        if (roll == 7) {
            return;
        }
        Map<ResourceType, Integer> totalNeeded = new EnumMap<>(ResourceType.class);
        Map<Player, Map<ResourceType, Integer>> playerGains = new LinkedHashMap<>();
        collectGains(roll, vertices, totalNeeded, playerGains);
        distributeToPlayers(totalNeeded, playerGains, bank);
    }

    private void collectGains(int roll, List<Vertex> vertices,
            Map<ResourceType, Integer> totalNeeded,
            Map<Player, Map<ResourceType, Integer>> playerGains) {
        for (Vertex vertex : vertices) {
            if (!vertex.isOccupied()) {
                continue;
            }
            Player owner = vertex.getOwner().get();
            int amount = vertex.isCity() ? 2 : 1;
            for (Hex hex : vertex.getAdjacentHexes()) {
                if (hex.getNumberToken() == roll && hex.producesResource()) {
                    ResourceType resource = hex.getTerrainType().getResourceType();
                    totalNeeded.merge(resource, amount, Integer::sum);
                    playerGains
                        .computeIfAbsent(owner, p -> new EnumMap<>(ResourceType.class))
                        .merge(resource, amount, Integer::sum);
                }
            }
        }
    }

    private void distributeToPlayers(
            Map<ResourceType, Integer> totalNeeded,
            Map<Player, Map<ResourceType, Integer>> playerGains,
            Bank bank) {
        for (ResourceType resource : totalNeeded.keySet()) {
            int needed = totalNeeded.get(resource);
            int available = bank.getResourceCount(resource);
            if (available < needed) {
                long affectedCount = playerGains.values().stream()
                        .filter(m -> m.containsKey(resource)).count();
                if (affectedCount > 1) {
                    continue;
                }
                giveRemainingToSinglePlayer(resource, available, playerGains, bank);
                continue;
            }
            for (Map.Entry<Player, Map<ResourceType, Integer>> entry : playerGains.entrySet()) {
                Integer gain = entry.getValue().get(resource);
                if (gain != null) {
                    entry.getKey().addResources(resource, gain);
                }
            }
            bank.deduct(resource, needed);
        }
    }

    private void giveRemainingToSinglePlayer(
            ResourceType resource, int available,
            Map<Player, Map<ResourceType, Integer>> playerGains,
            Bank bank) {
        for (Map.Entry<Player, Map<ResourceType, Integer>> entry : playerGains.entrySet()) {
            if (entry.getValue().containsKey(resource)) {
                entry.getKey().addResources(resource, available);
            }
        }
        bank.deduct(resource, available);
    }
}
