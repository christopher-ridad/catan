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
        throw new UnsupportedOperationException("Not implemented");
    }

    public void deduct(ResourceType type, int amount) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
