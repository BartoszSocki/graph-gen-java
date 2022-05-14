package algorithms;

import graph.Graph;

public class BFS {
    public static BFSResult bfs(Graph graph, int begVertex) {
        if (graph == null)
            throw new NullPointerException(BFS.class.getSimpleName() + ": graph is null");
        if (graph.isVertexOutOfBounds(begVertex))
            throw new IndexOutOfBoundsException(BFS.class.getSimpleName() + ": vertex out of bounds");

        BFSResult result = new BFSResult(graph.getCols() * graph.getCols());

        int begIndex = 0, endIndex = 1;

        result.getVertices()[0] = begVertex;
        result.getVisited()[begVertex] = true;

        while (begIndex < endIndex) {
            int currIndex = result.getVertices()[begIndex++];
            var edges = graph.getEdges().get(currIndex);
            for (var edge : edges) {
                if (result.getVisited()[edge.getEndVertex()])
                    continue;
                result.getVertices()[endIndex] = edge.getEndVertex();
                result.getDistance()[endIndex] = result.getDistance()[begIndex - 1] + 1;
                result.getVisited()[edge.getEndVertex()] = true;
                endIndex++;
            }
        }
        return result;
    }
}
