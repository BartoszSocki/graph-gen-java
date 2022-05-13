package graph.control;

import graph.Graph;
import graph.control.edge.GraphEdge;
import graph.control.vertex.GraphVertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GraphModel {
    private final ArrayList<GraphVertex> vertices;
    private final HashMap<Long, GraphEdge> edges;
    private final int width;
    private final int height;
    private final int size;

    public GraphModel(Graph graph) {
        this(graph.getCols(), graph.getRows());
        for (int i = 0; i < graph.getEdges().size(); i++) {
//            if (graph.getEdges().get(i).size() != 0)
//                addVertex(i);

            for (var edge : graph.getEdges().get(i)) {
                addEdge(i, edge.getEndVertex());

                if (getVertex(edge.getEndVertex()) == null)
                    addVertex(edge.getEndVertex());
            }
        }
    }

    public GraphModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;

        this.vertices = new ArrayList<>(Collections.nCopies(this.size, null));
        this.edges = new HashMap<>();
    }

    public void addVertex(int vertex) {
        vertices.set(vertex, new GraphVertex(vertex));
    }

    public GraphVertex getVertex(int vertex) {
        return vertices.get(vertex);
    }

    public void addEdge(int begVertex, int endVertex) {
        long key = pairMap(begVertex, endVertex);
        if (edges.containsKey(key))
            return;
        edges.put(key, new GraphEdge(begVertex, endVertex));
    }

    public GraphEdge getEdge(int begVertex, int endVertex) {
        long key = pairMap(begVertex, endVertex);
        return edges.get(key);
    }

    public ArrayList<GraphVertex> getVertices() {
        return vertices;
    }

    public HashMap<Long, GraphEdge> getEdges() {
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

    // maps two ints to one long, useful when dealing with HashMap
    private long pairMap(int a, int b) {
        long value = (long)(0.5 * (a + b + 1) * (a + b + 2));
        return value + Math.min(a, b);
    }
}
