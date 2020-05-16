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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
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
    private BarChart<?, ?> barChartTasks;

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
    private JFXButton user = new JFXButton();

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
        colTaskDescription.setPrefWidth(335);
        colHours.setPrefWidth(134);
        colCost.setPrefWidth(134);
        // TODO
        displayUserFilter();
        //displayProjectFilter();
        //displayTimeFrameFilter();
        removeUserFilter();
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
        clearFilters();
        //vBox.translateXProperty().bind((scrollPane.widthProperty().subtract(vBox.widthProperty())).divide(2));

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                grid.setPrefWidth(newValue.doubleValue() - (oldValue.doubleValue() - scrollPane.getViewportBounds().getWidth()));
            }

        });
        ObservableValue<Number> w1 = tbvTasks.widthProperty().multiply(0.5);
        ObservableValue<Number> w2 = tbvTasks.widthProperty().multiply(0.25);
        colTaskDescription.prefWidthProperty().bind(w1);
        colHours.prefWidthProperty().bind(w2);
        colCost.prefWidthProperty().bind(w2);

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
                    user.setText(newVal.toString());
                    user.getStyleClass().add("buttonFilteredItem");
                    hBoxFilter.getChildren().add(user);
                    nodesListUser.animateList(false);
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not filter by user.", "An error occured: " + ex.getMessage());
                }
                System.out.println(scrollPane.getWidth());
            }
        });
    }

    private void selectClient() {
        cboClients.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                nodesListProject.animateList();
            }
        });
    }

    private void selectProject() {
        cboProjects.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    mainModel.getAllTasksFiltered(new Filter(cboUsers.getValue(), newVal, dpStartDate.getValue(), dpEndDate.getValue()));
                    nodesListProject.animateList(false);
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
                hBoxFilter.getChildren().remove(user);
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
            if (!cboClients.getSelectionModel().isEmpty()) {
                nodesListProject.animateList();
            } else {
                if (!cboClients.isShowing() && !cboProjects.isShowing()) {
                    nodesListProject.animateList(false);
                }
            }
        });
    }

    private void displayTimeFrameFilter() {
        nodesListTimeFrame.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                nodesListTimeFrame.animateList();
            } else {
                nodesListTimeFrame.animateList(false);
            }
        });
    }

    private void removeUserFilter() {
        user.setOnAction((event) -> {
            cboUsers.getSelectionModel().clearSelection();
            hBoxFilter.getChildren().remove(user);
            
            try {
                mainModel.getAllTasks();
            } catch (ModelException ex) {
                Logger.getLogger(OverviewController.class.getName()).log(Level.SEVERE, null, ex);
            }                    
        });
    }

}
