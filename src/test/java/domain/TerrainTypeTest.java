package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TerrainTypeTest {

    @Test
    void GetResourceType_ForHills_ReturnsBrick() {
        assertEquals(ResourceType.BRICK, TerrainType.HILLS.getResourceType());
    }

    @Test
    void GetResourceType_ForForest_ReturnsLumber() {
        assertEquals(ResourceType.LUMBER, TerrainType.FOREST.getResourceType());
    }

    @Test
    void GetResourceType_ForMountains_ReturnsOre() {
        assertEquals(ResourceType.ORE, TerrainType.MOUNTAINS.getResourceType());
    }

    @Test
    void GetResourceType_ForFields_ReturnsGrain() {
        assertEquals(ResourceType.GRAIN, TerrainType.FIELDS.getResourceType());
    }

    @Test
    void GetResourceType_ForPasture_ReturnsWool() {
        assertEquals(ResourceType.WOOL, TerrainType.PASTURE.getResourceType());
    }

    @Test
    void GetResourceType_ForDesert_ThrowsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> TerrainType.DESERT.getResourceType());
    }
}
