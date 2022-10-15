package hr.algebra.codenames.utils;

import javafx.scene.control.Alert;

public class DialogUtils {
    private DialogUtils(){}

    // TODO: 15.10.2022. Create your own dialogs!
    public static void showInformationDialog(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showErrorDialog(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
