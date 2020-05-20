package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ActiveFilterButton;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.ToolTipManager;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXNodesList;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
    private final ToolTipManager toolTipManager;
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

    private final ActiveFilterButton btnFilterUser = new ActiveFilterButton();
    private final ActiveFilterButton btnFilterProject = new ActiveFilterButton();
    private final ActiveFilterButton btnFilterTimeFrame = new ActiveFilterButton();

    //TODO: Maybe move to CSS
    String defaultColor = "-fx-text-fill: #435A9A";
    String highlightColor = "-fx-text-fill: #F0C326";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.US);

    @FXML
    private Label lblUser;
    private User currentUser;

    public OverviewController() {
        this.alertManager = new AlertManager();
        this.toolTipManager = new ToolTipManager();
        this.validationManager = new ValidationManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void initializeView() {

//        displayUserFilter();      this one is called in setUpUserRules();
        displayProjectFilter();
        displayTimeFrameFilter();
        removeUserFilter();
        removeProjectFilter();
        removeTimeFrameFilter();
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
        setToolTipsForButtons();

        //  this is important for hiding user combo box and stuff
        setUpUserRules();

        //vBox.translateXProperty().bind((scrollPane.widthProperty().subtract(vBox.widthProperty())).divide(2));
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                grid.setPrefWidth(newValue.doubleValue() - (oldValue.doubleValue() - scrollPane.getViewportBounds().getWidth()));
            }
        });

        //TODO: Fix table column widths.
