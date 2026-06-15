package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Player {

    private final PlayerColor color;
    private final String name;
    private Map<ResourceType, Integer> resources;
    private final List<DevelopmentCard> developmentCards;

    @SuppressFBWarnings(value = "CT_CONSTRUCTOR_THROW",
            justification = "Class cannot be final because tests mock it with EasyMock; validation must fail fast on invalid arguments.")
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
        this.developmentCards = new ArrayList<>();
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

    @SuppressFBWarnings(value = "CT_CONSTRUCTOR_THROW",
            justification = "Class cannot be final because tests mock it with EasyMock; validation must fail fast on invalid arguments.")
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
        validateResourceAndAmount(resourceType, amount);
        this.resources.merge(resourceType, amount, Integer::sum);
    }

    public int getTotalResourceCount() {
        return this.resources.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void removeResources(ResourceType resourceType, int amount) {
        validateResourceAndAmount(resourceType, amount);
        if (getResourceCount(resourceType) < amount) {
            throw new IllegalStateException("Amount cannot be greater than current resource count");
        }
        this.resources.put(resourceType, getResourceCount(resourceType) - amount);
    }

    private void validateResourceAndAmount(ResourceType resourceType, int amount) {
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return java.util.Collections.unmodifiableList(developmentCards);
    }

    void addDevelopmentCard(DevelopmentCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Development card cannot be null");
        }
        this.developmentCards.add(card);
    }
}
