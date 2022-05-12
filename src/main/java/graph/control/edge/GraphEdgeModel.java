package graph.control.edge;

public class GraphEdgeModel {
    private final int begVertex;
    private final int endVertex;

    public GraphEdgeModel(int begVertex, int endVertex) {
        this.begVertex = begVertex;
        this.endVertex = endVertex;
    }

    public int getBegVertex() {
        return begVertex;
    }

    public int getEndVertex() {
        return endVertex;
    }
}
