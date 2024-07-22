package circulos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Pane drawingPane;

    @FXML
    private Button botaoAdicionar;

    @FXML
    private Button botaoEditar;

    @FXML
    private Button botaoExcluir;

    private Circle circuloSelecionado;

    private Circle ultimoCirculoSelecionado = null;

    private final ChoiceBox<String> assetClass = new ChoiceBox<>();

    private final List<String> cores = new ArrayList<>();


    @FXML
    public void adicionarCirculo() {
        CircleEditor editor = new CircleEditor();
        CircleEditResult resultado = editor.editCirculo(Color.WHITE, 100.0);

        if (resultado != null) {
            Circle circulo = new Circle(resultado.getDiameter() / 2);
            circulo.setFill(resultado.getColor());
            circulo.setCenterX(100);
            circulo.setCenterY(100);
            circulo.setStroke(Color.BLACK);
            circulo.setStrokeWidth(1);

            circulo.setUserData(new double[]{0.0, 0.0, circulo.getCenterX(), circulo.getCenterY()});

            circulo.setOnMouseDragged(evento -> {
                double[] userData = (double[]) circulo.getUserData();
                double newX = userData[2] + evento.getX() - userData[0];
                double newY = userData[3] + evento.getY() - userData[1];
                
                if (isWithinBounds(newX, newY, circulo)) {
                    circulo.setCenterX(newX);
                    circulo.setCenterY(newY);
                }

                circulo.setUserData(new double[]{0, 0, 0, 0});
            });

            circulo.setOnMouseClicked(evento -> {
                if (ultimoCirculoSelecionado != null) {
                    ultimoCirculoSelecionado.setEffect(null);
                }

                botaoEditar.setVisible(true);
                botaoExcluir.setVisible(true);
                circuloSelecionado = (Circle) evento.getSource();
                destacarCirculoSelecionado();

                ultimoCirculoSelecionado = circuloSelecionado;
            });

            drawingPane.getChildren().add(circulo);
        }
    }

    @FXML
    public void editarCirculo() {
        if (circuloSelecionado != null) {
            CircleEditor editor = new CircleEditor();
            CircleEditResult resultado = editor.editCirculo((Color) circuloSelecionado.getFill(), circuloSelecionado.getRadius() * 2);
            if (resultado != null) {
                circuloSelecionado.setFill(resultado.getColor());
                circuloSelecionado.setRadius(resultado.getDiameter() / 2);
            }
        }
    }

    @FXML
    public void removerCirculo() {
        if (circuloSelecionado != null) {
            drawingPane.getChildren().remove(circuloSelecionado);
            circuloSelecionado = null;
            botaoEditar.setVisible(false);
            botaoExcluir.setVisible(false);
        }
    }

    private void destacarCirculoSelecionado() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setColor(Color.YELLOW);
        circuloSelecionado.setEffect(dropShadow);
    }

    private void limparDestaqueCirculos() {
        if (ultimoCirculoSelecionado != null) {
            ultimoCirculoSelecionado.setEffect(null);
            ultimoCirculoSelecionado = null;
        }
    }

    private void esconderBotoesEditarRemover() {
        botaoEditar.setVisible(false);
        botaoExcluir.setVisible(false);
    }

    @FXML
    private void moverCirculo(KeyCode keyCode) {
        if (circuloSelecionado.getEffect() != null) {
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
            System.out.println("Moving Circle: X=" + newX + ", Y=" + newY);

            if (isWithinBounds(newX, newY, circuloSelecionado)) {
                circuloSelecionado.setCenterX(newX);
                circuloSelecionado.setCenterY(newY);
            }
        }
    }
    
    private boolean isWithinBounds(double x, double y, Circle circle) {
        double radius = circle.getRadius();
        return x - radius >= 0 && x + radius <= drawingPane.getWidth() &&
               y - radius >= 0 && y + radius <= drawingPane.getHeight();
    }

    @FXML
    private void onKeyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            moverCirculo(event.getCode());
        }
    }
    
    private void configurarBotao(Button botao, String corNormal, String corDestaque) {
        botao.setOnMouseEntered(event -> botao.setStyle("-fx-background-color: " + corDestaque + "; -fx-border-color: black; -fx-border-radius: 6;"));
        botao.setOnMouseExited(event -> botao.setStyle("-fx-background-color: " + corNormal + "; -fx-border-color: black; -fx-border-radius: 6;"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botaoEditar.setVisible(false);
        botaoExcluir.setVisible(false);
        String corNormal = "#FFEFD5";
        String corDestaque = "#FF6347";
        
        configurarBotao(botaoAdicionar, corNormal, corDestaque);
        configurarBotao(botaoEditar, corNormal, corDestaque);
        configurarBotao(botaoExcluir, corNormal, corDestaque);

        drawingPane.setOnMouseClicked(evento -> {
            boolean cliqueEmCirculo = false;
            for (Node node : drawingPane.getChildren()) {
                if (node instanceof Circle && node.contains(evento.getX(), evento.getY())) {
                    cliqueEmCirculo = true;
                    break;
                }
            }

            if (!cliqueEmCirculo) {
                limparDestaqueCirculos();
                esconderBotoesEditarRemover();
            }
        });
        
        drawingPane.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        drawingPane.setFocusTraversable(true);
    }
}
