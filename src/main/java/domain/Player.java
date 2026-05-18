package domain;

public class Player {

    private final PlayerColor color;
    private final String name;

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
     }

    public String getName() {
        return this.name;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public int getResourceCount(ResourceType resourceType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void addResources(ResourceType resourceType, int amount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getTotalResourceCount() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
