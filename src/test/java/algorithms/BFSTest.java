package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {
    @Test
    void runBfsOnGraphThatIsFullyConnected_thenSuccess() {
        Graph graph = Graph.generateBidirectionalFromSeed(2, 5, 0, 1, 0);
        Path result = BFS.bfs(graph, 0);
        Set<Integer> set = Arrays.stream(result.vertices()).boxed().collect(Collectors.toSet());
        assertEquals(set, new HashSet<>(Arrays.asList(0, 1, 2, 3, 4 ,5, 6, 7, 8, 9)));
    }

    @Test
    void runBfsOnGraphThatIsNotFullyConnected_thenSuccess() {
        String filepath = "src/test/resources/small_graph.txt";
        Graph graph = null;

        try {
            graph = Graph.readFromFile(new File(filepath));
        } catch (Exception e) {
            // 😎
        }
        Path result3 = BFS.bfs(graph, 0);
        Set<Integer> set3 = Arrays.stream(result3.vertices()).boxed().collect(Collectors.toSet());
        assertEquals(set3, new HashSet<>(Arrays.asList(0, 1, 2, 5, 8)));

        Path result2 = BFS.bfs(graph, 3);
        Set<Integer> set2 = Arrays.stream(result2.vertices()).boxed().collect(Collectors.toSet());
        assertEquals(set2, new HashSet<>(Arrays.asList(3, 4)));

        Path result1 = BFS.bfs(graph, 6);
        Set<Integer> set1 = Arrays.stream(result1.vertices()).boxed().collect(Collectors.toSet());
        assertEquals(set1, new HashSet<>(Arrays.asList(6)));
    }
}