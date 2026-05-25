package domain;

public class Hex {
    private final TerrainType terrainType;
    private final int numToken;

    public Hex(TerrainType terrainType, int numToken) {
        if (terrainType == TerrainType.DESERT && numToken != 0) {
            throw new IllegalArgumentException("Desert hex cannot have a number token");
        }
        if (terrainType != TerrainType.DESERT && (numToken == 7 || numToken > 12 || numToken < 2)) {
            throw new IllegalArgumentException("Invalid number token: " + numToken);
        }
        this.terrainType = terrainType;
        this.numToken = numToken;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public int getNumberToken() { return numToken; }

    public boolean isDesert() {
        return terrainType == TerrainType.DESERT;
    }

    public boolean producesResource() {
        return terrainType != TerrainType.DESERT;
    }
}