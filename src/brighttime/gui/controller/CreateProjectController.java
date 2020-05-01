package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.ModelFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
public class CreateProjectController implements Initializable {

    @FXML
    private HBox hBoxItemElements;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXButton btnAdd;

    private ModelFacade modelManager;
    private final AlertManager alertManager;

    public CreateProjectController() {
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
        System.out.println("in CreateProject page");
        setClientsIntoComboBox();
        inputValidation();
        selectionValidation();
        addClient();
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (modelManager.getClientList() != null) {
            try {
                modelManager.loadClients();
                cboClient.getItems().clear();
                cboClient.getItems().addAll(modelManager.getClientList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
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
     * Validates the selection of the ComboBox.
     */
    private void selectionValidation() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        cboClient.getValidators().add(validator);
        validator.setMessage("No client selected.");
        cboClient.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cboClient.validate();
            }
        });
    }

    /**
     * Adds a new project.
     */
    private void addClient() {
        btnAdd.setOnAction((event) -> {
            if (!txtName.getText().trim().isEmpty() && !cboClient.getSelectionModel().isEmpty()) {
                try {
                    modelManager.addProject(new Project(txtName.getText().trim(), cboClient.getSelectionModel().getSelectedItem()));
                    System.out.println("Action event is working!");
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not create the project.", "An error occured: " + ex.getMessage());
                }
            } else if (txtName.getText().trim().isEmpty()) {
                alertManager.showAlert("No project name was entered.", "Please enter a name for the new project.");
            } else {
                alertManager.showAlert("No client is selected.", "Please select a client.");
            }
        });
    }

}
