package circulos;

import javafx.scene.control.Button;

public class ButtonManager {
    private final String corNormal = "#FFEFD5";
    private final String corDestaque = "#FF6347";
    
    public void configurarBotao(Button botao) {
        botao.setOnMouseEntered(event -> botao.setStyle("-fx-background-color: " + corDestaque + "; -fx-border-color: black; -fx-border-radius: 6;"));
        botao.setOnMouseExited(event -> botao.setStyle("-fx-background-color: " + corNormal + "; -fx-border-color: black; -fx-border-radius: 6;"));
    }
}
