package graph.control;

import graph.control.edge.GraphEdge;
import graph.control.vertex.GraphVertex;

import java.util.ArrayList;

public class GraphModel {
    private final ArrayList<GraphVertex> vertices;
    private final ArrayList<GraphEdge> edges;
    private final int width;
    private final int height;
    private final int size;

    public GraphModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;

        this.vertices = new ArrayList<>(this.size);
        this.edges = new ArrayList<>();
    }

    public void addVertex(int vertex) {
        this.vertices.add(vertex, new GraphVertex(vertex));
    }

    public void addEdge(int begVertex, int endVertex) {
        this.edges.add(new GraphEdge(begVertex, endVertex));
    }

    public ArrayList<GraphVertex> getVertices() {
        return vertices;
    }

    public ArrayList<GraphEdge> getEdges() {
        return edges;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSize() {
        return size;
    }
}
