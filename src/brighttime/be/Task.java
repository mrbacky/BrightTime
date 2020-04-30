package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Duration;

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
    private final ObjectProperty<List<TaskEntry>> taskEntryList = new SimpleObjectProperty<>();
    private final StringProperty stringDuration = new SimpleStringProperty();
    
    //  delete startime and endtime from constructor
    public Task(int id, String description, Project project, LocalDateTime startTime, LocalDateTime endTime, List<TaskEntry> taskEntryList) {
        this.id.set(id);
        this.description.set(description);
        this.project.set(project);
        this.startTime.set(startTime);
        this.endTime.set(endTime);
        this.taskEntryList.set(taskEntryList);
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
    
    //everything down should be deleted from here
    public LocalDateTime getTaskStartTime() {
        LocalDateTime taskStartTime = getTaskEntryList().get(0).getStartTime();
        return taskStartTime;
    }
    
    public LocalDateTime getTaskEndTime() {
        TaskEntry latestTaskEntry = getTaskEntryList().get(getTaskEntryList().size() - 1);
        LocalDateTime taskEndTime = latestTaskEntry.getEndTime();
        return taskEndTime;
    }

    public String getStringDuration() {
        int totalDuration = 0;
        for (TaskEntry taskEntry : taskEntryList.get()) {
            
            System.out.println(taskEntry);
            System.out.println(taskEntry.getDuration());
            totalDuration = (int) (totalDuration + taskEntry.getDuration().toSeconds());
        }
        return sec_To_Format(totalDuration);
    }

    public void setStringDuration(String value) {
        stringDuration.set(value);
    }

    public StringProperty stringDurationProperty() {
        return stringDuration;
    }

    public String sec_To_Format(int sec) {
        int hours, mins, secs;
        mins = (int) (sec / 60);
        while (mins >= 60) {
            mins = mins % 60;
        }
        hours = (int) ((sec / 60) / 60);
        secs = sec % 60;
        String stringTime = String.format("%02d:%02d:%02d", hours, mins, secs);
        return stringTime;
    }

}
