package circulos;

import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CircleEditor {

    public CircleEditResult editCirculo(Color corAtual, double diametroAtual) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Editar Círculo");

        ColorPicker colorPicker = new ColorPicker(corAtual);
        TextField diameterField = new TextField(String.valueOf(diametroAtual));

        //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextFormatter.html#TextFormatter-java.util.function.UnaryOperator-
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*\\.?[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        diameterField.setTextFormatter(formatter);

        Button saveButton = new Button("Salvar");
        Button cancelButton = new Button("Cancelar");

        final CircleEditResult[] resultHolder = new CircleEditResult[1];

        saveButton.setOnAction(e -> {
            try {
                double novoDiametro = Double.parseDouble(diameterField.getText());

                if (novoDiametro > 800) {
                    showAlert("Diâmetro Inválido", "O diâmetro não pode ser maior que 800.");
                    return;
                }

                Color novaCor = colorPicker.getValue();
                resultHolder[0] = new CircleEditResult(novaCor, novoDiametro);
                stage.close();
            } catch (NumberFormatException ex) {
                showAlert("Diâmetro Inválido", "Por favor, insira um número válido para o diâmetro.");
            }
        });

        cancelButton.setOnAction(e -> {
            resultHolder[0] = null;
            stage.close();
        });

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Cor:"), colorPicker,
                new Label("Diâmetro:"), diameterField,
                buttons
        );

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();

        return resultHolder[0];
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
