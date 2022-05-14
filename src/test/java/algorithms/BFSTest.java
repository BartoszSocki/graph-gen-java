package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {
    @Test
    void runBfsOnGraphThatIsFullyConnected_thenSuccess() {
        Graph graph = Graph.generateBidirectionalFromSeed(2, 5, 0, 1, 0);
        BFSResult result = BFS.bfs(graph, 0);
        String resultAsString = result.getVisitedVertices().toString();
        assertEquals(resultAsString, "[0=0, 5=1, 1=1, 6=2, 2=2, 7=3, 3=3, 8=4, 4=4, 9=5]");
    }

    @Test
    void runBfsOnGraphThatIsNotFullyConnected_thenSuccess() {
        String filepath = "src/test/resources/small_graph.txt";
        Graph graph = null;

        try {
            graph = Graph.readFromFile(filepath);
        } catch (Exception e) {
            // 😎
        }
        BFSResult result3 = BFS.bfs(graph, 0);
        String resultAsString3 = result3.getVisitedVertices().toString();
        assertEquals(resultAsString3, "[0=0, 1=1, 2=2, 5=3, 8=4]");

        BFSResult result2 = BFS.bfs(graph, 3);
        String resultAsString2 = result2.getVisitedVertices().toString();
        assertEquals(resultAsString2, "[3=0, 4=1]");

        BFSResult result1 = BFS.bfs(graph, 6);
        String resultAsString1 = result1.getVisitedVertices().toString();
        assertEquals(resultAsString1, "[6=0]");
    }
}