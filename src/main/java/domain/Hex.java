package domain;

public class Hex {
    private final TerrainType terrainType;

    public Hex(TerrainType terrainType, int numToken) {

        this.terrainType = terrainType;

    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public boolean isDesert() {
        return terrainType == TerrainType.DESERT;
    }
}