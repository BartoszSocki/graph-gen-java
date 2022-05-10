package algorithms;

import java.util.Comparator;

public class QueuedVertex implements Comparable<QueuedVertex>
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


    @Override
    public int compareTo(QueuedVertex o) {
        return Double.compare(this.getDist(), o.getDist());
    }
}

