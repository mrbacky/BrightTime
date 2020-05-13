package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {

    private final XYChart.Series<String, Integer> billableSeries = new XYChart.Series<>();
    private final XYChart.Series<String, Integer> nonBillableSeries = new XYChart.Series<>();

    private final ObservableList<XYChart.Series<String, Integer>> data = FXCollections.observableArrayList();

//    private final ObservableList<XYChart.Data<String, Number>> billableData = FXCollections.observableArrayList();
//    private final ObservableList<XYChart.Data<String, Number>> nonBillableData = FXCollections.observableArrayList();
    @FXML
    private TableView<Task> tbvTasks;

    @FXML
    private TableColumn<Task, String> colTaskDesc;
    @FXML
    private TableColumn<Task, Integer> colHours;
    @FXML
    private TableColumn<Task, Integer> colCost;

    private IMainModel mainModel;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;

    @FXML
    private AnchorPane apOverview;

    @FXML
    private JFXComboBox<?> cboUsers;
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
    private StackedBarChart<String, Integer> chartTasks;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;

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

//        setupBarCharSeries();
//        initHardcodedChart();
//        initChartData();
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
        colTaskDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("totalDuration"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        tbvTasks.setItems(mainModel.getTaskList());
        try {
            mainModel.getAllTasks();
        } catch (ModelException ex) {
            alertManager.showAlert("Could not get the tasks.", "An error occured: " + ex.getMessage());
        }
    }

    private void selectProject() {
        cboProjects.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(newVal, dpStartDate.getValue(), dpEndDate.getValue()));
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
                    mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), newValue, dpEndDate.getValue()));
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
                    mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), dpStartDate.getValue(), newValue));
                    initChartData();
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
            mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));
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
            mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));
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

            mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));
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

            mainModel.getAllTasksFiltered(new Filter(cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));

        } catch (ModelException ex) {
            alertManager.showAlert("Could not filter in this month", "An error occured: " + ex.getMessage());
        }
    }

    private ObservableList<Series<String, Integer>> initChartData() {

        ObservableList<Task> taskList = mainModel.getTaskList();

        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();
//        long daysForChart = Duration.between(startDate, endDate).toDaysPart();

        Map<LocalDate, List<Task>> taskMap = mainModel.getTasks();
        Map<LocalDate, List<Task>> filteredMap = new HashMap<>();
        for (Map.Entry<LocalDate, List<Task>> entry : taskMap.entrySet()) {
            LocalDate key = entry.getKey();
            List<Task> value = entry.getValue();
            filteredMap = (Map<LocalDate, List<Task>>) taskMap.entrySet().stream().filter(allTasks
                    -> allTasks.getKey().isAfter(startDate) || allTasks.getKey().isBefore(endDate))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        for (Map.Entry<LocalDate, List<Task>> entry : filteredMap.entrySet()) {
            List<Task> value = entry.getValue();
            System.out.println("task: " + value);

        }
//
//        List<Task> billableTasks = taskList.stream()
//                .filter(task -> task.getBillability().equals(Task.Billability.BILLABLE))
//                .collect(Collectors.toList());
////                -> allTasks.getBillability().equals(Task.Billability.BILLABLE)).collect(Collectors.toList());
//        ObservableList<Task> billableTasksObsList = FXCollections.observableArrayList(billableTasks);
//        List<Task> nonBillableTasks = taskList.stream()
//                .filter(task -> task.getBillability().equals(Task.Billability.NON_BILLABLE))
//                .collect(Collectors.toList());
//        ObservableList<Task> nonBillableTasksObsList = FXCollections.observableArrayList(nonBillableTasks);
//        int billableHoursSum = 0;
//        for (Task task : billableTasksObsList) {
//            billableHoursSum += task.getTotalDuration();
//        }
//        System.out.println("billableHoursSum: " + billableHoursSum);
//        int nonBillableHoursSum = 0;
//        for (Task task : nonBillableTasksObsList) {
//            nonBillableHoursSum += task.getTotalDuration();
//        }
//        System.out.println("nonBillableHoursSum: " + nonBillableHoursSum);
////  how many days, that many runs in the loop
////  in one day are tasks with billable and nonbillable status
//
//        data.addAll(billableSeries, nonBillableSeries);
//        chartTasks.getData().addAll(data);
        return null;
    }

    private void setupBarCharSeries() {
        billableSeries.setName("Billable");
        chartTasks.getData().add(billableSeries);

        nonBillableSeries.setName("Nonbillable");
        chartTasks.getData().add(nonBillableSeries);

    }

    private void initHardcodedChart() {

    }

    private List<TaskEntry> getEntriesForDay(Task task, LocalDate date) {
        List<TaskEntry> dayEntries = task.getTaskEntryList().stream().filter(allEntries
                -> allEntries.getStartTime().toLocalDate().equals(date)).collect(Collectors.toList());
        return dayEntries;
    }

}
