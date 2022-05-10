package graph;

public class Edge {
    private int endVertex;
    private double weight;

    public Edge(int endVertex, double weight) {
        this.endVertex = endVertex;
        this.weight = weight;
    }

    public int getEndVertex() {
        return endVertex;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "{" + endVertex + ", " + String.format("%.2f", weight) + '}';
    }
}
