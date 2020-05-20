package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The base class for any type of Task.
 *
 * @author rado
 */
public abstract class TaskBase {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Project> project = new SimpleObjectProperty<>();
    private Billability billability;

    TaskBase(int id, String description, Project project, Billability billability) {
        this.id.set(id);
        this.description.set(description);
        this.project.set(project);
        this.billability = billability;
    }

    TaskBase(String description, Project project, Billability billability) {
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

}
