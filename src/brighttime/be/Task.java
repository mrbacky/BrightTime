package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rado
 */
public class Task {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty clientName = new SimpleStringProperty();
    private final StringProperty projectName = new SimpleStringProperty();
    private final IntegerProperty duration = new SimpleIntegerProperty();
    private final StringProperty stringDuration = new SimpleStringProperty();
    
    // add duration property
    public Task(int id, String name, String projectName, String clientName, int duration) {
        this.id.set(id);
        this.name.set(name);
        this.projectName.set(projectName);
        this.clientName.set(clientName);
        this.duration.set(duration);
        
    }

    @Override
    public String toString() {
        String output = id.get() + " " + name.get() + " " + projectName.get() + " " + clientName.get() + " " + duration.get();
        return output;
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

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getClientName() {
        return clientName.get();
    }

    public void setClientName(String value) {
        clientName.set(value);
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public String getProjectName() {
        return projectName.get();
    }

    public void setProjectName(String value) {
        projectName.set(value);
    }

    public StringProperty projectNameProperty() {
        return projectName;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int value) {
        duration.set(value);
    }

    public IntegerProperty durationProperty() {
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
