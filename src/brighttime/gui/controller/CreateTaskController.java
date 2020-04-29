package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.concretes.ClientModel;
import brighttime.gui.model.interfaces.IClientModel;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateTaskController implements Initializable {

    @FXML
    private HBox hBoxItemElements;
    @FXML
    private TextField txtDescription;
    @FXML
    private Button btnAdd;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;

    private IClientModel clientModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void initializeView() throws IOException {
        System.out.println("in Creator page");
        clientModel = new ClientModel();
        setClientsIntoComboBox();
    }

    private void setClientsIntoComboBox() {
        if (clientModel.getClientList() != null) {
            try {
                clientModel.getClients();
                cboClient.getItems().clear();
                cboClient.getItems().addAll(clientModel.getClientList());
            } catch (ModelException ex) {
                showAlert(ex);
            }
        }

    }

    private void showAlert(Exception ex) {
        //TO DO: Customize alert properly.
        Alert a = new Alert(Alert.AlertType.ERROR, "An error occured: " + ex.getMessage(), ButtonType.OK);
        a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        a.show();
        if (a.getResult() == ButtonType.OK) {
        }
    }
    
}
