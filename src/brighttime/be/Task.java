package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.util.List;
import javafx.util.Duration;

/**
 *
 * @author rado
 */
public class Task {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Project> project = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<>();
    private final ObjectProperty<List<TaskEntry>> taskEntryList = new SimpleObjectProperty<>();

    public Task(int id, String description, Project project) {
        this.id.set(id);
        this.description.set(description);
        this.project.set(project);
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

    public Project getProject() {
        return project.get();
    }

    public void setProject(Project value) {
        project.set(value);
    }

    public ObjectProperty projectProperty() {
        return project;
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

    public List<TaskEntry> getTaskEntryList() {
        return taskEntryList.get();
    }

    public void setTaskEntryList(List value) {
        taskEntryList.set(value);
    }

    public ObjectProperty taskEntryListProperty() {
        return taskEntryList;
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

}
