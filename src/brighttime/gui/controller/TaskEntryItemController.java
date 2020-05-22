package brighttime.gui.controller;

import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TaskEntryItemController implements Initializable {

    private static final String DATE_TIME_FORMAT = "HH:mm";

    @FXML
    private JFXTextField textFieldTaskEntryDesc;
    @FXML
    private JFXTimePicker timePickerStartTime;
    @FXML
    private JFXTimePicker timePickerEndTime;
    @FXML
    private Label lblDuration;
    @FXML
    private JFXButton btnRemoveTask;

    private ITaskEntryModel taskEntryModel;
    private final AlertManager alertManager;
    private TimeTrackerController timeTrackerController;
    private LocalTime oldStartTime;
    private LocalTime oldEndTime;

    private final StringConverter<LocalTime> timeConverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE);

    public TaskEntryItemController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        display24HourView();
    }

    public void injectTaskEntryModel(ITaskEntryModel taskEntryModel) {
        this.taskEntryModel = taskEntryModel;
        setTaskEntryDetails(taskEntryModel.getTaskEntry());
        setOldStartTime();
        setOldEndTime();

    }

    public void setTaskEntryDetails(TaskEntry taskEntry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        textFieldTaskEntryDesc.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntry.getTask().getDescription(), taskEntryModel.entryDescriptionProperty()));

        timePickerStartTime.valueProperty().bindBidirectional(taskEntryModel.startTimeProperty());
        timePickerEndTime.valueProperty().bindBidirectional(taskEntryModel.endTimeProperty());

        lblDuration.textProperty().bind(taskEntryModel.stringDurationProperty());

//        lblDuration.textProperty().bind(Bindings.createStringBinding(()
//                -> taskEntryModel.secToFormat(taskEntryModel.calculateDuration(taskEntry).toSeconds()), taskEntryModel.stringDurationProperty()));
    }

    @FXML
    private void handleEditStartTime(Event event) throws ModelException {
        LocalTime updatedStartTime = timePickerStartTime.getValue();
        TaskEntry taskEntry = taskEntryModel.getTaskEntry();
        LocalDate entryDate = taskEntry.getStartTime().toLocalDate();
        if (updatedStartTime.isBefore(taskEntry.getEndTime().toLocalTime()) && !updatedStartTime.equals(oldStartTime)) {
            try {
                taskEntry.setStartTime(LocalDateTime.of(entryDate, updatedStartTime));
                taskEntryModel.updateTaskEntryStartTime(taskEntry);
                oldStartTime = taskEntry.getStartTime().toLocalTime();
                //  workaround
                timeTrackerController.initTasks();
                System.out.println("startTime: " + taskEntry.getStartTime());
            } catch (ModelException ex) {
                alertManager.showAlert("An error occured", "Check your internet connection." + ex.getMessage());
            }

        } else if (updatedStartTime.isAfter(taskEntry.getEndTime().toLocalTime())) {
            Platform.runLater(() -> {
                alertManager.showAlert("Invalid input", "Start time has to be before end time");
                timePickerStartTime.show();

            });
        }

    }

    @FXML
    private void handleEditEndTime(Event event) throws ModelException {

        TaskEntry taskEntry = taskEntryModel.getTaskEntry();
        LocalTime updatedEndTime = timePickerEndTime.getValue();
        LocalDate entryDate = taskEntry.getEndTime().toLocalDate();

        if (updatedEndTime.isAfter(taskEntry.getStartTime().toLocalTime()) && !updatedEndTime.equals(oldEndTime)) {
            try {
                taskEntry.setEndTime(LocalDateTime.of(entryDate, updatedEndTime));
                taskEntryModel.updateTaskEntryEndTime(taskEntry);
//                oldEndTime = taskEntry.getEndTime().toLocalTime();
                System.out.println("endTime: " + taskEntry.getEndTime());
                timeTrackerController.initTasks();
            } catch (ModelException ex) {
                alertManager.showAlert("An error occured", "Check your internet connection." + ex.getMessage());
            }
        } else if (updatedEndTime.isBefore(taskEntry.getStartTime().toLocalTime())) {
            Platform.runLater(() -> {
                alertManager.showAlert("Invalid input", "End time has to be after start time");
                timePickerEndTime.show();
            });
        }

    }

    void injectTimeTrackerController(TimeTrackerController timeTrackerController) {
        this.timeTrackerController = timeTrackerController;
    }

    private void setOldStartTime() {
        oldStartTime = taskEntryModel.getTaskEntry().getStartTime().toLocalTime();
    }

    private void setOldEndTime() {
        oldEndTime = taskEntryModel.getTaskEntry().getEndTime().toLocalTime();

    }


    private void display24HourView() {
        timePickerStartTime.set24HourView(true);
        timePickerStartTime.converterProperty().setValue(timeConverter);
        timePickerEndTime.set24HourView(true);
        timePickerEndTime.converterProperty().setValue(timeConverter);
    }

}
