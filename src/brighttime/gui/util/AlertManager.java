package brighttime.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 *
 * @author annem
 */
public class AlertManager {

    /**
     * Shows an error dialog.
     *
     * @param headerText
     * @param message
     */
    public void showAlert(String headerText, String message) {
        //TO DO: The alert is acceptable, but customize further if time permits.
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        //alert.setTitle("ERROR!");
        alert.setHeaderText(headerText);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
        }
    }

    public void showSuccess(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
