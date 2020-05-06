package brighttime.be;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author annem
 */
public class TaskEntry {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Task> task = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();

    public TaskEntry(int id, Task task, LocalDateTime startTime, LocalDateTime endTime) {
        this.id.set(id);
        this.task.set(task);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
    }

    public TaskEntry(Task task, LocalDateTime startTime, LocalDateTime endTime) {
        this.task.set(task);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
    }

    @Override
    public String toString() {
        return "TaskEntry{" + "id=" + id + ", task=" + task + ", startTime=" + startTime + ", endTime=" + endTime + '}';
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

    public Task getTask() {
        return task.get();
    }

    public void setTask(Task value) {
        task.set(value);
    }

    public ObjectProperty taskProperty() {
        return task;
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
