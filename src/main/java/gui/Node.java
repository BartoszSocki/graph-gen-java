package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node {
    private final Circle circle;
    private boolean isHighlighted;

    public Node() {
        this.circle = new Circle();
        this.circleSetup();

        this.isHighlighted = false;
    }

    private void circleSetup() {
        this.circle.setFill(Color.GREEN);
        this.circle.setStroke(Color.BLACK);
        this.circle.setRadius(10);

        this.circle.setOnMouseClicked((event) -> {
            this.toggleHighlight();
        });
    }

    public void toggleHighlight() {
        this.isHighlighted = !this.isHighlighted;
        if (this.isHighlighted) {
            this.circle.setFill(Color.WHITE);
        } else {
            this.circle.setFill(Color.GREEN);
        }
    }

    public Circle getCircle() {
        return this.circle;
    }
}
