package graph;

public record Edge(int begVertex, int endVertex, double weight) {

    public int getBegVertex() {
        return begVertex;
    }
    public int getEndVertex() {
        return endVertex;
    }
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return endVertex + " :" + weight;
    }
}
