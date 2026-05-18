package domain;

public class Edge {
    private final int id;

    public Edge(int id, Vertex endpoint1, Vertex endpoint2) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean hasRoad() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Player getOwner() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Vertex[] getEndpoints() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean connectsTo(Vertex vertex) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
