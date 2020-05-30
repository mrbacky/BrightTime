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

    private final StringConverter<LocalTime> timeConverter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE);

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

    private final AlertManager alertManager;

    private LocalTime initialStartTime;
    private LocalTime initialEndTime;
    private ITaskEntryModel taskEntryModel;

    public TaskEntryItemController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void injectTaskEntryModel(ITaskEntryModel taskEntryModel) {
        this.taskEntryModel = taskEntryModel;

    }

    void initializeView() {
        setTaskEntryDetails(taskEntryModel.getTaskEntry());
        setInitialStartTime();
        setInitialEndTime();
        display24HourView();
    }

    private void setInitialStartTime() {
        initialStartTime = taskEntryModel.getStartTime();
    }

    private void setInitialEndTime() {
        initialEndTime = taskEntryModel.getEndTime();

    }

    private void display24HourView() {
        timePickerStartTime.set24HourView(true);
        timePickerStartTime.converterProperty().setValue(timeConverter);
        timePickerEndTime.set24HourView(true);
        timePickerEndTime.converterProperty().setValue(timeConverter);
    }

    public void setTaskEntryDetails(TaskEntry taskEntry) {
        textFieldTaskEntryDesc.setText(taskEntry.getTask().getDescription());
        timePickerStartTime.valueProperty().bindBidirectional(taskEntryModel.startTimeProperty());
        timePickerEndTime.valueProperty().bindBidirectional(taskEntryModel.endTimeProperty());
        lblDuration.textProperty().bind(taskEntryModel.stringDurationProperty());
    }

    @FXML
    private void handleEditStartTime(Event event) throws ModelException {
        LocalTime updatedStartTime = timePickerStartTime.getValue();
        if (updatedStartTime.isBefore(taskEntryModel.getEndTime()) && !updatedStartTime.equals(initialStartTime)) {
            try {
                taskEntryModel.updateTaskEntryStartTime();
                initialStartTime = taskEntryModel.getStartTime();
            } catch (ModelException ex) {
                taskEntryModel.setStartTime(initialStartTime);
                alertManager.showAlert("Unable to edit start time of task entry", "Check your internet connection. " + ex.getMessage());
            }
        } else if (updatedStartTime.isAfter(taskEntryModel.getEndTime())) {
            Platform.runLater(() -> {
                alertManager.showAlert("Invalid input", "Start time has to be before end time");
                timePickerStartTime.show();
            });
        }
    }

    @FXML
    private void handleEditEndTime(Event event) throws ModelException {
        LocalTime updatedEndTime = timePickerEndTime.getValue();
        if (updatedEndTime.isAfter(taskEntryModel.getStartTime()) && !updatedEndTime.equals(initialEndTime)) {
            try {
                taskEntryModel.updateTaskEntryEndTime();
                initialEndTime = taskEntryModel.getEndTime();
            } catch (ModelException ex) {
                taskEntryModel.setEndTime(initialEndTime);
                alertManager.showAlert("Unable to edit end time of task entry", "Check your internet connection." + ex.getMessage());
            }
        } else if (updatedEndTime.isBefore(taskEntryModel.getStartTime())) {
            Platform.runLater(() -> {
                alertManager.showAlert("Invalid input", "End time has to be after start time");
                timePickerEndTime.show();
            });
        }
    }

}
