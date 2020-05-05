package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateTaskController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    private ModelFacade modelManager;
    private final AlertManager alertManager;
//    private final ValidationManager validationManager;
    private IMainModel mainModel;
    @FXML
    private TextField textFieldTaskDescInput;
    @FXML
    private ComboBox<Client> comboBoxClient;
    @FXML
    private ComboBox<Project> comboBoxProject;
    private TimeTrackerController timeTrackerController;

    public CreateTaskController() {
        this.alertManager = new AlertManager();
//        this.validationManager = new ValidationManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void initializeView() throws IOException {
        System.out.println("in Creator page");
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
//        setValidators();
        addTask();
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void injectTimeTrackerController(TimeTrackerController timeTrackerController) {
        this.timeTrackerController = timeTrackerController;
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (mainModel.getClientList() != null) {
            try {
                mainModel.loadClients();
                comboBoxClient.getItems().clear();
                comboBoxClient.getItems().addAll(mainModel.getClientList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets the projects into the ComboBox.
     */
    private void setProjectsIntoComboBox() {
        comboBoxClient.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                if (mainModel.getProjectList() != null) {
                    try {
                        mainModel.loadProjects(newVal);
                        comboBoxProject.getItems().clear();
                        comboBoxProject.getItems().addAll(mainModel.getProjectList());
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
//    private void setValidators() {
//        validationManager.inputValidation(textFieldTaskDescInput, "No description added.");
//        validationManager.selectionValidation(comboBoxClient, "No client selected.");
//        validationManager.selectionValidation(comboBoxProject, "No project selected.");
//    }
    /**
     * Adds a new task.
     */
    private void addTask() {
        btnAdd.setOnAction((event) -> {
            if (!textFieldTaskDescInput.getText().trim().isEmpty() && !comboBoxProject.getSelectionModel().isEmpty()) {
                try {
                    Task task = new Task(textFieldTaskDescInput.getText().trim(), comboBoxProject.getSelectionModel().getSelectedItem());
                    System.out.println("all tasks before: + " + mainModel.getTasks());
                    mainModel.addTask(task);
                    timeTrackerController.initializeView();
                } catch (ModelException ex) {
                    Logger.getLogger(CreateTaskController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (textFieldTaskDescInput.getText().trim().isEmpty()) {
                alertManager.showAlert("No task description was entered.", "Please enter a description of the new task.");
            } else if (comboBoxClient.getSelectionModel().isEmpty()) {
                alertManager.showAlert("No client is selected.", "Please select a client.");
            } else {
                alertManager.showAlert("No project is selected.", "Please select a project.");
            }
        });
    }

}
