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
    // add duration property

    public Task(int id, String name, String clientName, String projectName) {
        this.id.set(id);
        this.name.set(name);
        this.clientName.set(clientName);
        this.projectName.set(projectName);
    }

    private int getId() {
        return id.get();
    }

    private void setId(int value) {
        id.set(value);
    }

    private IntegerProperty idProperty() {
        return id;
    }

    private String getName() {
        return name.get();
    }

    private void setName(String value) {
        name.set(value);
    }

    private StringProperty nameProperty() {
        return name;
    }

    private String getClientName() {
        return clientName.get();
    }

    private void setClientName(String value) {
        clientName.set(value);
    }

    private StringProperty clientNameProperty() {
        return clientName;
    }

    private String getProjectName() {
        return projectName.get();
    }

    private void setProjectName(String value) {
        projectName.set(value);
    }

    private StringProperty projectNameProperty() {
        return projectName;
    }

}
