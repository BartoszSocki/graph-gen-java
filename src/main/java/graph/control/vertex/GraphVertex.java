package graph.control.vertex;

import graph.control.Drawable;
import graph.control.Highlightable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphVertex extends Highlightable implements Drawable {
    private final GraphVertexModel model;

    public GraphVertex(int vertex) {
        this(vertex, Color.BLUE, Color.BLACK);
    }

    public GraphVertex(int vertex, Color highlightColor, Color defaultColor) {
        super(highlightColor, defaultColor);
        this.model = new GraphVertexModel(vertex);
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
