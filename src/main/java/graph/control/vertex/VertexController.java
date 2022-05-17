package graph.control.vertex;

import graph.Vertex;
import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VertexController extends Highlightable implements Drawable {
    private final Vertex model;

    public VertexController(int vertex) {
        this(vertex, Color.BLUE, Color.BLACK);
    }

    public VertexController(int vertex, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = new Vertex(vertex);
    }

    @Override
    public void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side, double minWeight, double maxWeight) {
        double recX = (this.model.getVertex() % width) * dx + (dx - side) / 2;
        double recY = (this.model.getVertex() / width) * dy + (dy - side) / 2;

        gc.setFill(this.getColor());
        gc.fillRect(recX, recY, side, side);
    }

    @Override
    public String toString() {
        return "GraphVertex{" + model + '}';
    }
}
