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

    public String getName() {
        return this.name;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public int getResourceCount(ResourceType resourceType) {
        return this.resources.get(resourceType);
    }

    public void addResources(ResourceType resourceType, int amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getTotalResourceCount() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
