package graph.control;

import graph.Graph;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GraphController {
    @FXML
    Canvas canvas;

    private GraphModel graph;
    private GraphicsContext gc;
    private Clickable onVertexClick;

    void draw() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // setting canvas background color
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        drawGraph();
    }

    public void clearCanvas()
    {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void loadGraph(Graph graph) {
        if (graph == null)
            throw new NullPointerException("graph is null");
        this.graph = new GraphModel(graph);
        drawGraph();
    }

    public void setOnClickEvent(Clickable onClick) {
        this.onVertexClick = onClick;
    }

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        // canvas resizing
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());

        // click event for vertices
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

            if (!isInside)
                return;

            int index = y * graph.getWidth() + x;
            if (graph.getVertex(index) == null)
                return;

            graph.getVertex(index).toggleHighlight();
            graph.getVertex(index).draw(gc, dx, dy, graph.getWidth(), graph.getHeight(), side, graph.getMin(), graph.getMax());

            this.onVertexClick.click(x, y);
        });
    }

    private void drawDrawable(Iterable<? extends Drawable> drawable) {
        double dx = canvas.getWidth() / graph.getWidth();
        double dy = canvas.getHeight() / graph.getHeight();
        double side = Math.min(dx, dy) / 2;

        for (var shape : drawable)
            if (shape != null)
                shape.draw(gc, dx, dy, graph.getWidth(), graph.getHeight(), side, graph.getMin(), graph.getMax());
    }

    public void drawGraph() {
        drawDrawable(graph.getEdges().values());
        drawDrawable(graph.getVertices());
    }

    public GraphModel getGraphModel() {
        return graph;
    }
    public Canvas getCanvas() {
        return canvas;
    }
}