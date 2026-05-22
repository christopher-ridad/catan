package domain;

public class Edge {

    public Edge(int id, Vertex endpoint1, Vertex endpoint2) {
        if (id < 0 || id > 71) {
            throw new IllegalArgumentException("Edge id must be between 0 and 71");
        }
    }

    public int getId() {
        throw new UnsupportedOperationException("Not implemented yet");
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
