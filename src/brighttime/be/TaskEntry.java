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
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>();
    private final StringProperty stringDuration = new SimpleStringProperty();

    public TaskEntry(int id, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id.set(id);
        this.description.set(description);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
    }

    public TaskEntry(String description, LocalDateTime startTime, LocalDateTime endTime) {
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

    public Duration getDuration() {
        return duration.get();
    }

    public void setDuration(Duration value) {
        duration.set(value);
    }

    public ObjectProperty durationProperty() {
        return duration;
    }

    public String getStringDuration() {
        return stringDuration.get();
    }

    public void setStringDuration(String value) {
        stringDuration.set(value);
    }

    public StringProperty stringDurationProperty() {
        return stringDuration;
    }

}
