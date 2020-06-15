package brighttime.gui.util;

import com.jfoenix.controls.JFXDatePicker;
import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 *
 * @author annem
 */
public class DatePickerCustomizer {

    public void disableFutureDates(JFXDatePicker datePicker) {
        datePicker.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(LocalDate.now()));
                }
            };
        });
    }

    public void changeWrittenFutureDateToCurrentDate(JFXDatePicker datePicker) {
        StringConverter<LocalDate> dateConverter = new LocalDateStringConverter() {
            @Override
            public LocalDate fromString(String string) {
                LocalDate date = super.fromString(string);
                if (date.isAfter(LocalDate.now())) {
                    return LocalDate.now();
                } else {
                    return date;
                }
            }
        };
        datePicker.setConverter(dateConverter);
    }

}
