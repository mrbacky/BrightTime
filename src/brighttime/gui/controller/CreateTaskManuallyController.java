package brighttime.gui.controller;

import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateTaskManuallyController implements Initializable {

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePickerStart;
    @FXML
    private JFXTimePicker timePickerEnd;

    private final AlertManager alertManager;
    LocalDate lastVal;
    Boolean date = false;
    Boolean start = false;
    Boolean end = false;
    Boolean timeInterval = false;

    // TODO: Decide if 12HourView or 24HourView. Or the user's system.
    //StringConverter<LocalTime> converter =new LocalTimeStringConverter(FormatStyle.SHORT, Locale.getDefault());
    StringConverter<LocalTime> converter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE);

    public CreateTaskManuallyController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO: Maybe change the prompt text to clarify the format
        //TODO: Javadoc.
        //datePicker.setValue(LocalDate.now());
        setDateRestriction();
        setTimeRestriction();
        display24HourView();
        listenDatePicker();
        listenTimePickerStart();
        listenTimePickerEnd();
    }

    private void setDateRestriction() {
        //TODO: Disable future dates.
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        //If a futute date is written, show alert and change to current date.
        StringConverter<LocalDate> dateConverter = new LocalDateStringConverter() {
            @Override
            public LocalDate fromString(String string) {
                LocalDate date = super.fromString(string);
                if (date.isAfter(LocalDate.now())) {
                    //TODO: Why does the alert crash the program?
                    //alertManager.showAlert("The date is in the future.", "Please write or select a valid date.");
                    return LocalDate.now();
                    //return lastVal;
                } else {
                    return date;
                }
            }
        };
        datePicker.setConverter(dateConverter);
    }

    private void setTimeRestriction() {
        //TODO: If a future time is written or chosen, show alert and write the current time.
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEqual(LocalDate.now())) {
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

    private void display24HourView() {
        timePickerStart.set24HourView(true);
        timePickerStart.converterProperty().setValue(converter);
    }

    Boolean getDate() {
        return date;
    }

    private void setDate(Boolean date) {
        this.date = date;
    }

    private void listenDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //lastVal = newValue;
                System.out.println("okay");
                setDate(true);
            } else {
                setDate(false);
            }
        });
    }

    Boolean getStart() {
        return start;
    }

    private void setStart(Boolean start) {
        this.start = start;
    }

    private void listenTimePickerStart() {
        timePickerStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setStart(true);
                if (timePickerEnd.getValue() != null && newValue.isBefore(timePickerEnd.getValue())) {
                    setTimeInterval(true);
                }
                if (timePickerEnd.getValue() != null && newValue.isAfter(timePickerEnd.getValue())) {
                    setTimeInterval(false);
                }
            }
        });
    }

    Boolean getEnd() {
        return end;
    }

    private void setEnd(Boolean end) {
        this.end = end;
    }

    private void listenTimePickerEnd() {
        timePickerEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setEnd(true);//7:10-8:29
                if (timePickerStart.getValue() != null && newValue.isAfter(timePickerStart.getValue())) {
                    setTimeInterval(true);
                }
                if (timePickerStart.getValue() != null && newValue.isBefore(timePickerStart.getValue())) {
                    setTimeInterval(false);
                }
            }
        });
    }

    Boolean getTimeInterval() {
        return timeInterval;
    }

    private void setTimeInterval(Boolean timeInterval) {
        this.timeInterval = timeInterval;
    }

    LocalDateTime getStartTime() {
        return LocalDateTime.of(datePicker.getValue(), timePickerStart.getValue());
    }

    LocalDateTime getEndTime() {
        return LocalDateTime.of(datePicker.getValue(), timePickerEnd.getValue());
    }

}
