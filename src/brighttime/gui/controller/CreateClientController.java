package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateClientController implements Initializable {

    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXTextField txtName;

    private ManageClientsController contr;
    private IMainModel mainModel;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;

    public CreateClientController() {
        this.alertManager = new AlertManager();
        this.validationManager = new ValidationManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void injectManageClientsController(ManageClientsController contr) {
        this.contr = contr;
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void initializeView() throws IOException {
        System.out.println("in CreateClient page");
        validationManager.inputValidation(txtName, "No name written.");
        addClient();
    }

    /**
     * Adds a new client.
     */
    private void addClient() {
        btnAdd.setOnAction((event) -> {
            if (!txtName.getText().trim().isEmpty()) {
                try {
                    mainModel.addClient(new Client(txtName.getText().trim()));
                    System.out.println("Action event is working!");
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not create the client.", "An error occured: " + ex.getMessage());
                }
            } else {
                alertManager.showAlert("No client name was entered.", "Please enter a name for the new client.");
            }
        });
    }

}
