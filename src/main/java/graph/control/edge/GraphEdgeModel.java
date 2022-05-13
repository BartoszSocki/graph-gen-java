package graph.control.edge;

public class GraphEdgeModel {
    private final int begVertex;
    private final int endVertex;
    private final double weight;

    public GraphEdgeModel(int begVertex, int endVertex, double weight) {
        this.begVertex = begVertex;
        this.endVertex = endVertex;
        this.weight = weight;
    }

    public int getBegVertex() {
        return begVertex;
    }

    public int getEndVertex() {
        return endVertex;
    }

    public double getWeight() {
        return weight;
    }
}
