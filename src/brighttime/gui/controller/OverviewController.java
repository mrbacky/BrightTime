package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskType2;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {
    
    @FXML
    private TableView<TaskType2> tblTasks;
    @FXML
    private TableColumn<TaskType2, String> colName;
    @FXML
    private TableColumn<TaskType2, String> colHours;
    @FXML
    private TableColumn<TaskType2, String> colCost;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;
    
    private IMainModel mainModel;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;
    @FXML
    private JFXDatePicker datePickerStart;
    @FXML
    private JFXDatePicker datePickerEnd;

    //TODO: Very basic and lacking implementation made to check the connection between the view and database.
    public OverviewController() {
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
    
    void initializeView() {
        System.out.println("in Overview page");
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        setTable();
        selectProject();
        listenDatePickerStart();
        listenDatePickerEnd();
    }
    
    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
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
    
    private void setValidators() {
        validationManager.selectionValidation(cboClient, "No client selected.");
        validationManager.selectionValidation(cboProject, "No project selected.");
    }
    
    private void setTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("totalDurationString"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("totalCostString"));
        colCost.setStyle("-fx-alignment: CENTER-RIGHT;");
        tblTasks.setItems(mainModel.getTaskList());
        try {
            mainModel.getAllTasks();
        } catch (ModelException ex) {
            alertManager.showAlert("Could not get the tasks.", "An error occured: " + ex.getMessage());
        }
    }
    
    private void selectProject() {
        cboProject.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(newVal, datePickerStart.getValue(), datePickerEnd.getValue()));
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by project.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }
    
    private void listenDatePickerStart() {
        datePickerStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerEnd.getValue() != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboProject.getValue(), newValue, datePickerEnd.getValue()));
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }
    
    private void listenDatePickerEnd() {
        datePickerEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerStart.getValue() != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboProject.getValue(), datePickerStart.getValue(), newValue));
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }
    
}
