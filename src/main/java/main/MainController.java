package main;

import algorithms.*;
import graph.Graph;
import graph.control.GraphController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import utils.Logger;

import java.io.File;

public class MainController {
    @FXML
    private GraphController graphController;
    @FXML
    private Pane graphPane;
    @FXML
    private GridPane grid;
    @FXML
    private TextField graphGenRowsField;
    @FXML
    private TextField graphGenColsField;
    @FXML
    private TextField graphGenMinField;
    @FXML
    private TextField graphGenMaxField;
    @FXML
    private TextField graphGenSeedField;
    @FXML
    private TextField dijkstraStartField;
    @FXML
    private TextField dijkstraEndField;
    @FXML
    private TextField bfsStartField;
    @FXML
    private TextArea logField;

    private int lastSelected;
    private int lastUpdatedField = 0;

    @FXML
    public void initialize() {
        lastSelected = -1;

        // default graph
        Graph graph = Graph.generateBidirectionalFromSeed(10, 10, 0, 1, 0);
        graphController.loadGraph(graph);
        graphController.draw();

        // here goes vertex click logic
        graphController.setOnClickEvent((x, y) -> {
            // cursed
            int vertex = graphController.getCurrentGraph().xyToIndex(y, x);

            if (vertex == lastSelected)
                return;

            graphController.clearHighlighted();
            graphController.getGraphModel().getVertex(vertex).setHighlighted(true);

            if (lastSelected != -1)
                graphController.getGraphModel().getVertex(lastSelected).setHighlighted(true);

            int unselected = -1;

            if (lastUpdatedField == 0) {
                dijkstraStartField.setText(Integer.toString(vertex));
                bfsStartField.setText(Integer.toString(vertex));
            } else if (lastUpdatedField == 1) {
                dijkstraEndField.setText(Integer.toString(vertex));
                bfsStartField.setText(Integer.toString(vertex));
            } else if (lastUpdatedField == 2) {
                unselected = Integer.parseInt(dijkstraStartField.getText());
                dijkstraStartField.setText(dijkstraEndField.getText());
                dijkstraEndField.setText(Integer.toString(vertex));
                bfsStartField.setText(Integer.toString(vertex));
            }

            // remove elements when overflowing
            if (lastUpdatedField == 2) {
                graphController.getGraphModel().getVertex(unselected).setHighlighted(false);
            }

            // update value when user clicked 3 times
            lastUpdatedField = (lastUpdatedField != 2) ? lastUpdatedField + 1 : 0;
            lastSelected = vertex;

            graphController.draw();
        });

        // make right panel fixed size
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints(200);
        col1.setHgrow(Priority.ALWAYS);

        grid.setGridLinesVisible(true);
        grid.getColumnConstraints().addAll(col1, col2);

        // make canvas square
        DoubleBinding side = (DoubleBinding) Bindings.min(graphPane.widthProperty(), graphPane.heightProperty());
        graphController.getCanvas().widthProperty().bind(side);
        graphController.getCanvas().heightProperty().bind(side);

        // center canvas inside pane
        graphController.getCanvas().layoutXProperty().bind(graphPane.widthProperty().subtract(side).divide(2));
        graphController.getCanvas().layoutYProperty().bind(graphPane.heightProperty().subtract(side).divide(2));

        Logger.setTextField(logField);
    }

    public void runDijkstraButtonPressed(ActionEvent event) {
        int start = -1;
        int end = -1;
        try {
            if (dijkstraStartField.getText().length() != 0 && dijkstraEndField.getText().length() != 0) {
                start = Integer.parseInt(dijkstraStartField.getText());
                end = Integer.parseInt(dijkstraEndField.getText());
                Logger.clear();
                runDijkstra(start, end);
                clearFields();
            } else {
                Logger.displayError("DIJKSTRA", "Two points have to be specified!");
            }
        } catch (NumberFormatException nfe) {
            Logger.displayError("DIJKSTRA", "Vertex indexes have to be integers!");
        } catch (IllegalArgumentException e) {
            Logger.displayError("DIJKSTRA", e.getMessage());
        }
    }

