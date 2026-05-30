package domain;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourceProduction {

    public void distributeResources(int roll, List<Vertex> vertices, Bank bank) {
        if (roll == 7) {
            return;
        }

        Map<ResourceType, Integer> totalNeeded = new EnumMap<>(ResourceType.class);
        Map<Player, Map<ResourceType, Integer>> playerGains = new LinkedHashMap<>();

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

        for (ResourceType resource : totalNeeded.keySet()) {
            int needed = totalNeeded.get(resource);
            if (bank.getResourceCount(resource) < needed) {
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
}

