package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Graph {
    private final int rows;
    private final int cols;
    private ArrayList<LinkedList<Edge>> edges;

    public boolean isVertexOutOfBounds(int vertex) {
        return vertex < 0 && vertex >= this.rows * this.cols;
    }

    private static double uniformRandom(Random random, double min, double max) {
        return (random.nextDouble() * (max - min)) + min;
    }

    private void addDirectedEdge(int begVertex, int endVertex, double weight) {
        if (this.isVertexOutOfBounds(begVertex) || this.isVertexOutOfBounds(endVertex))
            throw new IndexOutOfBoundsException("vertex out of bound");

        this.edges.get(begVertex).add(new Edge(endVertex, weight));
    }

    public int xyToIndex(int row, int col) {
        return row * this.cols + col;
    }

    public static Graph generateBidirectionalFromSeed(int rows, int cols, double min, double max, long seed) {
        if (rows < 0 || cols < 0)
            throw new IllegalArgumentException("rows and cols cannot be less than 0");
        if (min > max)
            throw new IllegalArgumentException("min cannot be greater than max");

        Graph graph = new Graph(rows, cols);
        Random random = new Random(seed);

        for (int i = 0; i < graph.getRows() - 1; i++) {
            for (int j = 0; j < graph.getCols(); j++) {
                double weight = uniformRandom(random, min, max);
                graph.addDirectedEdge(graph.xyToIndex(i, j), graph.xyToIndex(i + 1, j), weight);
                graph.addDirectedEdge(graph.xyToIndex(i + 1, j), graph.xyToIndex(i, j), weight);
            }
        }

        for (int i = 0; i < graph.getRows(); i++) {
            for (int j = 0; j < graph.getCols() - 1; j++) {
                double weight = uniformRandom(random, min, max);
                graph.addDirectedEdge(graph.xyToIndex(i, j), graph.xyToIndex(i, j + 1), weight);
                graph.addDirectedEdge(graph.xyToIndex(i, j + 1), graph.xyToIndex(i, j), weight);
            }
        }

        return graph;
    }

    public static Graph readFromFile() {
        return null;
    }

    private Graph(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.edges = new ArrayList<>(rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            this.edges.add(i, new LinkedList<>());
        }

    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<LinkedList<Edge>> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.getCols() * this.getRows(); i++) {
            builder.append(i)
                    .append(": ")
                    .append(this.edges.get(i))
                    .append("\n");
        }
        return builder.toString();
    }
}