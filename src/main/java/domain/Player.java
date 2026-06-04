package domain;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final PlayerColor color;
    private final String name;
    private Map<ResourceType, Integer> resources;

    public Player(String name, PlayerColor color) {
        validateName(name);
        validateColor(color);
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

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Player name cannot be null");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be an empty string");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Player name cannot exceed 50 characters");
        }
    }

    private void validateColor(PlayerColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
    }

    // package private constructor for testing
    Player(String name, PlayerColor color, Map<ResourceType, Integer> customResources) {
        this(name, color);
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
        ValidateResourceAndAmount(resourceType, amount);
        this.resources.merge(resourceType, amount, Integer::sum);
    }

    public int getTotalResourceCount() {
        return this.resources.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void removeResources(ResourceType resourceType, int amount) {
        ValidateResourceAndAmount(resourceType, amount);
        if (getResourceCount(resourceType) < amount) {
            throw new IllegalStateException("Amount cannot be greater than current resource count");
        }
        this.resources.put(resourceType, getResourceCount(resourceType) - amount);
    }

    private void ValidateResourceAndAmount(ResourceType resourceType, int amount) {
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}
