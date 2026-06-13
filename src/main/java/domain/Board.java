package domain;

import java.util.*;

public final class Board {
    private final List<Hex> hexes;
    private final List<Vertex> vertices;
    private final List<Edge> edges;
    private Hex robberHex;

    public Board(List<Hex> hexes) {
        validateHexCount(hexes);
        validateTerrainCount(hexes, TerrainType.DESERT, 1);
        validateTerrainCount(hexes, TerrainType.FIELDS, 4);
        validateTerrainCount(hexes, TerrainType.PASTURE, 4);
        validateTerrainCount(hexes, TerrainType.FOREST, 4);
        validateTerrainCount(hexes, TerrainType.MOUNTAINS, 3);

        this.hexes = new ArrayList<>(hexes);
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.robberHex = findDesertHex(this.hexes);

        initializeVertices();
        initializeEdges();
    }

    Board(List<Hex> hexes, List<Vertex> vertices, List<Edge> edges) {
        this.hexes = new ArrayList<>(hexes);
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.robberHex = findDesertHex(this.hexes);
    }

    private Hex findDesertHex(List<Hex> hexes) {
        return hexes.stream().filter(Hex::isDesert).findFirst().orElse(null);
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
            List<Integer> hexIndices = BoardInitialization.getAdjacentHexIndices(i);
            List<Hex> adjacentHexes = new ArrayList<>();
            for (int hexIndex : hexIndices) {
                adjacentHexes.add(hexes.get(hexIndex));
            }
            vertices.add(new Vertex(i, adjacentHexes, new ArrayList<>()));
        }

        for (int i = 0; i < 54; i++) {
            List<Integer> adjIds = BoardInitialization.getAdjacentVertexIds(i);
            List<Vertex> adjacentVertices = new ArrayList<>();
            for (int adjId : adjIds) {
                adjacentVertices.add(vertices.get(adjId));
            }
            vertices.get(i).setAdjacentVertices(adjacentVertices);
        }

        Map<Integer, HarborType> harborVertices = BoardInitialization.getHarborVertices();
        for (Map.Entry<Integer, HarborType> entry : harborVertices.entrySet()) {
            vertices.get(entry.getKey()).setHarborType(entry.getValue());
        }
    }

    private void initializeEdges() {
        int[][] endpoints = BoardInitialization.getEdgeEndpoints();
        for (int i = 0; i < 72; i++) {
            Vertex v1 = vertices.get(endpoints[i][0]);
            Vertex v2 = vertices.get(endpoints[i][1]);
            edges.add(new Edge(i, v1, v2));
        }
    }

    public List<Hex> getHexes() {
        return new ArrayList<>(hexes);
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
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
        if (vertex.isOccupied()) {
            return false;
        }
        return vertex.getAdjacentVertices().stream().noneMatch(Vertex::isOccupied);
    }

    public boolean isConnectedToPlayer(Vertex vertex, Player player) {
        return edges.stream()
                .filter(e -> e.connectsTo(vertex))
                .anyMatch(e -> e.getOwner().filter(o -> o == player).isPresent());
    }

    public Optional<HarborType> getHarborType(Vertex vertex) {
        return vertex.getHarborType();
    }

    public Hex getRobberHex() {
        return robberHex;
    }

    public void setRobberHex(Hex hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Robber hex cannot be null");
        }
        if (!hexes.contains(hex)) {
            throw new IllegalArgumentException("Hex must belong to this board");
        }
        this.robberHex = hex;
    }
}