//        ObservableValue<Number> w1 = tbvTasks.widthProperty().multiply(0.465);
//        ObservableValue<Number> w2 = tbvTasks.widthProperty().multiply(0.25);
//        colTaskDescription.prefWidthProperty().bind(w1);
//        colHours.prefWidthProperty().bind(w2);
//        colCost.prefWidthProperty().bind(w2);
        //barChartTasks.setTitle("Amount of hours for each task");
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
        validationManager.comboBoxValidation(cboClients, "No client selected.");
        validationManager.comboBoxValidation(cboProjects, "No project selected.");
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

                    //  added rule ------------------------------------------------------------------------------------------------------------
                    if (currentUser.getType() == User.UserType.ADMIN) {
                        StringProperty selectedFilter = new SimpleStringProperty();
                        selectedFilter.bind(Bindings.concat(newVal.firstNameProperty(), " ", newVal.lastNameProperty()));
                        makeCustomActiveFilterButton(btnFilterUser, selectedFilter);
                        nodesListUser.animateList(false);
                    }
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
                    StringProperty selectedFilter = new SimpleStringProperty();
                    selectedFilter.bind(Bindings.concat(newVal.nameProperty(), " (", newVal.clientProperty(), ")"));
                    makeCustomActiveFilterButton(btnFilterProject, selectedFilter);
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
                        String startDate = newValue.format(dateFormatter);
                        String endDate = dpEndDate.getValue().format(dateFormatter);
                        StringProperty selectedFilter = new SimpleStringProperty();
                        selectedFilter.bind(Bindings.concat(startDate, " - ", endDate));
                        makeCustomActiveFilterButton(btnFilterTimeFrame, selectedFilter);
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
                        String start = dpStartDate.getValue().format(dateFormatter);
                        String end = newValue.format(dateFormatter);
                        StringProperty selectedFilter = new SimpleStringProperty();
                        selectedFilter.bind(Bindings.concat(start, " - ", end));
                        makeCustomActiveFilterButton(btnFilterTimeFrame, selectedFilter);
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
        btnFilterUser.getBtn().setOnAction((event) -> {
            cboUsers.getSelectionModel().clearSelection();
            hBoxFilter.getChildren().remove(btnFilterUser);
            refreshAfterRemovingOneFilter();
        });
    }

    private void removeProjectFilter() {
        btnFilterProject.getBtn().setOnAction((event) -> {
            cboProjects.getSelectionModel().clearSelection();
            cboProjects.getItems().clear();
            cboClients.getSelectionModel().clearSelection();
            hBoxFilter.getChildren().remove(btnFilterProject);
            refreshAfterRemovingOneFilter();
        });
    }

    private void removeTimeFrameFilter() {
        btnFilterTimeFrame.getBtn().setOnAction((event) -> {
            hBoxFilter.getChildren().remove(btnFilterTimeFrame);
            dpStartDate.setValue(null);
            dpEndDate.setValue(null);
            refreshAfterRemovingOneFilter();
        });
    }

    private void refreshAfterRemovingOneFilter() {
        //TODO: Exceptions.
        try {
            if (checkAllFilterEmpty()) {
                if (currentUser.getType() == User.UserType.ADMIN) {
                    mainModel.getAllTasks();
                }
                mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), null, null, null));
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
                hBoxFilter.getChildren().remove(btnFilterProject);
                hBoxFilter.getChildren().remove(btnFilterTimeFrame);
                //  added rules --------------------------------------------------------------------------------------------------------------------
                if (currentUser.getType() == User.UserType.ADMIN) {
                    hBoxFilter.getChildren().remove(btnFilterUser);
                    cboUsers.getSelectionModel().clearSelection();
                }
                cboClients.getSelectionModel().clearSelection();
                cboProjects.getSelectionModel().clearSelection();
                cboProjects.getItems().clear();
                dpStartDate.setValue(null);
                dpEndDate.setValue(null);
                //  added rule --------------------------------------------------------------------------------------------------------------------
                if (currentUser.getType() == User.UserType.ADMIN) {
                    mainModel.getAllTasks();
                } else if (currentUser.getType() == User.UserType.USER) {
                    mainModel.getAllTasksFiltered(new Filter(currentUser, null, null, null));
                }
            } catch (ModelException ex) {
                alertManager.showAlert("Could not clear the filters.", "An error occured: " + ex.getMessage());
            }
        });
    }

    private Boolean checkAllFilterEmpty() {

        if (currentUser.getType() == User.UserType.ADMIN) {
            System.out.println("current user: " + currentUser.getFirstName());
            return cboUsers.getValue() == null && cboProjects.getValue() == null && dpStartDate.getValue() == null && dpEndDate.getValue() == null;
        } else {
            //  added rule --------------------------------------------------------------------------------------------------------------------
            System.out.println("current user: " + currentUser.getFirstName());
            return cboUsers.getValue() == currentUser && cboProjects.getValue() == null && dpStartDate.getValue() == null && dpEndDate.getValue() == null;
        }

    }

    private void changeLabel(Label label, String text, String style) {
        label.setText(text);
        label.setStyle(style);
    }

    private void makeCustomActiveFilterButton(ActiveFilterButton button, ObservableValue text) {
        button.getSelectedFilter().bind(text);

//        if (hBoxFilter.getChildren().contains(button)) {
//            hBoxFilter.getChildren().remove(1);
//        }
//        hBoxFilter.getChildren().add(1, button);
        //  Exception after selecting different project. Adding duplicate node. -----------------------------------------------------------------
        if (!hBoxFilter.getChildren().contains(button)) {
            hBoxFilter.getChildren().add(button);
        }
    }

    private void setUpBarChart() {
        ObservableList<XYChart.Series<String, Double>> taskBars = FXCollections.observableArrayList();

        List<TaskConcrete2> taskList = mainModel.getTaskList();
        List<TaskConcrete2> tasksWithHours = taskList.stream().filter(task -> task.getTotalDurationSeconds() > 60).collect(Collectors.toList());

        for (TaskConcrete2 task : tasksWithHours) {

            double taskHoursTotal = ((double) task.getTotalDurationSeconds() / 60) / 60;
            String taskDescription = task.getDescription();
            XYChart.Series<String, Double> oneTaskBar = new XYChart.Series<>();
            XYChart.Data<String, Double> data = new XYChart.Data<>("tasks", taskHoursTotal);
            data.setNode(new StackPane());
            createToolTip(data, task);
            oneTaskBar.setName(taskDescription);
            oneTaskBar.getData().add(data);
            taskBars.add(oneTaskBar);
        }
        barChartTasks.getData().clear();
        barChartTasks.getData().addAll(taskBars);
        barChartTasks.getYAxis().setLabel("hours");
        barChartTasks.setLegendVisible(false);
    }

    private void createToolTip(XYChart.Data<String, Double> xyPlot, TaskConcrete2 task) {
        String projectInfo = task.getProject().getName() + " (" + task.getProject().getClient().getName() + ")";
        String text
                = task.getDescription() + System.lineSeparator()
                + projectInfo + System.lineSeparator()
                + task.getTotalDurationString() + " hours";
        Tooltip toolTip = new Tooltip(text);
        toolTip.setShowDelay(Duration.seconds(0));
        Tooltip.install(xyPlot.getNode(), toolTip);

        xyPlot.getNode().setOnMouseEntered(mEvent -> xyPlot.getNode().getStyleClass().add("onBarchartColumnHover"));
        xyPlot.getNode().setOnMouseExited(mEvent -> xyPlot.getNode().getStyleClass().remove("onBarchartColumnHover"));
    }

    private void setUpUserRules() {
        currentUser = mainModel.getUser();
        if (currentUser.getType() == User.UserType.ADMIN) {
            displayUserFilter();

        } else {
            tbvTasks.getColumns().remove(2);
            cboUsers.getSelectionModel().select(currentUser);
            lblUser.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            nodesListUser.setDisable(true);
            lblUser.setStyle("-fx-opacity: 1");
        }
    }

    private void setToolTipsForButtons() {
        toolTipManager.setToolTipForOneButton(btnClearFilters, "Clear all active filters");
        toolTipManager.setToolTipForOneActiveFilterButton(btnFilterUser, "Clear user filter");
        toolTipManager.setToolTipForOneActiveFilterButton(btnFilterProject, "Clear project filter");
        toolTipManager.setToolTipForOneActiveFilterButton(btnFilterTimeFrame, "Clear time frame filter");
    }

}
