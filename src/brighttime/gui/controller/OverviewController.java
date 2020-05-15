package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {

    @FXML
    private TableView<TaskConcrete2> tbvTasks;

    @FXML
    private TableColumn<TaskConcrete2, String> colTaskDescription;
    @FXML
    private TableColumn<TaskConcrete2, String> colHours;
    @FXML
    private TableColumn<TaskConcrete2, String> colCost;

    private IMainModel mainModel;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;

    @FXML
    private AnchorPane apOverview;

    @FXML
    private JFXComboBox<User> cboUsers;
    @FXML
    private JFXComboBox<Client> cboClients;
    @FXML
    private JFXComboBox<Project> cboProjects;
    @FXML
    private JFXButton btnThisWeek;
    @FXML
    private JFXButton btnThisMonth;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnLastWeek;
    @FXML
    private JFXButton btnLastMonth;
    @FXML
    private JFXButton btnClearFilters;
    @FXML
    private BarChart<String, Double> barChartTasks;

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
        setUsersIntoComboBox();
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        setTable();
        selectUser();
        selectProject();
        listenDatePickerStart();
        listenDatePickerEnd();
        clearFilters();

        barChartTasks.setTitle("Amount of hours for each task");

        tbvTasks.getItems().addListener((ListChangeListener.Change<? extends TaskConcrete2> c) -> {
            setUpBarChart();
        });
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Sets the users into the ComboBox.
     */
    private void setUsersIntoComboBox() {
        if (mainModel.getUserList() != null) {
            try {
                mainModel.loadUsers();
                cboUsers.getItems().clear();
                cboUsers.getItems().addAll(mainModel.getUserList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the users.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (mainModel.getClientList() != null) {
            try {
                mainModel.loadClients();
                cboClients.getItems().clear();
                cboClients.getItems().addAll(mainModel.getClientList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets the projects into the ComboBox.
     */
    private void setProjectsIntoComboBox() {
        cboClients.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                if (mainModel.getProjectList() != null) {
                    try {
                        mainModel.loadProjects(newVal);
                        cboProjects.getItems().clear();
                        cboProjects.getItems().addAll(mainModel.getProjectList());
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not get the projects.", "An error occured: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void setValidators() {
        validationManager.selectionValidation(cboClients, "No client selected.");
        validationManager.selectionValidation(cboProjects, "No project selected.");
    }

    private void setTable() {
        colTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("totalDurationString"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("totalCostString"));
        tbvTasks.setItems(mainModel.getTaskList());
        try {
            mainModel.getAllTasks();
        } catch (ModelException ex) {
            alertManager.showAlert("Could not get the tasks.", "An error occured: " + ex.getMessage());
        }
    }

    private void selectUser() {
        cboUsers.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(newVal, cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));

                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by user.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    private void selectProject() {
        cboProjects.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), newVal, dpStartDate.getValue(), dpEndDate.getValue()));

                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by project.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    private void listenDatePickerStart() {
        dpStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dpEndDate.getValue() != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), newValue, dpEndDate.getValue()));
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    private void listenDatePickerEnd() {
        dpEndDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && dpStartDate.getValue() != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), newValue));

                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleThisWeek(ActionEvent event) {
        try {
            LocalDate firstDayOfThisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate lastDayOfThisWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            dpStartDate.setValue(firstDayOfThisWeek);
            dpEndDate.setValue(lastDayOfThisWeek);
            mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));
        } catch (ModelException ex) {
            alertManager.showAlert("Could not filter in this week", "An error occured: " + ex.getMessage());
        }
    }

    @FXML
    private void handleThisMonth(ActionEvent event) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate firstDayInThisMonth = today.withDayOfMonth(1);
            LocalDate endDayInThisMonth = today.withDayOfMonth(today.lengthOfMonth());

            dpStartDate.setValue(firstDayInThisMonth);
            dpEndDate.setValue(endDayInThisMonth);
            mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));

        } catch (ModelException ex) {
            alertManager.showAlert("Could not filter in this month", "An error occured: " + ex.getMessage());
        }
    }

    @FXML
    private void handleLastWeek(ActionEvent event) {
        try {
            LocalDate firstDayOfLastWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY);
            LocalDate lastDayOfLastWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.SUNDAY);

            dpStartDate.setValue(firstDayOfLastWeek);
            dpEndDate.setValue(lastDayOfLastWeek);

            mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));

        } catch (ModelException ex) {
            alertManager.showAlert("Could not filter in this week", "An error occured: " + ex.getMessage());
        }
    }

    @FXML
    private void handleLastMonth(ActionEvent event) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate lastMonth = today.minusMonths(1);
            LocalDate firstDayOfLastMonth = lastMonth.withDayOfMonth(1);
            LocalDate lastDayOfLastMonth = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

            dpStartDate.setValue(firstDayOfLastMonth);
            dpEndDate.setValue(lastDayOfLastMonth);

            mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));

        } catch (ModelException ex) {
            alertManager.showAlert("Could not filter in this month", "An error occured: " + ex.getMessage());
        }

    }

    private void clearFilters() {
        btnClearFilters.setOnAction((event) -> {
            try {
                cboUsers.getSelectionModel().clearSelection();
                cboClients.getSelectionModel().clearSelection();
                cboProjects.getSelectionModel().clearSelection();
                dpStartDate.setValue(null);
                dpEndDate.setValue(null);
                mainModel.getAllTasks();

            } catch (ModelException ex) {
                alertManager.showAlert("Could not clear the filters.", "An error occured: " + ex.getMessage());
            }
        });
    }

    private void setUpBarChart() {
        ObservableList<XYChart.Series<String, Double>> taskBars = FXCollections.observableArrayList();

        List<TaskConcrete2> taskList = mainModel.getTaskList();
        List<TaskConcrete2> tasksWithHours = taskList.stream().filter(task -> task.getTotalDurationSeconds() > 60).collect(Collectors.toList());

        for (TaskConcrete2 task : tasksWithHours) {

            double taskHoursTotal = ((double) task.getTotalDurationSeconds() / 60) / 60;
            String taskDescription = task.getDescription();
            XYChart.Series<String, Double> oneTaskBar = new XYChart.Series<>();
            XYChart.Data<String, Double> data = new XYChart.Data<>("Tasks", taskHoursTotal);
            oneTaskBar.setName(taskDescription);
            oneTaskBar.getData().add(data);
            taskBars.add(oneTaskBar);
        }
        barChartTasks.getData().clear();
        barChartTasks.getData().addAll(taskBars);

    }

}
