package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Vertex {

    private final int id;
    private Player owner = null;
    private boolean city = false;
    private final List<Hex> adjacentHexes;
    private List<Vertex> adjacentVertices;
    private HarborType harborType = null;

    public Vertex(int id, List<Hex> adjacentHexes, List<Vertex> adjacentVertices) {
        validateId(id);
        validateAdjacentHexes(adjacentHexes);
        validateAdjacentVertices(adjacentVertices);
        this.id = id;
        this.adjacentHexes = new ArrayList<>(adjacentHexes);
        this.adjacentVertices = new ArrayList<>(adjacentVertices);
    }

    private void validateId(int id) {
        if (id < 0 || id > 53) {
            throw new IllegalArgumentException("Vertex id must be between 0 and 53");
        }
    }

    private void validateAdjacentHexes(List<Hex> adjacentHexes) {
        if (adjacentHexes == null) {
            throw new IllegalArgumentException("Adjacent hexes cannot be null");
        }
    }

    private void validateAdjacentVertices(List<Vertex> adjacentVertices) {
        if (adjacentVertices == null) {
            throw new IllegalArgumentException("Adjacent vertices cannot be null");
        }
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return owner != null;
    }

    public Optional<Player> getOwner() {
        return Optional.ofNullable(owner);
    }

    // Player is intentionally mutable (resources change during gameplay).
    // Defensive copying would break object identity. Suppressing EI2 by design.
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setOwner(Player player) {
        this.owner = player;
    }

    public List<Hex> getAdjacentHexes() {
        return new ArrayList<>(adjacentHexes);
    }

    public List<Vertex> getAdjacentVertices() {
        return new ArrayList<>(adjacentVertices);
    }

    void setAdjacentVertices(List<Vertex> adjacentVertices) {
        this.adjacentVertices = new ArrayList<>(adjacentVertices);
    }
  
    public boolean isCity() {
        return city;
    }

    public void upgradeToCity() {
        if (!isOccupied()) {
            throw new IllegalStateException("Cannot upgrade an unoccupied vertex to a city");
        }
        this.city = true;
    }

    public Optional<HarborType> getHarborType() {
        return Optional.ofNullable(harborType);
    }

    void setHarborType(HarborType harborType) {
        this.harborType = harborType;
    }
}
