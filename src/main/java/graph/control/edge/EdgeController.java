package graph.control.edge;

import graph.Edge;
import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EdgeController extends Highlightable implements Drawable {
    private final Edge model;

    public EdgeController(int begVertex, int endVertex, double weight) {
        this(begVertex, endVertex, weight, Color.BLUE, Color.RED);
    }

    public EdgeController(int begVertex, int endVertex, double weight, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = new Edge(begVertex, endVertex, weight);
    }

    @Override
    public void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side, double minWeight, double maxWeight) {
        double begX = (model.getBegVertex() % width) * dx + (dx) / 2;
        double begY = (model.getBegVertex() / width) * dy + (dy) / 2;

        double endX = (model.getEndVertex() % width) * dx + (dx) / 2;
        double endY = (model.getEndVertex() / width) * dy + (dy) / 2;

        double hue = 360 * ((model.getWeight() - minWeight) / (1 + maxWeight - minWeight));
        gc.setStroke(isHighlighted() ? getHighlightColor() : Color.hsb(hue, 1.0, 1.0));

        // make edge start shifted, more to left or more to right
        double offsetX = (side / 4) * ((model.getBegVertex() / width <= model.getEndVertex() / width) ? -1 : 1);
        double offsetY = (side / 4) * ((model.getBegVertex() % width <= model.getEndVertex() % width) ? 1 : -1);

        // for larger lines
//        gc.setLineWidth(side / 2);

        // for smaller lines
         gc.setLineWidth(side / 4);
         gc.strokeLine(begX + offsetX, begY + offsetY, endX + offsetX, endY + offsetY);
    }
}
