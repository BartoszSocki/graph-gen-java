package graph.control;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class GraphController {
    @FXML
    Canvas canvas;

    private GraphModel graph;
    private GraphicsContext gc;

    void draw() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        drawGraph();
    }

    @FXML
    public void initialize() {
        graph = new GraphModel(50, 100);
        gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < graph.getSize(); i++) {
            graph.addVertex(i);
//            graph.addEdge(0, i);
        }

        drawGraph();

        // those lines make canvas responsive
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());

        // for vertices
        canvas.setOnMouseClicked(event -> {
            double dx = canvas.getWidth() / graph.getWidth();
            double dy = canvas.getHeight() / graph.getHeight();
            double side = Math.min(dx, dy) / 2;

            int x = (int)(event.getX() / dx);
            int y = (int)(event.getY() / dy);

            double left = x * dx + (dx - side) / 2;
            double right = x * dx + (dx + side) / 2;
            boolean isHorizontallyInside = left <= event.getX() && event.getX() <= right;

            double top = y * dy + (dy - side) / 2;
            double bottom = y * dy + (dy + side) / 2;
            boolean isVerticallyInside = top <= event.getY() && event.getY() <= bottom;
            boolean isInside = isHorizontallyInside && isVerticallyInside;

            if (isInside) {
                int index = y * graph.getWidth() + x;
                graph.getVertices().get(index).toggleHighlight();
                graph.getVertices().get(index).draw(gc, dx, dy, graph.getWidth(), graph.getHeight(), side);
            }
        });
    }

    private void drawDrawable(List<? extends Drawable> drawable) {
        double dx = canvas.getWidth() / graph.getWidth();
        double dy = canvas.getHeight() / graph.getHeight();
        double side = Math.min(dx, dy) / 2;

        for (var shape : drawable)
            shape.draw(gc, dx, dy, graph.getWidth(), graph.getHeight(), side);
    }

    public void drawGraph() {
        drawDrawable(graph.getEdges());
        drawDrawable(graph.getVertices());
    }

    public Canvas getCanvas() {
        return canvas;
    }
}