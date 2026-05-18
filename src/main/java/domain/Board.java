package domain;

import java.util.List;

public class Board {

    public Board(List<Hex> hexes) {
        if (hexes.size() != 19) {
            throw new IllegalArgumentException("Board must have exactly 19 hexes");
        }
        long desertCount = hexes.stream().filter(Hex::isDesert).count();
        if (desertCount != 1) {
            throw new IllegalArgumentException("Board must have exactly 1 desert hex");
        }
    }

    public List<Hex> getHexes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Vertex> getVertices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Edge> getEdges() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Vertex getVertex(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Edge getEdge(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getHexCount(TerrainType terrainType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean satisfiesDistanceRule(Vertex vertex) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isConnectedToPlayer(Vertex vertex, Player player) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
