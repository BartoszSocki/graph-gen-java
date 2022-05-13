package com.wiaczek.socki.graphalgogui;

import algorithms.BFS;
import algorithms.BFSResult;
import algorithms.Dijkstra;
import algorithms.DijkstraResult;
import graph.Graph;
import graph.control.GraphController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class HelloController {
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
    private Integer[] lastDijkstraPath;

    private Deque<Integer> lastSelectedVertices;

    private boolean dijkstraUseTextInput;

    public HelloController() {
        lastDijkstraPath = null;
        dijkstraUseTextInput = true;
        lastSelectedVertices = new LinkedList<>();
    }

    @FXML
    public void initialize() {
        // for testing purposes
        graph = Graph.generateBidirectionalFromSeed(10, 10, 0, 1, 0);
        graphController.loadGraph(graph);

        // here goes vertex click logic
        graphController.setOnClickEvent((x, y) -> {
            clearLastDijkstraPath();

            // cursed
            int vertex = graph.xyToIndex(y, x);

            if (lastSelectedVertices.size() > 0 && lastSelectedVertices.contains(vertex)) {
                lastSelectedVertices.remove(vertex);
                graphController.drawGraph();
                return;
            }

            lastSelectedVertices.add(vertex);

            if (lastSelectedVertices.size() == 3) {
                int unselected = lastSelectedVertices.removeFirst();
                graphController.getGraphModel().getVertex(unselected).setHighlighted(false);
            }

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

    private void clearLastDijkstraPath()
    {
        if(lastDijkstraPath == null)
            return;

        for(int i = 1; i < lastDijkstraPath.length; i++) {
            int u = lastDijkstraPath[i - 1];
            int v = lastDijkstraPath[i];
            graphController.getGraphModel().getVertex(u).setHighlighted(false);
            graphController.getGraphModel().getVertex(v).setHighlighted(false);
            graphController.getGraphModel().getEdge(u, v).setHighlighted(false);
        }
        graphController.drawGraph();
        lastDijkstraPath = null;
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

        clearLastDijkstraPath();
    }

    @FXML
    public void runDijkstraButtonPressed(ActionEvent e) {
        clearLastDijkstraPath();

        int start = -1;
        int end = -1;

        if(!dijkstraUseTextInput && lastSelectedVertices.size() == 2)
        {
            start = lastSelectedVertices.remove();
            end = lastSelectedVertices.remove();
        }
        else if(dijkstraStartField.getText().length() != 0 && dijkstraEndField.getText().length()!= 0)
        {
            start = Integer.parseInt(dijkstraStartField.getText());
            end = Integer.parseInt(dijkstraEndField.getText());
        }
        else{
            createAlertWindow("Dijkstra", "Wrong input!");
        }

        runDijkstra(start, end);
    }

    private void runDijkstra(int start, int end) {
        (new Thread(() -> {
            try{
                clearLastDijkstraPath();
                DijkstraResult dr = Dijkstra.dijkstra(graph, start);
                Integer[] path = Dijkstra.getPath(dr, end).toArray(new Integer[0]);

                for(int i = 1 ;i < path.length;i++)
                {
                    int u = path[i-1];
                    int v = path[i];
                    graphController.getGraphModel().getVertex(u).setHighlighted(true);
                    graphController.getGraphModel().getVertex(v).setHighlighted(true);
                    graphController.getGraphModel().getEdge(u,v).setHighlighted(true);
                }
                graphController.drawGraph();
                lastDijkstraPath = path;
            }
            catch(Exception e)
            {

            }
        })).start();
    }

    @FXML
    public void runBfsButtonPressed(ActionEvent e) {
        try{
            int start;

            start = Integer.parseInt(bfsStartField.getText());
            (new Thread(() -> {
                try{

                    BFSResult br = BFS.bfs(graph, start);
                }
                catch(Exception e1)
                {

                }
            })).start();
        }
        catch (Exception nfe) {
            createAlertWindow("BFS", "Wrong input!");
        }
    }
    @FXML
    public void graphGenButtonPressed(ActionEvent e) {
        try{
            int rows, cols;
            double min, max;
            int seed = 0;

            rows = Integer.parseInt(graphGenRowsField.getText());
            cols = Integer.parseInt(graphGenColsField.getText());
            min = Double.parseDouble(graphGenMinField.getText());
            max = Double.parseDouble(graphGenMaxField.getText());
            if(!graphGenSeedField.getText().isEmpty())
                seed = Integer.parseInt(graphGenSeedField.getText());

            graphController.clearCanvas();
            lastDijkstraPath = null;

            graph = Graph.generateBidirectionalFromSeed(rows,cols,min,max,seed);
            graphController.loadGraph(graph);
        }
        catch (Exception nfe) {
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

    }

    @FXML
    public void saveButtonPressed(ActionEvent e) {

    }

    @FXML
    public void clearGraphButtonPressed(ActionEvent e) {
        graphController.clearCanvas();
        lastDijkstraPath = null;
    }
}