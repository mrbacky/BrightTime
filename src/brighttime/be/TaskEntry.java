package brighttime.be;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author annem
 */
public class TaskEntry {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<TaskConcrete1> task = new SimpleObjectProperty<>();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();

    // put calculated properties inside the TaskModel
    public TaskEntry(int id, TaskConcrete1 task, LocalDateTime startTime, LocalDateTime endTime) {
        this.id.set(id);
        this.task.set(task);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
    }

    public TaskEntry(TaskConcrete1 task, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.task.set(task);
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

    public TaskConcrete1 getTask() {
        return task.get();
    }

    public void setTask(TaskConcrete1 value) {
        task.set(value);
    }

    public ObjectProperty taskProperty() {
        return task;
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

}
