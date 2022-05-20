package graph;

public record Vertex(int vertex) {
    @Override
    public String toString() { return Integer.toString(vertex); }
}
