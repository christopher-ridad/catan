package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Hex {
    private final TerrainType terrainType;
    private final int numToken;

    @SuppressFBWarnings(value = "CT_CONSTRUCTOR_THROW",
            justification = "Class cannot be final because tests mock it with EasyMock; validation must fail fast on invalid arguments.")
    public Hex(TerrainType terrainType, int numToken) {
        validateTerrainType(terrainType, numToken);
        validateNumToken(terrainType, numToken);
        this.terrainType = terrainType;
        this.numToken = numToken;
    }

    private void validateTerrainType(TerrainType terrainType, int numToken) {
        if (terrainType == TerrainType.DESERT && numToken != 0) {
            throw new IllegalArgumentException("Desert hex cannot have a number token");
        }
    }

    private void validateNumToken(TerrainType terrainType, int numToken) {
        if (terrainType != TerrainType.DESERT && (numToken == 7 || numToken > 12 || numToken < 2)) {
            throw new IllegalArgumentException("Invalid number token: " + numToken);
        }
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public int getNumberToken() {
        return numToken;
    }

    public boolean isDesert() {
        return terrainType == TerrainType.DESERT;
    }

    public boolean producesResource() {
        return terrainType != TerrainType.DESERT;
    }
}