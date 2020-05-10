package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

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
    @FXML
    private HBox hBoxDateTime;

    private final String TASK_MANUAL_FXML = "/brighttime/gui/view/CreateTaskManually.fxml";

    private IMainModel mainModel;
    private TimeTrackerController timeTrackerContr;
    private CreateTaskManuallyController createTaskManuallyContr;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;
    private Boolean manualMode;

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
        timeTrackerContr.injectCreateTaskController(this);
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
                    if (manualMode) {
                        LocalDateTime startDateTime = createTaskManuallyContr.getStartTime();
                        LocalDateTime endDateTime = createTaskManuallyContr.getEndTime();
                        TaskEntry entry = new TaskEntry(task, task.getDescription(), startDateTime, endDateTime);
                        List<TaskEntry> list = new ArrayList();
                        list.add(entry);
                        task.setTaskEntryList(list);
                    }
                    mainModel.addTask(task);
                    timeTrackerContr.initializeView();
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

    void manualMode() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_MANUAL_FXML));
            Parent root = fxmlLoader.load();
            createTaskManuallyContr = fxmlLoader.getController();
            hBoxDateTime.getChildren().add(root);
            manualMode = true;

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void normalMode() {
        hBoxDateTime.getChildren().clear();
    }

}
