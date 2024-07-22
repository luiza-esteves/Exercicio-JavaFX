package circulos;
import javafx.scene.paint.Color;

public class CircleEditResult {
    private final Color color;
    private final double diameter;

    public CircleEditResult(Color color, double diameter) {
        this.color = color;
        this.diameter = diameter;
    }

    public Color getColor() {
        return color;
    }

    public double getDiameter() {
        return diameter;
    }
}