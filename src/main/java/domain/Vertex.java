package domain;

import java.util.List;

public class Vertex {

    private final int id;
    private Player owner = null;

    public Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices) {
        if (id < 0 || id > 53) {
            throw new IllegalArgumentException("Vertex id must be between 0 and 53");
        }
        if (adjacentHexes == null) {
            throw new IllegalArgumentException("Adjacent hexes cannot be null");
        }
        if (adjacentVertices == null) {
            throw new IllegalArgumentException("Adjacent vertices cannot be null");
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public boolean isOccupied() {
        return owner != null;
    }

    public Player getOwner() {
        return owner;
    }

    public List<Hex> getAdjacentHexes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Vertex> getAdjacentVertices() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
