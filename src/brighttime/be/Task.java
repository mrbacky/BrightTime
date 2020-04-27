package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Time;

/**
 *
 * @author rado
 */
public class Task {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty clientName = new SimpleStringProperty();
    private final StringProperty projectName = new SimpleStringProperty();
    private final ObjectProperty<Time> duration = new SimpleObjectProperty<>();

    public Task(int id, String name, String clientName, String projectName, Time duration) {
        this.id.set(id);
        this.name.set(name);
        this.clientName.set(clientName);
        this.projectName.set(projectName);
        this.duration.set(duration);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    private IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    private StringProperty nameProperty() {
        return name;
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String value) {
        clientName.set(value);
    }

    private StringProperty clientNameProperty() {
        return clientName;
    }

    public String getProjectName() {
        return projectName.get();
    }

    public void setProjectName(String value) {
        projectName.set(value);
    }

    private StringProperty projectNameProperty() {
        return projectName;
    }

    public Time getDuration() {
        return duration.get();
    }

    public void setDuration(Time value) {
        duration.set(value);
    }

    private ObjectProperty durationProperty() {
        return duration;
    }

}
