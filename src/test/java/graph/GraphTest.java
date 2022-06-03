package graph;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void generateBidirectionalGraph_withCorrectArguments_thenSuccess() {
        assertDoesNotThrow(() -> {
            Graph graph = Graph.generateBidirectionalFromSeed(2, 3, 0, 1, 0);
        });
    }

    @Test
    void generateBidirectionalGraph_withInvalidArguments_thenThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            Graph graph = Graph.generateBidirectionalFromSeed(0, 3, 0, 1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Graph graph = Graph.generateBidirectionalFromSeed(2, 0, 0, 1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Graph graph = Graph.generateBidirectionalFromSeed(2, 3, 3, 1, 0);
        });
    }

    @Test
    void generateBidirectionalGraphs_fromDifferentSeeds_thenNotEquals() {
        Graph graph1 = Graph.generateBidirectionalFromSeed(3, 3, 0, 1, 0);
        Graph graph2 = Graph.generateBidirectionalFromSeed(3, 3, 0, 1, 1);

        assertNotEquals(graph1.toString(), graph2.toString());
    }

    @Test
    void checkIfVertexBelongsToGraph() {
        Graph graph = Graph.generateBidirectionalFromSeed(2, 3, 0, 1, 0);
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(0, 0)));
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(0, 1)));
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(0, 2)));
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(1, 0)));
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(1, 1)));
        assertFalse(graph.isVertexOutOfBounds(graph.xyToIndex(1, 2)));

        assertTrue(graph.isVertexOutOfBounds(graph.xyToIndex(-1, -1)));
        assertTrue(graph.isVertexOutOfBounds(graph.xyToIndex(-1, 0)));
        assertTrue(graph.isVertexOutOfBounds(graph.xyToIndex(-1, 1)));
        assertTrue(graph.isVertexOutOfBounds(graph.xyToIndex( 3, -1)));
        assertTrue(graph.isVertexOutOfBounds(graph.xyToIndex( 3, 4)));
    }

    @Test
    void readFromFile_thenSuccess() {
        String filepath = "src/test/resources/example_graph.txt";
        assertDoesNotThrow(() -> {
            Graph graph = Graph.readFromFile(new File(filepath));
        });
    }
}