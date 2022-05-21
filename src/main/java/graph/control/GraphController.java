package graph.control;

import algorithms.Path;
import graph.Graph;
import graph.control.edge.EdgeController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GraphController {
    @FXML
    Canvas canvas;

    private GraphModel graph;
    private GraphicsContext gc;
    private ClickConsumer onVertexClick;
    private Graph currentGraph;

    public synchronized void draw() {
        clearCanvas();
        drawGraph();
    }

    // this is not optimal but simple
    public void clearHighlighted() {
        for (var vertex : getGraphModel().getVertices())
            if (vertex != null)
                vertex.setHighlighted(false);

        for (var edge : getGraphModel().getEdges().values())
            edge.setHighlighted(false);
    }
    public synchronized void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void loadGraph(Graph graph) {
        if (graph == null)
            throw new NullPointerException("graph is null");
        this.currentGraph = graph;
        this.graph = new GraphModel(graph);
        drawGraph();
    }

    public void highlightPath(Path path, boolean highlightEdges) {
        for (var vertex : path.vertices())
            getGraphModel().getVertex(vertex).setHighlighted(true);

        if (!highlightEdges) {
            draw();
            return;
        }

        for (int i = 0; i < path.vertices().length - 1; i++) {
            int begVertex = path.vertices()[i];
            int endVertex = path.vertices()[i + 1];

            if (getGraphModel().getEdge(begVertex, endVertex) != null)
                getGraphModel().getEdge(begVertex, endVertex).setHighlighted(true);
            else
                getGraphModel().getEdge(endVertex, begVertex).setHighlighted(true);
        }
        draw();
    }

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();

        // canvas resizing
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());

        // click event for vertices
        canvas.setOnMouseClicked(event -> {
            // this whole section checks if user clicked on vertex or not
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

    public synchronized void drawGraph() {
        drawDrawable(graph.getEdges().values());
        drawDrawable(graph.getVertices());
    }

    public void setOnClickEvent(ClickConsumer onClick) {
        this.onVertexClick = onClick;
    }
    public GraphModel getGraphModel() {
        return graph;
    }
    public Canvas getCanvas() {
        return canvas;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }
}