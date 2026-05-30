package domain;

import java.util.EnumMap;
import java.util.Map;

public class Bank {

    private static final int STANDARD_RESOURCE_COUNT = 19;
    private final Map<ResourceType, Integer> resources;

    public Bank() {
        this.resources = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, STANDARD_RESOURCE_COUNT);
        }
    }

    Bank(Map<ResourceType, Integer> initialResources) {
        this.resources = new EnumMap<>(initialResources);
    }

    public int getResourceCount(ResourceType type) {
        if (type == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        return resources.get(type);
    }

    public void deduct(ResourceType type, int amount) {
        if (type == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        int current = resources.get(type);
        if (amount > current) {
            throw new IllegalArgumentException("Insufficient resources in bank");
        }
        resources.put(type, current - amount);
    }
}
