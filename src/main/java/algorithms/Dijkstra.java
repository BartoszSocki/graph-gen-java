package algorithms;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra {

    // TODO: wywal ten custom exception
    public static Path dijkstra(Graph g, int source, int dest){
        int n = g.getCols() * g.getRows();
        int[] pred = new int[n];
        double[] dist = new double[n];

        Arrays.fill(pred, -1);
        Arrays.fill(dist, Double.MAX_VALUE);

        VertexPriorityQueue q = new VertexPriorityQueue(n);
        q.add(source, 0);
        for(int i =0; i < n;i++)
        {
            if(i == source)
                continue;
            q.add(i);
        }

        while(q.getSize() != 0)
        {
            QueuedVertex u = q.poll();

            for(Edge e : g.getEdges().get(u.getId()))
            {
                int v = e.endVertex();
                double w = e.weight();

                if(q.isInTheQueue(v))
                {
                    double alt = u.getDist() + w;
                    if(alt < q.getQueuedVertex(v).getDist())
                    {
                        q.update(v, alt);
                        pred[v] = u.getId();
                    }
                }
            }
            dist[u.getId()] = u.getDist();
        }

        return getPath(pred, dist, dest);
    }

    private static Path getPath(int[] pred, double[] dist, int dest) {
        if (dist[dest] == -1)
            return null;
        List<Integer> nodes = new ArrayList<>();
        int j = dest;
        do {
            nodes.add(j);
            j = pred[j];
        } while (j != -1);

        return new Path(nodes.stream().mapToInt(n -> n).toArray());

    }
}
