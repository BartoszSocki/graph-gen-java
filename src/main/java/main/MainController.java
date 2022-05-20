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
    @FXML
    private TextArea logField;

    private int lastSelected;
    private int lastUpdatedField = 0;


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
        lastSelected = -1;

        // for testing purposes
        graph = Graph.generateBidirectionalFromSeed(10, 10, 0, 1, 0);
        graphController.loadGraph(graph);

        // here goes vertex click logic
        graphController.setOnClickEvent((x, y) -> {
            // cursed
            int vertex = graph.xyToIndex(y, x);

            if (vertex == lastSelected)
                return;
            resetGraph();

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


            if (lastUpdatedField != 2)
                lastUpdatedField++;
            else
                lastUpdatedField = 0;

            lastSelected = vertex;

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


        Logger.setTextField(logField);
    }

    private void resetGraph() {
        clearHighlighted();
        graphController.drawGraph();
    }

    public void runDijkstraButtonPressed(ActionEvent e) {
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
        }


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
                    // TODO: kolorowanie do poprawy, bo się program wywala
//                    graphController.getGraphModel().getEdge(u, v).setHighlighted(true);
                }
                graphController.drawGraph();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();

    }

    @FXML
    public void runBfsButtonPressed(ActionEvent e) {
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
            Logger.displayError("BFS", "Vertex index has to be integer!");
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
        }
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


    private void clearFields() {
        lastUpdatedField = 0;
        lastSelected = -1;
        dijkstraStartField.setText("");
        dijkstraEndField.setText("");
        bfsStartField.setText("");
    }
}