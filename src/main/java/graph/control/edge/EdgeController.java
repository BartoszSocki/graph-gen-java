package graph.control.edge;

import graph.Edge;
import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EdgeController extends Highlightable implements Drawable {
    private final Edge model;

    public EdgeController(Edge edge) {
        this(edge, Color.BLUE, Color.RED);
    }

    public EdgeController(Edge edge, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = edge;
    }

    @Override
    public void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side, double minWeight, double maxWeight) {
        double begX = (model.begVertex() % width) * dx + (dx) / 2;
        double begY = (model.begVertex() / width) * dy + (dy) / 2;

        double endX = (model.endVertex() % width) * dx + (dx) / 2;
        double endY = (model.endVertex() / width) * dy + (dy) / 2;

        double hue = 360 * ((model.weight() - minWeight) / (1 + maxWeight - minWeight));
        gc.setStroke(isHighlighted() ? getHighlightColor() : Color.hsb(hue, 1.0, 1.0));

        // make edge start shifted, more to left or more to right
        double offsetX = (side / 4) * ((model.begVertex() / width <= model.endVertex() / width) ? -1 : 1);
        double offsetY = (side / 4) * ((model.begVertex() % width <= model.endVertex() % width) ? 1 : -1);

        // for larger lines
//        gc.setLineWidth(side / 2);

        // for smaller lines
         gc.setLineWidth(side / 4);
         gc.strokeLine(begX + offsetX, begY + offsetY, endX + offsetX, endY + offsetY);
    }
}
