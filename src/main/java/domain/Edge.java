package domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;
import java.util.Optional;

public final class Edge {

    private final int id;
    private Player owner = null;
    private final Vertex[] endpoints;

    public Edge(int id, Vertex endpoint1, Vertex endpoint2) {
        validateId(id);
        validateEndpoints(endpoint1, endpoint2);
        this.id = id;
        this.endpoints = new Vertex[]{endpoint1, endpoint2};
    }

    private void validateId(int id) {
        if (id < 0 || id > 71) {
            throw new IllegalArgumentException("Edge id must be between 0 and 71");
        }
    }

    private void validateEndpoints(Vertex endpoint1, Vertex endpoint2) {
        if (endpoint1 == null || endpoint2 == null) {
            throw new IllegalArgumentException("Endpoints cannot be null");
        }
    }

    public int getId() {
        return id;
    }

    public boolean hasRoad() {
        return owner != null;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Player is intentionally mutable; defensive copying would break object identity")
    public void setOwner(Player player) {
        this.owner = player;
    }

    public Optional<Player> getOwner() {
        return Optional.ofNullable(owner);
    }

    public List<Vertex> getEndpoints() {
        return List.of(endpoints[0], endpoints[1]);
    }

    public boolean connectsTo(Vertex vertex) {
        return endpoints[0] == vertex || endpoints[1] == vertex;
    }
}
