package circulos;
import javafx.scene.paint.Color;

public class CircleEditResult {
        private Color color;
        private double diameter;

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