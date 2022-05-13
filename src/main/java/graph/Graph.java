package graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

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

    public static Graph readFromFile(String filepath) throws Exception {
        Scanner lineScanner = new Scanner(new File(filepath));
        int rows, cols;
        rows = lineScanner.nextInt();
        cols = lineScanner.nextInt();
        if (rows < 1 || cols < 1)
            throw new Exception("rows < 1 || cols < 1"); // TODO add custom exception
        // go to another line
        lineScanner.nextLine();

        Graph graph = new Graph(rows, cols);

        int begVertex = 0;
        while (lineScanner.hasNext() && begVertex < rows * cols) {
            Scanner scanner = new Scanner(lineScanner.nextLine());
            // skip empty spaces && colons
            scanner.useDelimiter("(\\s*:\\s*|\\s+)");

            while (scanner.hasNext()) {
                int endVertex = scanner.nextInt();
                // for some reason scanner.nextDouble does not work
                double weight = Double.parseDouble(scanner.next());
                graph.addDirectedEdge(begVertex, endVertex, weight);

            }
            begVertex++;
            scanner.close();
        }

        // check if there are extra tokens at the end of file
        if (begVertex + 1 != rows * cols || lineScanner.hasNext())
            throw new Exception("extra values at the end of file");

        lineScanner.close();
        return graph;
    }

    public static void writeToFile(Graph graph, String filepath) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        writer.write(graph.toString());
        writer.close();
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
        builder.append(this.getRows()).append(" ").append(this.getCols()).append("\n");

        for (int i = 0; i < this.getCols() * this.getRows(); i++) {
            String edges = this.edges.get(i)
                    .toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", " ");
            builder.append("\t").append(edges).append("\n");
        }
        return builder.toString();
    }
}