    private void runDijkstra(int begVertex, int endVertex) {
        if (graphController.getCurrentGraph().isVertexOutOfBounds(begVertex) ||
                graphController.getCurrentGraph().isVertexOutOfBounds(endVertex))
            throw new IllegalArgumentException("Vertices are out of bounds");

        new Thread(() -> {
            Path path = Dijkstra.dijkstra(graphController.getCurrentGraph(), begVertex, endVertex);
            graphController.clearHighlighted();
            graphController.highlightPath(path, true);
        }).start();
    }

    @FXML
    public void runBfsButtonPressed(ActionEvent event) {
        int start = -1;
        try {
            if (bfsStartField.getText().length() != 0) {
                start = Integer.parseInt(bfsStartField.getText());

                Logger.clear();
                runBfs(start);
                clearFields();
            } else {
                Logger.displayError("BFS", "Vertex index has to be specified!");
            }
        } catch (NumberFormatException nfe) {
            Logger.displayError("bfs", "vertex index has to be integer!");
        } catch (IllegalArgumentException e) {
            Logger.displayError("bfs", e.getMessage());
        }
    }

    private void runBfs(int begVertex) {
        if (graphController.getCurrentGraph().isVertexOutOfBounds(begVertex))
            throw new IllegalArgumentException("Vertex out of bounds");

        new Thread(() -> {
            Path path = BFS.bfs(graphController.getCurrentGraph(), begVertex);

            graphController.clearHighlighted();
            graphController.highlightPath(path, false);
        }).start();
    }

    @FXML
    public void graphGenButtonPressed(ActionEvent e) {
        try {
            int rows, cols;
            double min, max;
            int seed = 0;

            rows = Integer.parseInt(graphGenRowsField.getText());
            cols = Integer.parseInt(graphGenColsField.getText());
            min = Double.parseDouble(graphGenMinField.getText());
            max = Double.parseDouble(graphGenMaxField.getText());

            if (rows <= 0 || cols <= 0)
                throw new IllegalArgumentException("rows and cols have to be positive numbers!");
            if (min >= max)
                throw new IllegalArgumentException("min should be smaller than max");

            if (!graphGenSeedField.getText().isEmpty())
                seed = Integer.parseInt(graphGenSeedField.getText());

            Graph graph = Graph.generateBidirectionalFromSeed(rows, cols, min, max, seed);
            graphController.loadGraph(graph);
            graphController.draw();

        } catch (NumberFormatException nfe) {
            Logger.displayError("Generate", "Rows, cols, min, max have to be numbers!");
        } catch (IllegalArgumentException il) {
            Logger.displayError("Generate", il.getMessage());
        }
    }

    @FXML
    public void loadFromFileButtonPressed(ActionEvent e) {
        clearFields();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File fileToOpen = fileChooser.showOpenDialog(null);

        try {
            if (fileToOpen != null) {
                Graph graph = Graph.readFromFile(fileToOpen);
                graphController.loadGraph(graph);
                graphController.draw();
            } else {
                System.out.println("file is null");
            }
        } catch (Exception exe) {
            Logger.displayError("Load File", "Cannot load file!");
        }
    }

    @FXML
    public void saveButtonPressed(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File f = fc.showSaveDialog(null);

        try {
            if (f != null) {
                Graph.writeToFile(graphController.getCurrentGraph(), f.getPath());
            } else {
                Logger.displayError("Save File", "Unexpected IO Error happened");
            }
        } catch (Exception exe) {
            Logger.displayError("Save File", "Cannot save file!");
        }
    }

    @FXML
    public void clearGraphButtonPressed(ActionEvent e) {
        clearFields();
        graphController.clearHighlighted();
        graphController.draw();
    }

    private void clearFields() {
        lastUpdatedField = 0;
        lastSelected = -1;
        dijkstraStartField.setText("");
        dijkstraEndField.setText("");
        bfsStartField.setText("");
    }
}