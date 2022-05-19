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

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;

public class MainController {
    private Graph graph;
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

    private final Deque<Integer> lastSelectedVertices;

    private boolean dijkstraUseTextInput;
    private boolean bfsUseTextInput;

    public MainController() {
        this.dijkstraUseTextInput = true;
        this.bfsUseTextInput = true;
        this.lastSelectedVertices = new LinkedList<>();
    }

    // this is not optimal but simple
    public void clearHighlighted() {
        for (var vertex : graphController.getGraphModel().getVertices())
            if (vertex != null)
                vertex.setHighlighted(false);

        for (var edge : graphController.getGraphModel().getEdges().values())
            edge.setHighlighted(false);
    }

    @FXML
    public void initialize() {
        // for testing purposes
        graph = Graph.generateBidirectionalFromSeed(10, 10, 0, 1, 0);
        graphController.loadGraph(graph);

        // here goes vertex click logic
        graphController.setOnClickEvent((x, y) -> {
            resetGraph();

            // cursed
            int vertex = graph.xyToIndex(y, x);
            graphController.getGraphModel().getVertex(vertex).setHighlighted(true);

            // removes all duplicates
            if (lastSelectedVertices.size() > 0 && lastSelectedVertices.contains(vertex)) {
                lastSelectedVertices.remove(vertex);
                graphController.getGraphModel().getVertex(vertex).setHighlighted(false);
                // temporary solution
                for (var selectedVertex : lastSelectedVertices)
                    graphController.getGraphModel().getVertex(selectedVertex).setHighlighted(true);
                graphController.drawGraph();
                return;
            }
            System.out.println("1: " + lastSelectedVertices);

            lastSelectedVertices.add(vertex);

            // remove elements when overflowing
            if (lastSelectedVertices.size() == 3) {
                int unselected = lastSelectedVertices.removeFirst();
                graphController.getGraphModel().getVertex(unselected).setHighlighted(false);
            }

            // temporary solution
            for (var selectedVertex : lastSelectedVertices)
                graphController.getGraphModel().getVertex(selectedVertex).setHighlighted(true);

            graphController.drawGraph();
        });

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
    }

    private void resetGraph() {
        clearHighlighted();
        graphController.drawGraph();
    }

    @FXML
    public void dijkstraTextInputChecked(ActionEvent e) {
        dijkstraUseTextInput = !dijkstraUseTextInput;

        dijkstraStartField.setEditable(dijkstraUseTextInput);
        dijkstraEndField.setEditable(dijkstraUseTextInput);

        dijkstraEndField.setVisible(dijkstraUseTextInput);
        dijkstraStartField.setVisible(dijkstraUseTextInput);

        dijkstraStartField.setText("");
        dijkstraEndField.setText("");

        resetGraph();
    }

    @FXML
    public void runDijkstraButtonPressed(ActionEvent e) {
        int start = -1;
        int end = -1;

        if (!dijkstraUseTextInput && lastSelectedVertices.size() == 2) {
            start = lastSelectedVertices.remove();
            end = lastSelectedVertices.remove();
        } else if (dijkstraStartField.getText().length() != 0 && dijkstraEndField.getText().length() != 0) {
            start = Integer.parseInt(dijkstraStartField.getText());
            end = Integer.parseInt(dijkstraEndField.getText());
        } else {
            createAlertWindow("Dijkstra", "Wrong input!");
        }

        runDijkstra(start, end);
    }

    // TODO: wywala index out of bounds exception
    private void runDijkstra(int start, int end) {
        new Thread(() -> {
            try {
                clearHighlighted();
                Path dr = Dijkstra.dijkstra(graph, start, end);
                int[] path = dr.vertices();

                for (int i = 1; i < path.length; i++) {
                    int u = path[i - 1];
                    int v = path[i];
                    System.out.println(u + " " + v);
                    graphController.getGraphModel().getVertex(u).setHighlighted(true);
                    graphController.getGraphModel().getVertex(v).setHighlighted(true);
                    // TODO: kolorowanie do poprawy, bo się program wypierdala
//                    graphController.getGraphModel().getEdge(u, v).setHighlighted(true);
                }
                graphController.drawGraph();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    @FXML
    private void bfsTextInputChecked() {
        resetGraph();

        bfsUseTextInput = !bfsUseTextInput;
        bfsStartField.setVisible(bfsUseTextInput);
        bfsStartField.setEditable(bfsUseTextInput);
    }

    @FXML
    public void runBfsButtonPressed(ActionEvent e) {
        try {
            int start = -1;

            if (!bfsUseTextInput && lastSelectedVertices.size() > 0) {
                start = lastSelectedVertices.remove();
            } else if (bfsStartField.getText().length() != 0) {
                start = Integer.parseInt(bfsStartField.getText());
            } else {
                createAlertWindow("BFS", "Wrong input!");
            }

            runBfs(start);
        } catch (Exception nfe) {
            createAlertWindow("BFS", "Wrong input!");
        }
    }

    private void runBfs(int start) {
        clearHighlighted();

        Path path = BFS.bfs(graph, start);
        for (var vertex : path.vertices())
            graphController.getGraphModel().getVertex(vertex).setHighlighted(true);

        graphController.drawGraph();
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
            if (!graphGenSeedField.getText().isEmpty())
                seed = Integer.parseInt(graphGenSeedField.getText());

            graphController.clearCanvas();

            graph = Graph.generateBidirectionalFromSeed(rows, cols, min, max, seed);
            graphController.loadGraph(graph);
            graphController.drawGraph();
        } catch (Exception nfe) {
            createAlertWindow("Generate graph", "Wrong input!");
        }
    }

    private void createAlertWindow(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Graphalgo-gui error!");
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void loadFromFileButtonPressed(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File fileToOpen = fileChooser.showOpenDialog(null);

        try {
            if (fileToOpen != null) {
                graph = Graph.readFromFile(fileToOpen);
                graphController.loadGraph(graph);

                graphController.clearCanvas();
                graphController.drawGraph();
            } else {
                System.out.println("file is null");
            }
        } catch (Exception exe) {
            System.out.println(exe.getMessage());
        }
    }

    @FXML
    public void saveButtonPressed(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File f = fc.showSaveDialog(null);

        try {
            if (f != null) {
                Graph.writeToFile(graph, f.getPath());
            } else {

            }
        } catch (Exception exe) {

        }
    }

    @FXML
    public void clearGraphButtonPressed(ActionEvent e) {
        clearHighlighted();
        graphController.drawGraph();
    }
}