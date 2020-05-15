package brighttime.be;

import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * A concrete type of Task used in the TimeTracker view.
 *
 * @author annem
 */
public class TaskConcrete1 extends TaskBase {

    private final ObjectProperty<Project> project = new SimpleObjectProperty<>();
    private final ObjectProperty<List<TaskEntry>> taskEntryList = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> creationTime = new SimpleObjectProperty<>();
    
    public TaskConcrete1(String description, Billability billability, Project project, LocalDateTime creationTime) {
        super(description, billability);
        this.project.set(project);
        this.creationTime.set(creationTime);

    }

    public TaskConcrete1(int id, String description, Billability billability, Project project, List<TaskEntry> taskEntryList, LocalDateTime creationTime) {
        super(id, description, billability);
        this.project.set(project);
        this.taskEntryList.set(taskEntryList);
        this.creationTime.set(creationTime);
    }

    public String getDescription() {
        return super.getDescription();
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

    public List<TaskEntry> getTaskEntryList() {
        return taskEntryList.get();
    }

    public void setTaskEntryList(List value) {
        taskEntryList.set(value);
    }

    public ObjectProperty taskEntryListProperty() {
        return taskEntryList;
    }

    public LocalDateTime getCreationTime() {
        return creationTime.get();
    }

    public void setCreationTime(LocalDateTime value) {
        creationTime.set(value);
    }

    public ObjectProperty creationTimeProperty() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "TaskConcrete1" + " " + this.getDescription();
    }

}
