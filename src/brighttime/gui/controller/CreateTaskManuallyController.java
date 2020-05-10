package brighttime.gui.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;
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

    // TODO: Decide id 12HourView or 24HourView
    //StringConverter<LocalTime> converter =new LocalTimeStringConverter(FormatStyle.SHORT, Locale.getDefault());
    StringConverter<LocalTime> converter = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.FRANCE);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Javadoc
        display24HourView();
    }

    private void display24HourView() {
        timePickerStart.set24HourView(true);
        timePickerStart.converterProperty().setValue(converter);
    }

    LocalDateTime getStartTime() {
        return LocalDateTime.of(datePicker.getValue(), timePickerStart.getValue());
    }

    LocalDateTime getEndTime() {
        return LocalDateTime.of(datePicker.getValue(), timePickerEnd.getValue());
    }

}
