package graph.control;

import javafx.scene.paint.Color;

public abstract class Highlightable {
    private boolean isHighlighted;
    private Color highlightColor;
    private Color defaultColor;

    protected Highlightable(Color highlightColor, Color defaultColor) {
        this.highlightColor = highlightColor;
        this.defaultColor = defaultColor;
        this.isHighlighted = false;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public boolean isHighlighted() {
        return this.isHighlighted;
    }

    public void toggleHighlight() {
        this.isHighlighted = !this.isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Color getColor() {
        return (isHighlighted) ? this.highlightColor : this.defaultColor;
    }
}
