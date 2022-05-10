package gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Pane pane;

    private Grid grid;

    @FXML
    public void initialize() {
        int width = 10;
        int height = 20;
        grid = new Grid(pane, height, width);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid.add(i, j, new Node());
            }
        }

//        grid.addBidirectionalEdge(0, 1, 1);
//        grid.addEdge(0, 1, 1);
        grid.getNode(0, 0).toggleHighlight();
    }
}