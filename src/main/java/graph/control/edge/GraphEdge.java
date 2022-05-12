package graph.control.edge;

import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphEdge extends Highlightable implements Drawable {
    private final GraphEdgeModel model;

    public GraphEdge(int begVertex, int endVertex) {
        this(begVertex, endVertex, Color.BLUE, Color.RED);
    }

    public GraphEdge(int begVertex, int endVertex, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = new GraphEdgeModel(begVertex, endVertex);
    }

    @Override
    public void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side) {
        double begX = (this.model.getBegVertex() % width) * dx + (dx) / 2;
        double begY = (this.model.getBegVertex() / width) * dy + (dy) / 2;

        double endX = (this.model.getEndVertex() % width) * dx + (dx) / 2;
        double endY = (this.model.getEndVertex() / width) * dy + (dy) / 2;

        gc.setStroke(this.getColor());
        gc.setLineWidth(side / 5);
        gc.strokeLine(begX, begY, endX, endY);
    }
}
