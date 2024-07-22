package circulos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    private CircleManager circleManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        circleManager = new CircleManager(drawingPane, botaoEditar, botaoExcluir);
        botaoEditar.setVisible(false);
        botaoExcluir.setVisible(false);

        // Configura os botões
        configurarBotao(botaoAdicionar, "#FFEFD5", "#FF6347");
        configurarBotao(botaoEditar, "#FFEFD5", "#FF6347");
        configurarBotao(botaoExcluir, "#FFEFD5", "#FF6347");

        drawingPane.setOnMouseClicked(evento -> {
            boolean cliqueEmCirculo = false;
            for (Node node : drawingPane.getChildren()) {
                if (node instanceof Circle && node.contains(evento.getX(), evento.getY())) {
                    cliqueEmCirculo = true;
                    break;
                }
            }

            if (!cliqueEmCirculo) {
                circleManager.limparDestaqueCirculos();
                circleManager.esconderBotoesEditarRemover();
            }
        });

        drawingPane.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        drawingPane.setFocusTraversable(true);
    }

    @FXML
    public void adicionarCirculo() {
        CircleEditor editor = new CircleEditor();
        CircleEditResult resultado = editor.editCirculo(Color.WHITE, 100.0);

        if (resultado != null) {
            circleManager.adicionarCirculo(resultado.getColor(), resultado.getDiameter());
        }
    }

    @FXML
    public void editarCirculo() {
        if (circleManager.getCirculoSelecionado() != null) {
            CircleEditor editor = new CircleEditor();
            CircleEditResult resultado = editor.editCirculo((Color) circleManager.getCirculoSelecionado().getFill(), circleManager.getCirculoSelecionado().getRadius() * 2);
            if (resultado != null) {
                circleManager.editarCirculo(resultado.getColor(), resultado.getDiameter());
            }
        }
    }

    @FXML
    public void removerCirculo() {
        circleManager.removerCirculo();
    }

    @FXML
    private void onKeyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            circleManager.moverCirculo(event.getCode());
        }
    }

    private void configurarBotao(Button botao, String corNormal, String corDestaque) {
        botao.setOnMouseEntered(event -> botao.setStyle("-fx-background-color: " + corDestaque + "; -fx-border-color: black; -fx-border-radius: 6;"));
        botao.setOnMouseExited(event -> botao.setStyle("-fx-background-color: " + corNormal + "; -fx-border-color: black; -fx-border-radius: 6;"));
    }
}
