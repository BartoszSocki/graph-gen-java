package com.wiaczek.socki.graphalgogui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class HelloController {

    @FXML
    private TextField graph_gen_rows_field;
    @FXML
    private TextField graph_gen_cols_field;
    @FXML
    private TextField graph_gen_min_field;
    @FXML
    private TextField graph_gen_max_field;
    @FXML
    private TextField graph_gen_seed_field;

    @FXML
    private TextField dijkstra_start_field;
    @FXML
    private TextField dijkstra_end_field;

    @FXML
    private TextField bfs_start_field;
    @FXML
    public void run_dijkstra_button_pressed(ActionEvent e)
    {
        try{
            int start, end;

            start = Integer.parseInt(dijkstra_start_field.getText());
            end = Integer.parseInt(dijkstra_end_field.getText());

            System.out.println("Run dijkstra!");
            System.out.println(start);
            System.out.println(end);

        }
        catch (Exception nfe)
        {
            create_alert_window("Dijkstra:", "Wrong input! (" + nfe.getMessage() + ")");
        }
    }
    @FXML
    public void run_bfs_button_pressed(ActionEvent e)
    {
        try{
            int start;

            start = Integer.parseInt(bfs_start_field.getText());

            System.out.println("Run bfs!");
            System.out.println(start);

        }
        catch (Exception nfe)
        {
            create_alert_window("BFS:", "Wrong input! (" + nfe.getMessage() + ")");
        }
    }
    @FXML
    public void graph_gen_button_pressed(ActionEvent e)
    {
        try{
            int rows, cols;
            double min, max;
            int seed = 0;

            rows = Integer.parseInt(graph_gen_rows_field.getText());
            cols = Integer.parseInt(graph_gen_cols_field.getText());
            min = Double.parseDouble(graph_gen_min_field.getText());
            max = Double.parseDouble(graph_gen_max_field.getText());
            if(!graph_gen_seed_field.getText().isEmpty())
                seed = Integer.parseInt(graph_gen_seed_field.getText());

            System.out.println("Generate graph");
            System.out.println(rows);
            System.out.println(cols);
            System.out.println(min);
            System.out.println(max);
            System.out.println(seed);
        }
        catch (Exception nfe)
        {
            create_alert_window("Generate graph:", "Wrong input! (" + nfe.getMessage() + ")");
        }
    }

    private void create_alert_window(String title, String msg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Graphalgo - error");
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void load_from_file_button_pressed(ActionEvent e) {
        System.out.println("LOAD FROM FILE!");
    }

    @FXML
    public void save_button_pressed(ActionEvent e) {
        System.out.println("SAVE BUTTON PRESSED!");

    }
}