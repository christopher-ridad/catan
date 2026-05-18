package domain;

public class Player {

    public Player(String name, PlayerColor color) {
        if (name == null || name.isBlank() || color == null) {
            throw new IllegalArgumentException("Player name cannot be null");
        }
    }

    public String getName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public PlayerColor getColor() {
        throw new UnsupportedOperationException("Not implemented yet");
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
