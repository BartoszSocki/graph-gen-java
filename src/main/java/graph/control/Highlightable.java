package graph.control;

import javafx.scene.paint.Color;

public abstract class Highlightable {
    private boolean isHighlighted;
    private final Color highlightColor;
    private final Color defaultColor;
    private Color currentColor;

    protected Highlightable(Color highlightColor, Color defaultColor) {
        this.highlightColor = highlightColor;
        this.defaultColor = defaultColor;
        this.currentColor = defaultColor;
        this.isHighlighted = false;
    }

    public void toggleHighlight() {
        currentColor = isHighlighted ? highlightColor : defaultColor;
    }

    public boolean isHighlighted() {
        return this.isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
        if (isHighlighted)
            currentColor = highlightColor;
        else
            currentColor = defaultColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }
}
