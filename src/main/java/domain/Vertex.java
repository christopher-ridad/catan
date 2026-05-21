package domain;

import java.util.List;

public class Vertex {

    public Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices) {
        if (id < 0) {
            throw new IllegalArgumentException("Vertex id must be between 0 and 53");
        }
    }

    public int getId() {
        throw new UnsupportedOperationException("Not implemented yet");
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
