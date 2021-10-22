package de.nuton.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class UIUtils {

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setContentText("Es ist nichts zum speichern vorhanden.");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void showErrorAlert(String content) {
        showErrorAlert("Error!", content);
    }

}
