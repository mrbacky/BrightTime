package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.concretes.ClientModel;
import brighttime.gui.model.concretes.ProjectModel;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.IClientModel;
import brighttime.gui.model.interfaces.IProjectModel;
import brighttime.gui.model.interfaces.ITaskModel;
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
import javafx.scene.control.Button;
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
    private Button btnAdd;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;

    private IClientModel clientModel;
    private IProjectModel projectModel;
    private ITaskModel taskModel;

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
        projectModel = new ProjectModel();
        taskModel = new TaskModel();
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        addTask();
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (clientModel.getClientList() != null) {
            try {
                clientModel.getClients();
                cboClient.getItems().clear();
                cboClient.getItems().addAll(clientModel.getClientList());
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
                if (projectModel.getProjectList() != null) {
                    try {
                        projectModel.getProjects(newVal);
                        cboProject.getItems().clear();
                        cboProject.getItems().addAll(projectModel.getProjectList());
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
            if (!txtDescription.getText().isEmpty() && !cboProject.getSelectionModel().isEmpty()) {
                try {
                    Task task = new Task(txtDescription.getText().trim(), cboProject.getSelectionModel().getSelectedItem());
                    taskModel.addTask(task);
                    System.out.println("action event is working!");
                } catch (ModelException ex) {
                    showAlert("Could not create the task.", "An error occured: " + ex.getMessage());
                }
            } else if (txtDescription.getText().isEmpty()) {
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
