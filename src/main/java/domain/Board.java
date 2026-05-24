package domain;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Hex> hexes;
    private final List<Vertex> vertices;
    private final List<Edge> edges;

    public Board(List<Hex> hexes) {
        validateHexCount(hexes);
        validateTerrainCount(hexes, TerrainType.DESERT, 1);
        validateTerrainCount(hexes, TerrainType.FIELDS, 4);
        validateTerrainCount(hexes, TerrainType.PASTURE, 4);
        validateTerrainCount(hexes, TerrainType.FOREST, 4);
        validateTerrainCount(hexes, TerrainType.MOUNTAINS, 3);

        this.hexes = hexes;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();

        initializeVertices();
        initializeEdges();
    }

    Board(List<Hex> hexes, List<Vertex> vertices, List<Edge> edges) {
        this.hexes = hexes;
        this.vertices = vertices;
        this.edges = edges;
    }

    private void validateHexCount(List<Hex> hexes) {
        if (hexes.size() != 19) {
            throw new IllegalArgumentException("Board must have exactly 19 hexes");
        }
    }

    private void validateTerrainCount(List<Hex> hexes, TerrainType terrainType, int expected) {
        long count = hexes.stream().filter(h -> h.getTerrainType() == terrainType).count();
        if (count != expected) {
            throw new IllegalArgumentException("Board must have exactly " + expected + " " + terrainType + " hexes");
        }
    }

    private void initializeVertices() {
        for (int i = 0; i < 54; i++) {
            vertices.add(new Vertex(i, new ArrayList<>(), new ArrayList<>()));
        }
    }

    private void initializeEdges() {
        for (int i = 0; i < 72; i++) {
            edges.add(new Edge(i, vertices.get(0), vertices.get(1)));
        }
    }

    public List<Hex> getHexes() {
        return hexes;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Vertex getVertex(int id) {
        if (id < 0 || id > 53) {
            throw new IllegalArgumentException("Vertex id must be between 0 and 53");
        }
        return vertices.get(id);
    }

    public Edge getEdge(int id) {
        if (id < 0 || id > 71) {
            throw new IllegalArgumentException("Edge id must be between 0 and 71");
        }
        return edges.get(id);
    }

    public int getHexCount(TerrainType terrainType) {
        return (int) hexes.stream().filter(h -> h.getTerrainType() == terrainType).count();
    }

    public boolean satisfiesDistanceRule(Vertex vertex) {
        return vertex.getAdjacentVertices().stream().noneMatch(Vertex::isOccupied);
    }

    public boolean isConnectedToPlayer(Vertex vertex, Player player) {
        return edges.stream()
                .filter(e -> e.connectsTo(vertex))
                .anyMatch(e -> e.hasRoad() && e.getOwner().get() == player);
    }
}
