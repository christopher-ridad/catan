package domain;

public class Edge {

    private final int id;
    private Player owner = null;
    private final Vertex[] endpoints;

    public Edge(int id, Vertex endpoint1, Vertex endpoint2) {
        if (id < 0 || id > 71) {
            throw new IllegalArgumentException("Edge id must be between 0 and 71");
        }
        if (endpoint1 == null) {
            throw new IllegalArgumentException("Endpoints cannot be null");
        }
        if (endpoint2 == null) {
            throw new IllegalArgumentException("Endpoints cannot be null");
        }
        this.id = id;
        this.endpoints = new Vertex[]{endpoint1, endpoint2};
    }

    public int getId() {
        return id;
    }

    public boolean hasRoad() {
        return owner != null;
    }

    public void setOwner (Player player) {
        this.owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    public Vertex[] getEndpoints() {
        return endpoints;
    }

    public boolean connectsTo(Vertex vertex) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
