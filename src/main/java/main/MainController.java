package main;

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
    private int[] lastDijkstraPath;

    private final Deque<Integer> lastSelectedVertices;

    private boolean dijkstraUseTextInput;
    private boolean bfsUseTextInput;

    private boolean bfsResultCleared;

    public MainController() {
        this.lastDijkstraPath = null;
        this.dijkstraUseTextInput = true;
        this.bfsUseTextInput = true;
        this.lastSelectedVertices = new LinkedList<>();
        this.bfsResultCleared = false;
    }

    @FXML
    public void initialize() {
        // for testing purposes
        graph = Graph.generateBidirectionalFromSeed(10, 10, 0, 1, 0);
        graphController.loadGraph(graph);

        // here goes vertex click logic
        graphController.setOnClickEvent((x, y) -> {
            clearLastDijkstraPath();
            clearBfs();

            // cursed
            int vertex = graph.xyToIndex(y, x);
            graphController.getGraphModel().getVertex(vertex).setHighlighted(true);

            // removes all duplicates
            if (lastSelectedVertices.size() > 0 && lastSelectedVertices.contains(vertex)) {
                lastSelectedVertices.remove(vertex);
                graphController.getGraphModel().getVertex(vertex).setHighlighted(false);
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

    private synchronized void clearLastDijkstraPath() {
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

        int start = -1;
        int end = -1;

        if(!dijkstraUseTextInput && lastSelectedVertices.size() == 2) {
            start = lastSelectedVertices.remove();
            end = lastSelectedVertices.remove();
        }
        else if(dijkstraStartField.getText().length() != 0 && dijkstraEndField.getText().length()!= 0) {
            start = Integer.parseInt(dijkstraStartField.getText());
            end = Integer.parseInt(dijkstraEndField.getText());
        }
        else {
            createAlertWindow("Dijkstra", "Wrong input!");
        }

        runDijkstra(start, end);
    }

    private void runDijkstra(int start, int end) {
        new Thread(() -> {
            try {
                clearLastDijkstraPath();
                DijkstraResult dr = Dijkstra.dijkstra(graph, start);
                int[] path = Dijkstra.getPath(dr, end).stream()
                        .mapToInt(n -> n)
                        .toArray();

                for (int i = 1; i < path.length; i++) {
                    int u = path[i - 1];
                    int v = path[i];
                    graphController.getGraphModel().getVertex(u).setHighlighted(true);
                    graphController.getGraphModel().getVertex(v).setHighlighted(true);
                    graphController.getGraphModel().getEdge(u, v).setHighlighted(true);
                }
                graphController.drawGraph();
                lastDijkstraPath = path;
            } catch (Exception e) {
            }
        }).start();
    }


    @FXML
    private void bfsTextInputChecked()
    {
        clearLastDijkstraPath();
        clearBfs();

        bfsUseTextInput = !bfsUseTextInput;
        bfsStartField.setVisible(bfsUseTextInput);
        bfsStartField.setEditable(bfsUseTextInput);
    }


    private synchronized void clearBfs()
    {
        if(bfsResultCleared==true)
            return;

        for(int i =0; i < graph.getRows() * graph.getCols(); i++)
        {
            graphController.getGraphModel().getVertex(i).setHighlighted(false);
        }
        graphController.drawGraph();

        bfsResultCleared = true;
    }


    @FXML
    public void runBfsButtonPressed(ActionEvent e) {
        try{
            int start = -1;

            if(!bfsUseTextInput && lastSelectedVertices.size() > 0)
            {
                start = lastSelectedVertices.remove();
            }
            else if(bfsStartField.getText().length() != 0)
            {
                start = Integer.parseInt(bfsStartField.getText());
            }
            else
            {
                createAlertWindow("BFS","Wrong input!");
            }

            runBfs(start);
        }
        catch (Exception nfe) {
            createAlertWindow("BFS", "Wrong input!");
        }
    }


    private void runBfs(int start) {
        clearBfs();
        clearLastDijkstraPath();

        BFSResult br = BFS.bfs(graph, start);
        for(int i =0; i< graph.getCols() * graph.getRows();i++)
        {
            graphController.getGraphModel().getVertex(i).setHighlighted(true);
        }
        graphController.drawGraph();
        bfsResultCleared = false;
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
            graphController.drawGraph();
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File fileToOpen = fileChooser.showOpenDialog(null);

        try{
            if(fileToOpen != null)
            {
                graphController.clearCanvas();
                lastDijkstraPath = null;
                bfsResultCleared = false;
                graph = Graph.readFromFile(fileToOpen);
                graphController.loadGraph(graph);
                graphController.drawGraph();
            }
            else
            {

            }
        }
        catch(Exception exe)
        {

        }
    }

    @FXML
    public void saveButtonPressed(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File f = fc.showSaveDialog(null);

        try{
            if(f != null)
            {
                Graph.writeToFile(graph, f.getPath());
            }
            else
            {

            }
        }
        catch(Exception exe)
        {

        }
    }

    @FXML
    public void clearGraphButtonPressed(ActionEvent e) {
        graphController.clearCanvas();
        lastDijkstraPath = null;
        bfsResultCleared = false;
    }
}