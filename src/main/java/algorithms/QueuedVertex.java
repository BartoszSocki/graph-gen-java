package algorithms;

import java.util.Comparator;

public class QueuedVertex implements Comparator<QueuedVertex>
{
    private final int id;
    private double dist;

    public int getId() {
        return id;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public QueuedVertex(int id, double dist) {
        this.id = id;
        this.dist = dist;
    }

    public QueuedVertex(int id) {
        this.id = id;
        this.dist = Double.MAX_VALUE;
    }

    @Override
    public int compare(QueuedVertex o1, QueuedVertex o2) {
        return Double.compare(o1.getDist(), o2.getDist());
    }
}

