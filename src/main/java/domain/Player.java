package domain;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final PlayerColor color;
    private final String name;
    private Map<ResourceType, Integer> resources;

    public Player(String name, PlayerColor color) {
        if (name == null) {
            throw new IllegalArgumentException("Player name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be an empty string");
        }
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Player name cannot exceed 50 characters");
        }
        this.name = name;
        this.color = color;
        this.resources = new HashMap<>(Map.of(
                ResourceType.BRICK, 0,
                ResourceType.LUMBER, 0,
                ResourceType.ORE, 0,
                ResourceType.GRAIN, 0,
                ResourceType.WOOL, 0
        ));
    }

    Player(String name, PlayerColor color, Map<ResourceType, Integer> customResources) {
        this.name = name;
        this.color = color;
        this.resources = new HashMap<>(customResources);
    }

    public String getName() {
        return this.name;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public int getResourceCount(ResourceType resourceType) {
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        return this.resources.get(resourceType);
    }

    public void addResources(ResourceType resourceType, int amount) {
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (amount < 0 || amount > 1) {
            throw new IllegalArgumentException("Amount must be 0 or 1");
        }
        this.resources.merge(resourceType, amount, Integer::sum);
    }

    public int getTotalResourceCount() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
