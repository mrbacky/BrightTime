package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author rado
 */
public class Task {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Project> project = new SimpleObjectProperty<>();
    private final ObjectProperty<List<TaskEntry>> taskEntryList = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> creationTime = new SimpleObjectProperty<>();
    private Billability billability;
    private final IntegerProperty totalDuration = new SimpleIntegerProperty();
    private final IntegerProperty totalCost = new SimpleIntegerProperty();
    private int projectId;
    private int rate;

    public int getTotalDuration() {
        return totalDuration.get();
    }

    public void setTotalDuration(int value) {
        totalDuration.set(value);
    }

    public IntegerProperty totalDurationProperty() {
        return totalDuration;
    }

    public int getTotalCost() {
        return totalCost.get();
    }

    public void setTotalCost(int value) {
        totalCost.set(value);
    }

    public IntegerProperty totalCostProperty() {
        return totalCost;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Task(int id, String description, int totalHours, Billability billability, int projectId) {
        this.id.set(id);
        this.description.set(description);
        this.billability = billability;
        this.totalDuration.set(totalHours);
        this.projectId = projectId;
    }

    public Task(int id, String description, int totalHours, int rate, Billability billability) {
        this.id.set(id);
        this.description.set(description);
        this.billability = billability;
        this.rate = rate;
        this.totalDuration.set(totalHours);
    }

    public Task(int id, String description, Project project, Billability billability, List<TaskEntry> taskEntryList, LocalDateTime creationTime) {
        this.id.set(id);
        this.description.set(description);
        this.project.set(project);
        this.billability = billability;
        this.taskEntryList.set(taskEntryList);
        this.creationTime.set(creationTime);
    }

    public Task(String description, Project project, Billability billability) {
        this.description.set(description);
        this.project.set(project);
        this.billability = billability;
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

    public enum Billability {
        BILLABLE, NON_BILLABLE
    }

    public Billability getBillability() {
        return billability;
    }

    public void setBillability(Billability billability) {
        this.billability = billability;
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

}
