package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskConcrete1;
import brighttime.be.User;
import brighttime.be.TaskEntry;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.DatePickerCustomizer;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateTaskController implements Initializable {

    @FXML
    private GridPane grid;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXComboBox<Project> cboProject;
    @FXML
    private JFXToggleNode tglBillability;
    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePickerStart;
    @FXML
    private JFXTimePicker timePickerEnd;

    private IMainModel mainModel;
    private TimeTrackerController timeTrackerContr;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;
    private final DatePickerCustomizer datePickerCustomizer;
    private User user;

    private Boolean manualMode;
    private Boolean date = false;
    private Boolean start = false;
    private Boolean end = false;
    private Boolean timeInterval = false;

    private final StringConverter<LocalDate> dateConverter = new LocalDateStringConverter(FormatStyle.FULL, Locale.ENGLISH, Chronology.ofLocale(Locale.ENGLISH));
    //TODO: Change all to 24HourView!
    private final StringConverter<LocalTime> timeConverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE); //Locale determines the format in the text field.

    public CreateTaskController() {
        this.alertManager = new AlertManager();
        this.validationManager = new ValidationManager();
        this.datePickerCustomizer = new DatePickerCustomizer();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        normalMode();
        setDateRestrictions();
        setTimeRestriction();
        display24HourView();
        displayFormattedDate();
        listenDatePicker();
        listenTimePickerStart();
        listenTimePickerEnd();
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void initializeView() throws IOException {
        setUser();
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

    void manualMode() {
        manualMode = true;
        grid.add(datePicker, 0, 1, 1, 1);
        grid.add(timePickerStart, 1, 1, 1, 1);
        grid.add(timePickerEnd, 2, 1, 1, 1);
    }

    void normalMode() {
        manualMode = false;
        grid.getChildren().remove(datePicker);
        grid.getChildren().remove(timePickerStart);
        grid.getChildren().remove(timePickerEnd);
    }

    private void setUser() {
        user = mainModel.getUser();
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
        validationManager.comboBoxValidation(cboClient, "No client selected.");
        validationManager.comboBoxValidation(cboProject, "No project selected.");
    }

    /**
     * Adds a new task.
     */
    private void addTask() {
        //TODO: Should you be allowed to write wrong inputs and be stopped at creating the task. Or be stopped already at the wrong input?
        btnAdd.setOnAction((event) -> {//   change name method.
            if (validateInput()) {
                if (!manualMode) {
                    try {
                        TaskConcrete1 newTask = makeTask();
                        mainModel.addTask(newTask);
                        clearInputs();
//                        timeTrackerContr.initTasks();
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not create the task.", "An error occured: " + ex.getMessage());
                    }
                } else {
                    try {
                        TaskConcrete1 newTask = makeTask();
                        TaskConcrete1 newTaskWithEntry = makeTaskEntry(newTask);
                        mainModel.addTask(newTaskWithEntry);
                        clearInputs();
//                        timeTrackerContr.initTasks();
                    } catch (ModelException ex) {
                        alertManager.showAlert("Could not create the task.", "An error occured: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void clearInputs() {
        txtDescription.clear();
        cboClient.getSelectionModel().clearSelection();
        cboProject.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        timePickerStart.setValue(null);
        timePickerEnd.setValue(null);
    }

    private boolean validateInput() {
        if (txtDescription.getText().trim().isEmpty()) {
            alertManager.showAlert("No task description was entered.", "Please enter a description of the new task.");
            return false;
        }
        if (cboClient.getSelectionModel().isEmpty()) {
            alertManager.showAlert("No client is selected.", "Please select a client.");
            return false;
        }
        if (cboProject.getSelectionModel().isEmpty()) {
            alertManager.showAlert("No project is selected.", "Please select a project.");
            return false;
        }
        if (manualMode) {
            if (!date) {
                alertManager.showAlert("No date was selected.", "Please select a date.");
                return false;
            }
            if (!start) {
                alertManager.showAlert("No start time was selected.", "Please select a time.");
                return false;
            }
            if (!end) {
                alertManager.showAlert("No end time was selected.", "Please select a time.");
                return false;
            }
            if (!timeInterval) {
                alertManager.showAlert("The time interval is invalid.", "The end time is before the start time. Please check the selection.");
                return false;
            }
        }
        return true;
    }

    private TaskConcrete1 makeTask() {
        TaskBase.Billability billability;
        if (tglBillability.isSelected()) {
            billability = TaskBase.Billability.BILLABLE;
        } else {
            billability = TaskBase.Billability.NON_BILLABLE;
        }
        TaskConcrete1 newTask = new TaskConcrete1(txtDescription.getText().trim(), cboProject.getSelectionModel().getSelectedItem(), billability, user);
        newTask.setCreationTime(LocalDateTime.of(datePicker.getValue(), timePickerStart.getValue()));
        return newTask;
    }

    private TaskConcrete1 makeTaskEntry(TaskConcrete1 newTask) {
        LocalDateTime startDateTime = LocalDateTime.of(datePicker.getValue(), timePickerStart.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(datePicker.getValue(), timePickerEnd.getValue());
        TaskEntry entry = new TaskEntry(newTask, startDateTime, endDateTime);
        List<TaskEntry> list = new ArrayList();
        list.add(entry);
        newTask.setTaskEntryList(list);
        return newTask;
    }

    private void setDateRestrictions() {
        datePickerCustomizer.disableFutureDates(datePicker);
        datePickerCustomizer.changeWrittenFutureDateToCurrentDate(datePicker);
    }

    private void setTimeRestriction() {
        //TODO: If a future time is written or chosen, show alert and write the current time.
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isEqual(LocalDate.now())) {
                StringConverter<LocalTime> timeConverter = new LocalTimeStringConverter() {
                    @Override
                    public LocalTime fromString(String string) {
                        LocalTime time = super.fromString(string);
                        if (time.isAfter(LocalTime.now())) {
                            alertManager.showAlert("The time is in the future.", "Please write or select a valid time.");
                            return LocalTime.now();
                        } else {
                            return time;
                        }
                    }
                };
                timePickerStart.setConverter(timeConverter);
                timePickerEnd.setConverter(timeConverter);
            }
        });
    }

    private void displayFormattedDate() {
        datePicker.converterProperty().setValue(dateConverter);
    }

    private void display24HourView() {
        timePickerStart.set24HourView(true);
        timePickerStart.converterProperty().setValue(timeConverter);
        timePickerEnd.set24HourView(true);
        timePickerEnd.converterProperty().setValue(timeConverter);
    }

    private void listenDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                date = true;
            } else {
                date = false;
            }
        });
    }

    private void listenTimePickerStart() {
        timePickerStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                start = true;
                if (timePickerEnd.getValue() != null && newValue.isBefore(timePickerEnd.getValue())) {
                    timeInterval = true;
                }
                if (timePickerEnd.getValue() != null && newValue.isAfter(timePickerEnd.getValue())) {
                    timeInterval = false;
                }
            }
        });
    }

    private void listenTimePickerEnd() {
        timePickerEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                end = true;
                if (timePickerStart.getValue() != null && newValue.isAfter(timePickerStart.getValue())) {
                    timeInterval = true;
                }
                if (timePickerStart.getValue() != null && newValue.isBefore(timePickerStart.getValue())) {
                    timeInterval = false;
                }
            }
        });
    }

}
