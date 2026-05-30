package domain;

public enum TerrainType {
    HILLS, FOREST, MOUNTAINS, FIELDS, PASTURE, DESERT;

    public ResourceType getResourceType() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
