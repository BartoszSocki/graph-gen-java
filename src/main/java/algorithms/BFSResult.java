package algorithms;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BFSResult {
    private final boolean[] visited;
    private final int[] verticies;
    private final int[] distance;

    private final int size;

    public BFSResult(int size) {
        this.visited = new boolean[size];
        this.verticies = new int[size];
        Arrays.fill(this.verticies, -1);

        this.distance = new int[size];
        this.size = size;
    }

    public List<Pair<Integer, Integer>> getVisitedVertices() {
        LinkedList<Pair<Integer, Integer>> list = new LinkedList<>();
        for (int i = 0; i < this.getSize(); i++) {
            if (this.getVertices()[i] == -1)
                continue;
            if (!this.getVisited()[this.getVertices()[i]])
                continue;
            list.add(new Pair<>(this.getVertices()[i], this.getDistance()[i]));
        }
        return list;
    }

    public boolean[] getVisited() {
        return visited;
    }

    public int[] getVertices() {
        return verticies;
    }

    public int[] getDistance() {
        return distance;
    }

    public int getSize() {
        return size;
    }
}
