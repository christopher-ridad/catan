package domain;

public class Hex {
    private final TerrainType terrainType;

    public Hex(TerrainType terrainType, int numToken) {
        if (terrainType == TerrainType.DESERT && numToken != 0) {
            throw new IllegalArgumentException("Desert hex cannot have a number token");
        }
        if (terrainType != TerrainType.DESERT && (numToken == 7 || numToken > 12)) {
            throw new IllegalArgumentException("Invalid number token: " + numToken);
        }
        this.terrainType = terrainType;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public boolean isDesert() {
        return terrainType == TerrainType.DESERT;
    }
}