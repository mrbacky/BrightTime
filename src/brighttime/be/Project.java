package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rado
 */
public class Project {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<Client> client = new SimpleObjectProperty<>();

    public Project(int id, String name, Client client) {
        this.id.set(id);
        this.name.set(name);
        this.client.set(client); //maybe change this
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

    public Client getClient() {
        return client.get();
    }

    public void setClient(Client value) {
        client.set(value);
    }

    public ObjectProperty clientProperty() {
        return client;
    }

}
