package graph;

public record Edge(int begVertex, int endVertex, double weight) {
    @Override
    public String toString() { return begVertex + " -> " + endVertex + " : " + weight; }
}
