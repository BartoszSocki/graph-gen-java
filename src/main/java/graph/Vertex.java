package graph;

public record Vertex(int vertex) {
    public int getVertex() {
        return vertex;
    }

    @Override
    public String toString() { return Integer.toString(vertex); }
}
