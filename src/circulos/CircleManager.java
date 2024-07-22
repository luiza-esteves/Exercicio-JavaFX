package circulos;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;

public class CircleManager {
    private Pane drawingPane;
    private Circle circuloSelecionado;
    private Circle ultimoCirculoSelecionado;
    private Button botaoEditar;
    private Button botaoExcluir;

    public CircleManager(Pane drawingPane, Button botaoEditar, Button botaoExcluir) {
        this.drawingPane = drawingPane;
        this.botaoEditar = botaoEditar;
        this.botaoExcluir = botaoExcluir;
    }

    public void adicionarCirculo(Color cor, double diametro) {
        Circle circulo = new Circle(diametro / 2);
        circulo.setFill(cor);
        circulo.setCenterX(100);
        circulo.setCenterY(100);
        circulo.setStroke(Color.BLACK);
        circulo.setStrokeWidth(1);

        circulo.setUserData(new double[]{0.0, 0.0, circulo.getCenterX(), circulo.getCenterY()});

        circulo.setOnMouseDragged(evento -> {
            double[] userData = (double[]) circulo.getUserData();
            double newX = userData[2] + evento.getX() - userData[0];
            double newY = userData[3] + evento.getY() - userData[1];

            if (CircleUtils.isWithinBounds(newX, newY, circulo, drawingPane)) {
                circulo.setCenterX(newX);
                circulo.setCenterY(newY);
            }
            circulo.setUserData(new double[]{0, 0, 0, 0});
        });

        circulo.setOnMouseClicked(evento -> {
            if (ultimoCirculoSelecionado != null) {
                ultimoCirculoSelecionado.setEffect(null);
            }

            circuloSelecionado = circulo;
            destacarCirculoSelecionado();
            ultimoCirculoSelecionado = circuloSelecionado;
            mostrarBotoesEditarRemover();
        });

        drawingPane.getChildren().add(circulo);
    }

    public void editarCirculo(Color cor, double diametro) {
        if (circuloSelecionado != null) {
            circuloSelecionado.setFill(cor);
            circuloSelecionado.setRadius(diametro / 2);
        }
    }

    public void removerCirculo() {
        if (circuloSelecionado != null) {
            drawingPane.getChildren().remove(circuloSelecionado);
            circuloSelecionado = null;
            esconderBotoesEditarRemover();
        }
    }

    public void moverCirculo(javafx.scene.input.KeyCode keyCode) {
        if (circuloSelecionado != null && circuloSelecionado.getEffect() != null) {
            double moveX = 0;
            double moveY = 0;

            switch (keyCode) {
                case UP:
                    moveY = -10;
                    break;
                case DOWN:
                    moveY = 10;
                    break;
                case LEFT:
                    moveX = -10;
                    break;
                case RIGHT:
                    moveX = 10;
                    break;
                default:
                    break;
            }

            double newX = circuloSelecionado.getCenterX() + moveX;
            double newY = circuloSelecionado.getCenterY() + moveY;

            if (CircleUtils.isWithinBounds(newX, newY, circuloSelecionado, drawingPane)) {
                circuloSelecionado.setCenterX(newX);
                circuloSelecionado.setCenterY(newY);
            }
        }
    }

    private void destacarCirculoSelecionado() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setColor(Color.YELLOW);
        circuloSelecionado.setEffect(dropShadow);
    }

    private void mostrarBotoesEditarRemover() {
        botaoEditar.setVisible(true);
        botaoExcluir.setVisible(true);
    }

    void esconderBotoesEditarRemover() {
        botaoEditar.setVisible(false);
        botaoExcluir.setVisible(false);
    }

    public void limparDestaqueCirculos() {
        if (ultimoCirculoSelecionado != null) {
            ultimoCirculoSelecionado.setEffect(null);
            ultimoCirculoSelecionado = null;
        }
    }

    public Circle getCirculoSelecionado() {
        return circuloSelecionado;
    }
}
