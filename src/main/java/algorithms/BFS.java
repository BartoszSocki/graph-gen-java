package algorithms;

import graph.Edge;
import graph.Graph;

public class BFS {
    public static BFSResult bfs(Graph graph, int begVertex) {
        if (graph.isVertexOutOfBounds(begVertex))
            throw new IndexOutOfBoundsException();

        BFSResult result = new BFSResult(graph.getCols() * graph.getCols());

        int begIndex = 0, endIndex = 1;

        result.getVerticies()[0] = begIndex;
        result.getVisited()[0] = true;

        while (begIndex < endIndex) {
            int currIndex = result.getVerticies()[begIndex++];
            var edges = graph.getEdges().get(currIndex);
            while (!edges.isEmpty()) {
                // TODO
            }
        }

        return null;
    }
}
