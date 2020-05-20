package brighttime.gui.util;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class ActiveFilterButtonController implements Initializable {

    @FXML
    private Label lblActiveFilter;
    @FXML
    private JFXButton btnRemoveFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public Label getLblActiveFilter() {
        return lblActiveFilter;
    }

    public JFXButton getBtnRemoveFilter() {
        return btnRemoveFilter;
    }

}
