package algorithms;

public class BFSResult {
    private boolean visited[];
    private int verticies[];
    private int distance[];

    private int size;

    public BFSResult(int size) {
        this.visited = new boolean[size];
        this.verticies = new int[size];
        this.distance = new int[size];
        this.size = size;
    }

    public boolean[] getVisited() {
        return visited;
    }

    public void setVisited(boolean[] visited) {
        this.visited = visited;
    }

    public int[] getVerticies() {
        return verticies;
    }

    public void setVerticies(int[] verticies) {
        this.verticies = verticies;
    }

    public int[] getDistance() {
        return distance;
    }

    public void setDistance(int[] distance) {
        this.distance = distance;
    }
}
