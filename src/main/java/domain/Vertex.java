package domain;

import java.util.List;

public class Vertex {
    private final int id;
    private final List<Vertex> adjacentVertices;

    public Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices) {
        this.id = id;
        this.adjacentVertices = adjacentVertices;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Player getOwner() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Hex> getAdjacentHexes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Vertex> getAdjacentVertices() {
        return adjacentVertices;
    }
}
