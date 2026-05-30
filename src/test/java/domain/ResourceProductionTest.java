package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceProductionTest {

    private Bank bankWith(ResourceType type, int amount) {
        Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
        for (ResourceType t : ResourceType.values()) {
            resources.put(t, 0);
        }
        resources.put(type, amount);
        return new Bank(resources);
    }

    private Vertex settledVertex(int id, Player owner, List<Hex> hexes) {
        Vertex vertex = new Vertex(id, hexes, new ArrayList<>());
        vertex.setOwner(owner);
        return vertex;
    }

    private Vertex cityVertex(int id, Player owner, List<Hex> hexes) {
        Vertex vertex = settledVertex(id, owner, hexes);
        vertex.upgradeToCity();
        return vertex;
    }

    @Test
    void DistributeResources_RollOf7_NoResourcesDistributed() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 6);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = new Bank();

        new ResourceProduction().distributeResources(7, List.of(vertex), bank);

        assertEquals(0, player.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_RollOf2_SettlementAdjacent_PlayerReceives1() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 2);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 19);

        new ResourceProduction().distributeResources(2, List.of(vertex), bank);

        assertEquals(1, player.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_RollOf12_SettlementAdjacent_PlayerReceives1() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.FIELDS, 12);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.GRAIN, 19);

        new ResourceProduction().distributeResources(12, List.of(vertex), bank);

        assertEquals(1, player.getResourceCount(ResourceType.GRAIN));
    }

    @Test
    void DistributeResources_CityAdjacent_PlayerReceives2() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 6);
        Vertex vertex = cityVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 19);

        new ResourceProduction().distributeResources(6, List.of(vertex), bank);

        assertEquals(2, player.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_BankHas0_NoOneReceives() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 6);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 0);

        new ResourceProduction().distributeResources(6, List.of(vertex), bank);

        assertEquals(0, player.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_UnoccupiedVertexAdjacent_NoResourceDistributed() {
        Hex hex = new Hex(TerrainType.HILLS, 4);
        Vertex vertex = new Vertex(0, List.of(hex), new ArrayList<>());
        Bank bank = bankWith(ResourceType.BRICK, 19);

        new ResourceProduction().distributeResources(4, List.of(vertex), bank);

        assertEquals(19, bank.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_MultiplePlayersAdjacentSameHex_AllReceive() {
        Hex hex = new Hex(TerrainType.HILLS, 8);
        Player alice = new Player("Alice", PlayerColor.RED);
        Player bob = new Player("Bob", PlayerColor.BLUE);
        Vertex v1 = settledVertex(0, alice, List.of(hex));
        Vertex v2 = settledVertex(1, bob, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 19);

        new ResourceProduction().distributeResources(8, List.of(v1, v2), bank);

        assertEquals(1, alice.getResourceCount(ResourceType.BRICK));
        assertEquals(1, bob.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_BankCannotCoverAll_NobodyReceives() {
        Hex hex = new Hex(TerrainType.HILLS, 8);
        Player alice = new Player("Alice", PlayerColor.RED);
        Player bob = new Player("Bob", PlayerColor.BLUE);
        Vertex v1 = settledVertex(0, alice, List.of(hex));
        Vertex v2 = settledVertex(1, bob, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 1);

        new ResourceProduction().distributeResources(8, List.of(v1, v2), bank);

        assertEquals(0, alice.getResourceCount(ResourceType.BRICK));
        assertEquals(0, bob.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_BankHasExactlyEnough_ResourceDistributed() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 6);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 1);

        new ResourceProduction().distributeResources(6, List.of(vertex), bank);

        assertEquals(1, player.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void DistributeResources_BankDeductedAfterDistribution() {
        Player player = new Player("Alice", PlayerColor.RED);
        Hex hex = new Hex(TerrainType.HILLS, 6);
        Vertex vertex = settledVertex(0, player, List.of(hex));
        Bank bank = bankWith(ResourceType.BRICK, 5);

        new ResourceProduction().distributeResources(6, List.of(vertex), bank);

        assertEquals(4, bank.getResourceCount(ResourceType.BRICK));
    }
}
