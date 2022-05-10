package gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Grid {
    private final Pane pane;
    private final int width;
    private final int height;

    private final Node[] nodes;

    public Grid(Pane pane, int height, int width) {
        this.pane = pane;
        this.height = height;
        this.width = width;
        this.nodes = new Node [width * height];
    }

    public void add(int x, int y, Node node) {
        Circle circle = node.getCircle();

        DoubleBinding min = (DoubleBinding) Bindings.min(pane.widthProperty(), pane.heightProperty());
        DoubleBinding dx = min.divide(2 * width);
        DoubleBinding dy = min.divide(2 * height);
        DoubleBinding dr = (DoubleBinding) Bindings.min(dx, dy);

        circle.radiusProperty().bind(dr.divide(2));
        circle.centerXProperty().bind(dx.multiply(2 * x + 1).add(Bindings.max(0, pane.widthProperty().subtract(pane.heightProperty()).divide(2))));
        circle.centerYProperty().bind(dy.multiply(2 * y + 1).add(Bindings.max(0, pane.heightProperty().subtract(pane.widthProperty()).divide(2))));

        this.nodes[this.xyToIndex(x, y)] = node;
        this.pane.getChildren().add(node.getCircle());
    }

    public void addEdge(int xa, int ya, int xb, int yb, double weight) {
        Line line = new Line();
//        line.startXProperty().bind();
        
        this.pane.getChildren().add(line);
    }

    private int xyToIndex(int x, int y) {
        return x * this.height + y;
    }

    public Node getNode(int x, int y) {
        return this.nodes[this.xyToIndex(x, y)];
    }
}
