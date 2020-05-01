package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private JFXTextField txtDescription;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;

    private ModelFacade modelManager;

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
        System.out.println("in Creator page");
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        addTask();
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
                showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets the projects into the ComboBox.
     */
    private void setProjectsIntoComboBox() {
        cboClient.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                if (modelManager.getProjectList() != null) {
                    try {
                        modelManager.loadProjects(newVal);
                        cboProject.getItems().clear();
                        cboProject.getItems().addAll(modelManager.getProjectList());
                    } catch (ModelException ex) {
                        showAlert("Could not get the projects.", "An error occured: " + ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Adds a new task.
     */
    private void addTask() {
        btnAdd.setOnAction((event) -> {
            if (!txtDescription.getText().trim().isEmpty() && !cboProject.getSelectionModel().isEmpty()) {
                try {
                    Task task = new Task(txtDescription.getText().trim(), cboProject.getSelectionModel().getSelectedItem());
                    modelManager.addTask(task);
                    System.out.println("action event is working!");
                } catch (ModelException ex) {
                    showAlert("Could not create the task.", "An error occured: " + ex.getMessage());
                }
            } else if (txtDescription.getText().trim().isEmpty()) {
                showAlert("No task description was entered.", "Please enter a description of the new task.");
            } else if (cboClient.getSelectionModel().isEmpty()) {
                showAlert("No client is selected.", "Please select a client.");
            } else {
                showAlert("No project is selected.", "Please select a project.");
            }
        });
    }

    /**
     * Sets all validator.
     */
    private void setValidators() {
        inputValidation();
        selectionValidation(cboClient, "No client selected.");
        selectionValidation(cboProject, "No project selected.");
    }

    /**
     * Validates the input of the task description text field.
     */
    private void inputValidation() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        txtDescription.getValidators().add(validator);
        validator.setMessage("No description added.");
        txtDescription.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtDescription.validate();
            }
        });
    }

    /**
     * Validates the selection of a ComboBox.
     *
     * @param comboBox
     * @param message
     */
    private void selectionValidation(JFXComboBox comboBox, String message) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        comboBox.getValidators().add(validator);
        validator.setMessage(message);
        comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                comboBox.validate();
            }
        });
    }

    /**
     * Shows an error dialog.
     */
    private void showAlert(String headerText, String message) {
        //TO DO: Alert is acceptable, but customize further if time permits.
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        //alert.setTitle("ERROR!");
        alert.setHeaderText(headerText);
        alert.show();
        if (alert.getResult() == ButtonType.OK) {
        }
    }

}
