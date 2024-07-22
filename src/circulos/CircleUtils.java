package circulos;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CircleUtils {
    public static boolean isWithinBounds(double x, double y, Circle circle, Pane pane) {
        double radius = circle.getRadius();
        return x - radius >= 0 && x + radius <= pane.getWidth() &&
               y - radius >= 0 && y + radius <= pane.getHeight();
    }
}
