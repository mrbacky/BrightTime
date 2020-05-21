package brighttime.gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Modality;

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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.initModality(Modality.APPLICATION_MODAL);
        //alert.setTitle("ERROR!");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
        }
    }

    public boolean showConfirmation(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        //alert.setTitle("CONFIRMATION");        
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

}
