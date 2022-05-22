package graph.control.vertex;

import graph.Vertex;
import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VertexController extends Highlightable implements Drawable {
    private final Vertex model;

    public VertexController(Vertex vertex, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = vertex;
    }

    @Override
    public void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side) {
        double recX = (this.model.vertex() % width) * dx + (dx - side) / 2;
        double recY = (this.model.vertex() / width) * dy + (dy - side) / 2;

        if (isHighlighted()) {
            gc.setFill(Color.BLACK);
            gc.fillRect(recX - 0.1 * side, recY - 0.1 * side, 1.2 * side, 1.2 * side);
        }

        gc.setFill(getCurrentColor());
        gc.fillRect(recX, recY, side, side);
    }

    @Override
    public String toString() {
        return "Vertex{" + model + '}';
    }
}
