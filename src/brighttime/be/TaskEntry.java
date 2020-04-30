package brighttime.be;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.Duration;

/**
 *
 * @author annem
 */
public class TaskEntry {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();
    //private final StringProperty stringDuration = new SimpleStringProperty();
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>();

    public TaskEntry(int id, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id.set(id);
        this.description.set(description);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalDateTime value) {
        startTime.set(value);
    }

    public ObjectProperty startTimeProperty() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalDateTime value) {
        endTime.set(value);
    }

    public ObjectProperty endTimeProperty() {
        return endTime;
    }

//    public String getStringDuration(LocalDateTime startTime, LocalDateTime endTime) {
//        Duration duration = Duration.between(startTime, endTime);
//        this.duration.set(duration);
//        return sec_To_Format((int) duration.getSeconds());
//    }

//    public void setStringDuration(String value) {
//        stringDuration.set(value);
//    }
//
//    public StringProperty stringDurationProperty() {
//        return stringDuration;
//    }

//    public Duration getDuration() {
//        return duration.get();
//    }
//
//    public void setDuration(Duration value) {
//        duration.set(value);
//    }
//
//    public ObjectProperty durationProperty() {
//        return duration;
//    }

//    public String sec_To_Format(int sec) {
//        int hours, mins, secs;
//        mins = (int) (sec / 60);
//        while (mins >= 60) {
//            mins = mins % 60;
//        }
//        hours = (int) ((sec / 60) / 60);
//        secs = sec % 60;
//        String stringTime = String.format("%02d:%02d:%02d", hours, mins, secs);
//        return stringTime;
//    }
//
//    public int format_To_Sec(String formatString) {
//        String[] format = formatString.split(":");
//        int hh, mm, ss, hours_In_Sec, mins_In_Sec, totalSec;
//        hh = Integer.parseInt(format[0]);
//        mm = Integer.parseInt(format[1]);
//        ss = Integer.parseInt(format[2]);
//        hours_In_Sec = hh * 3600;
//        mins_In_Sec = mm * 60;
//        totalSec = hours_In_Sec + mins_In_Sec + ss;
//        return totalSec;
//    }

}
