package graph.control;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import graph.control.edge.EdgeController;
import graph.control.vertex.VertexController;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GraphModel {
    private final ArrayList<VertexController> vertices;
    private final HashMap<Pair<Integer, Integer>, EdgeController> edges;
    private final int width;
    private final int height;
    private final double min;
    private final double max;

    public GraphModel(Graph graph, Color highlightColor, Color defaultColor) {
        this.width = graph.getCols();
        this.height = graph.getRows();
        this.vertices = new ArrayList<>(Collections.nCopies(width * height, null));
        this.edges = new HashMap<>(width * height);

        for (var vertex : graph.getVertices()) {
            if (vertex != null)
                addVertex(vertex, highlightColor, defaultColor);
        }

        double tempMin = Double.POSITIVE_INFINITY;
        double tempMax = Double.NEGATIVE_INFINITY;

        for (var edgesFromVertex : graph.getEdges()) {
            for (var edge : edgesFromVertex) {
                tempMin = Math.min(tempMin, edge.weight());
                tempMax = Math.max(tempMax, edge.weight());
            }
        }
        this.min = tempMin;
        this.max = tempMax;

        // after getting min and max, calculate edge color
        for (var edgesFromVertex : graph.getEdges()) {
            for (var edge : edgesFromVertex) {
                double hue = 1 - ((edge.weight() - min) / (max));
                Color defaultEdgeColor = Color.hsb(360 * hue, 1, 1);
                addEdge(edge, highlightColor, defaultEdgeColor);
            }
        }
    }

    public void addVertex(Vertex vertex, Color highlightColor, Color defaultColor) {
        if (vertices.get(vertex.vertex()) != null) return;
        vertices.set(vertex.vertex(), new VertexController(vertex, highlightColor, defaultColor));
    }

    public VertexController getVertex(int vertex) {
        return vertices.get(vertex);
    }

    private void addEdge(Edge edge, Color highlightColor, Color defaultColor) {
        Pair<Integer, Integer> key = new Pair<>(edge.begVertex(), edge.endVertex());
        if (edges.containsKey(key)) return;
        edges.put(key, new EdgeController(edge, highlightColor, defaultColor));
    }

    public EdgeController getEdge(int begVertex, int endVertex) {
        Pair<Integer, Integer> key = new Pair<>(begVertex, endVertex);
        return edges.get(key);
    }

    public ArrayList<VertexController> getVertices() {
        return vertices;
    }

    public HashMap<Pair<Integer, Integer>, EdgeController> getEdges() {
        return edges;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
