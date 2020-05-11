package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rado
 */
public class Client {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty hourlyRate = new SimpleIntegerProperty();

    public Client(int id, String name) {
        this.id.set(id);
        this.name.set(name);
    }

    public Client(String name, int hourlyRate) {
        this.name.set(name);
        this.hourlyRate.set(hourlyRate);
    }

    public Client(String name) {
        this.name.set(name);
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

    @Override
    public String toString() {
        return getName();
    }

    public int getHourlyRate() {
        return hourlyRate.get();
    }

    public void setHourlyRate(int value) {
        hourlyRate.set(value);
    }

    public IntegerProperty hourlyRateProperty() {
        return hourlyRate;
    }

}
