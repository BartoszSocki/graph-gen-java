package algorithms;

import graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DijkstraTest {
    @Test
    void dijkstraCorrectlyChoosesPath()
    {
        int n = 100;
        Graph graph = Graph.generateBidirectionalFromSeed(1, n,0,1,42);
        Path result = Dijkstra.dijkstra(graph, 0 , n-1);
        for(int i =0; i < result.vertices().length; i++)
            assertEquals(result.vertices()[i], n-1-i);
    }
}
