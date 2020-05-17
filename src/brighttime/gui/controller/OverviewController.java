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
import com.jfoenix.controls.JFXNodesList;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
    @FXML
    private JFXNodesList nodesListUser;
    @FXML
    private JFXNodesList nodesListProject;
    @FXML
    private JFXNodesList nodesListTimeFrame;
    @FXML
    private HBox hBoxFilter;
    @FXML
    private Label lblProject;
    @FXML
    private Label lblTimeFrame;

    private final JFXButton user = new JFXButton();
    private final JFXButton project = new JFXButton();
    private final JFXButton timeFrame = new JFXButton();

    //TODO: Maybe move to CSS
    String defaultColor = "-fx-text-fill: #435A9A";
    String highlightColor = "-fx-text-fill: red";

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
        displayUserFilter();
        displayProjectFilter();
        displayTimeFrameFilter();
        removeUserFilter();
        removeProjectFilter();
        removeTimeFrameFilter();
    }

    void initializeView() {
        System.out.println("in Overview page");
        setUsersIntoComboBox();
        setClientsIntoComboBox();
        setProjectsIntoComboBox();
        setValidators();
        setTable();
        selectUser();
        selectClient();
        selectProject();
        listenDatePickerStart();
        listenDatePickerEnd();
        clearAllFilters();
        //vBox.translateXProperty().bind((scrollPane.widthProperty().subtract(vBox.widthProperty())).divide(2));

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                grid.setPrefWidth(newValue.doubleValue() - (oldValue.doubleValue() - scrollPane.getViewportBounds().getWidth()));
            }
        });

        //TODO: Fix table column widths.
        ObservableValue<Number> w1 = tbvTasks.widthProperty().multiply(0.465);
        ObservableValue<Number> w2 = tbvTasks.widthProperty().multiply(0.25);
        colTaskDescription.prefWidthProperty().bind(w1);
        colHours.prefWidthProperty().bind(w2);
        colCost.prefWidthProperty().bind(w2);

        barChartTasks.setTitle("Amount of hours for each task");
        setUpBarChart();
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
                    makeActiveFilterButton(user, newVal.toString());
                    nodesListUser.animateList(false);
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by user.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    private void selectClient() {
        cboClients.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                changeLabel(lblProject, "Please select a project.", highlightColor);
            }
        });
    }

    private void selectProject() {
        cboProjects.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), newVal, dpStartDate.getValue(), dpEndDate.getValue()));
                    makeActiveFilterButton(project, newVal.toString() + " (" + cboClients.getValue().toString() + ")");
                    changeLabel(lblProject, "Project", defaultColor);
                    nodesListProject.animateList(false);
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by project.", "An error occured: " + ex.getMessage());
                }
            }
        });
    }

    private void listenDatePickerStart() {
        dpStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (dpEndDate.getValue() != null) {
                    try {
                        mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), newValue, dpEndDate.getValue()));
                        //TODO: Change date format.
                        makeActiveFilterButton(timeFrame, newValue.toString() + " - " + dpEndDate.getValue().toString());
                        changeLabel(lblTimeFrame, "Time frame", defaultColor);
                        nodesListTimeFrame.animateList(false);
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                    }
                } else {
                    changeLabel(lblTimeFrame, "Please select an end date.", highlightColor);
                }
            }
        });
    }

    private void listenDatePickerEnd() {
        dpEndDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (dpStartDate.getValue() != null) {

                    try {
                        mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), newValue));
                        //TODO: Change date format.
                        makeActiveFilterButton(timeFrame, dpStartDate.getValue().toString() + " - " + newValue.toString());
                        changeLabel(lblTimeFrame, "Time frame", defaultColor);
                        nodesListTimeFrame.animateList(false);
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not filter by date.", "An error occured: " + ex.getMessage());
                    }
                } else {
                    changeLabel(lblTimeFrame, "Please select a start date.", highlightColor);
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

    private void displayUserFilter() {
        nodesListUser.setOnMouseEntered((event) -> {
            nodesListUser.animateList();
        });
        nodesListUser.setOnMouseExited((event) -> {
            if (!cboUsers.isShowing()) {
                nodesListUser.animateList(false);
            }
        });
    }

    private void displayProjectFilter() {
        nodesListProject.setOnMouseEntered((event) -> {
            nodesListProject.animateList();
        });
        nodesListProject.setOnMouseExited((event) -> {
            if (!cboClients.isShowing() && !cboProjects.isShowing()) {
                nodesListProject.animateList(false);
            }
        });
    }

    private void displayTimeFrameFilter() {
        nodesListTimeFrame.setOnMouseEntered((event) -> {
            nodesListTimeFrame.animateList();
        });
        nodesListTimeFrame.setOnMouseExited((event) -> {
            if (!dpStartDate.isShowing() && !dpEndDate.isShowing()) {
                nodesListTimeFrame.animateList(false);
            }
        });
    }

    private void removeUserFilter() {
        user.setOnAction((event) -> {
            cboUsers.getSelectionModel().clearSelection();
            hBoxFilter.getChildren().remove(user);
            refreshAfterRemovingOneFilter();
        });
    }

    private void removeProjectFilter() {
        project.setOnAction((event) -> {
            cboProjects.getSelectionModel().clearSelection();
            cboProjects.getItems().clear();
            cboClients.getSelectionModel().clearSelection();
            hBoxFilter.getChildren().remove(project);
            refreshAfterRemovingOneFilter();
        });
    }

    private void removeTimeFrameFilter() {
        timeFrame.setOnAction((event) -> {
            hBoxFilter.getChildren().remove(timeFrame);
            dpStartDate.setValue(null);
            dpEndDate.setValue(null);
            refreshAfterRemovingOneFilter();
        });
    }

    private void refreshAfterRemovingOneFilter() {
        //TODO: Exceptions.
        try {
            if (checkAllFilterEmpty()) {
                mainModel.getAllTasks();
            } else {
                mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), cboProjects.getValue(), dpStartDate.getValue(), dpEndDate.getValue()));
            }
        } catch (ModelException ex) {
            Logger.getLogger(OverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearAllFilters() {
        btnClearFilters.setOnAction((event) -> {
            try {
                changeLabel(lblProject, "Project", defaultColor);
                changeLabel(lblTimeFrame, "Time frame", defaultColor);
                hBoxFilter.getChildren().remove(user);
                hBoxFilter.getChildren().remove(project);
                hBoxFilter.getChildren().remove(timeFrame);
                cboUsers.getSelectionModel().clearSelection();
                cboClients.getSelectionModel().clearSelection();
                cboProjects.getSelectionModel().clearSelection();
                cboProjects.getItems().clear();
                dpStartDate.setValue(null);
                dpEndDate.setValue(null);
                mainModel.getAllTasks();
            } catch (ModelException ex) {
                alertManager.showAlert("Could not clear the filters.", "An error occured: " + ex.getMessage());
            }
        });
    }

    private Boolean checkAllFilterEmpty() {
        return cboUsers.getValue() == null && cboProjects.getValue() == null
                && dpStartDate.getValue() == null && dpEndDate.getValue() == null;
    }

    private void changeLabel(Label label, String text, String style) {
        label.setText(text);
        label.setStyle(style);
    }

    private void makeActiveFilterButton(JFXButton button, String text) {
        button.setText(text);
        button.getStyleClass().add("buttonFilteredItem");
        hBoxFilter.getChildren().add(button);
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
