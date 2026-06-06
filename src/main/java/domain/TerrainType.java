package domain;

public enum TerrainType {
    HILLS, FOREST, MOUNTAINS, FIELDS, PASTURE, DESERT;

    public ResourceType getResourceType() {
        switch (this) {
            case HILLS: return ResourceType.BRICK;
            case FOREST: return ResourceType.LUMBER;
            case MOUNTAINS: return ResourceType.ORE;
            case FIELDS: return ResourceType.GRAIN;
            case PASTURE: return ResourceType.WOOL;
            default: throw new IllegalStateException("DESERT does not produce a resource");
        }
    }
}
