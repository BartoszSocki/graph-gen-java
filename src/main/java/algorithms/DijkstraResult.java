package algorithms;

public class DijkstraResult {
    int[] pred;
    double[] dist;
    int source;

    public DijkstraResult(int[] pred, double[] dist, int source) {
        this.pred = pred;
        this.dist = dist;
        this.source = source;
    }

    public double[] getDist() {
        return dist;
    }

    public int getSource() {
        return source;
    }

    public int[] getPred() {
        return pred;
    }
}
