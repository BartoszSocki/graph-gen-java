package graph.control;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    void draw(GraphicsContext gc, double dx, double dy, int width, int height, double side, double minWeight, double maxWeight);
}
