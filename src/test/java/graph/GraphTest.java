package graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void generateBidirectionalFromSeed_thenSuccess() {
        Graph graph = Graph.generateBidirectionalFromSeed(2, 3, 0, 1, 0);
        System.out.println(graph);
    }

    @Test
    void readFromFile_thenSuccess() {
        String filepath = "src/test/resources/example_graph.txt";
        assertDoesNotThrow(() -> {
            Graph graph = Graph.readFromFile(filepath);
            System.out.println(graph);
        });
    }
}