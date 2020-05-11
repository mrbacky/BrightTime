package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateTaskController implements Initializable {

    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;

    private IMainModel mainModel;
    private TimeTrackerController timeTrackerContr;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;

    public CreateTaskController() {
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

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void initializeView() throws IOException {
        System.out.println("in Creator page");
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        addTask();
    }

    public void injectTimeTrackerController(TimeTrackerController timeTrackerContr) {
        this.timeTrackerContr = timeTrackerContr;
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (mainModel.getClientList() != null) {
            try {
                mainModel.loadClients();
                cboClient.getItems().clear();
                cboClient.getItems().addAll(mainModel.getClientList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets the projects into the ComboBox.
     */
    private void setProjectsIntoComboBox() {
        cboClient.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                if (mainModel.getProjectList() != null) {
                    try {
                        mainModel.loadProjects(newVal);
                        cboProject.getItems().clear();
                        cboProject.getItems().addAll(mainModel.getProjectList());
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not get the projects.", "An error occured: " + ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Sets all validator.
     */
    private void setValidators() {
        validationManager.inputValidation(txtDescription, "No description added.");
        validationManager.selectionValidation(cboClient, "No client selected.");
        validationManager.selectionValidation(cboProject, "No project selected.");
    }

    /**
     * Adds a new task.
     */
    private void addTask() {
        btnAdd.setOnAction((event) -> {
            if (!txtDescription.getText().trim().isEmpty() && !cboProject.getSelectionModel().isEmpty()) {
                try {
                    Task task = new Task(txtDescription.getText().trim(), cboProject.getSelectionModel().getSelectedItem());
                    mainModel.addTask(task);
                    Platform.runLater(() -> {
                        timeTrackerContr.initializeView();
                    });

                    System.out.println("action event is working!");
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not create the task.", "An error occured: " + ex.getMessage());
                }
            } else if (txtDescription.getText().trim().isEmpty()) {
                alertManager.showAlert("No task description was entered.", "Please enter a description of the new task.");
            } else if (cboClient.getSelectionModel().isEmpty()) {
                alertManager.showAlert("No client is selected.", "Please select a client.");
            } else {
                alertManager.showAlert("No project is selected.", "Please select a project.");
            }
        });
    }

}
