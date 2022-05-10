package graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void generateBidirectionalFromSeed() {
        Graph graph = Graph.generateBidirectionalFromSeed(2, 3, 0, 1, 0);
        System.out.println(graph);
    }

    @Test
    void readFromFile() {
    }
}