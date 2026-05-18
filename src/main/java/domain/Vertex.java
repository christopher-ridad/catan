package domain;

import java.util.List;

public class Vertex {
    private final int id;

    public Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices) {
        this.id = id;
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
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
