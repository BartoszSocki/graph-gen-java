package graph.control.vertex;

public class GraphVertexModel {
    private final int vertex;

    public GraphVertexModel(int vertex) {
        this.vertex = vertex;
    }

    public int getVertex() {
        return vertex;
    }

    @Override
    public String toString() {
        return Integer.toString(vertex);
    }
}
