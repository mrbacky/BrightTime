package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.ModelFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateClientController implements Initializable {

    @FXML
    private HBox hBoxItemElements;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXTextField txtName;

    private ModelFacade modelManager;
    private final AlertManager alertManager;

    public CreateClientController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void injectModelManager(ModelFacade modelManager) {
        this.modelManager = modelManager;
    }

    void initializeView() throws IOException {
        System.out.println("in CreateClient page");
        inputValidation();
        addClient();
    }

    /**
     * Validates the input of the TextField.
     */
    private void inputValidation() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        txtName.getValidators().add(validator);
        validator.setMessage("No name written.");
        txtName.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtName.validate();
            }
        });
    }

    /**
     * Adds a new client.
     */
    private void addClient() {
        btnAdd.setOnAction((event) -> {
            if (!txtName.getText().trim().isEmpty()) {
                try {
                    modelManager.addClient(new Client(txtName.getText().trim()));
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
