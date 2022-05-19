package algorithms;

import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BFS {
    public static Path bfs(Graph graph, int begVertex) {
        if (graph == null)
            throw new NullPointerException(BFS.class.getSimpleName() + ": graph is null");
        if (graph.isVertexOutOfBounds(begVertex))
            throw new IndexOutOfBoundsException(BFS.class.getSimpleName() + ": vertex out of bounds");

//        BFSResult result = new BFSResult(graph.getCols() * graph.getCols());
        int[] vertices = new int[graph.getCols() * graph.getRows()];
        boolean[] visited = new boolean[graph.getCols() * graph.getRows()];
        Arrays.fill(vertices, -1);
        Arrays.fill(visited, false);

        int begIndex = 0, endIndex = 1;

//        result.getVertices()[0] = begVertex;
//        result.getVisited()[begVertex] = true;

        vertices[0] = begVertex;
        visited[begVertex] = true;

        while (begIndex < endIndex) {
            int currIndex = vertices[begIndex++];
            var edges = graph.getEdges().get(currIndex);
            for (var edge : edges) {
                if (visited[edge.endVertex()])
                    continue;
                vertices[endIndex] = edge.endVertex();
//                getDistance()[endIndex] = result.getDistance()[begIndex - 1] + 1;
                visited[edge.endVertex()] = true;
                endIndex++;
            }
        }

        List<Integer> visitedVertices = new ArrayList<>();
        for (int i = 0; i < visited.length; i++)
            if (visited[i])
                visitedVertices.add(i);

        return new Path(visitedVertices.stream().mapToInt(n -> n).toArray());
    }
}
