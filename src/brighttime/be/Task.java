package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rado
 */
public abstract class Task {
//TODO: Make one abstract class and two contrete classes.

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private Billability billability;

    public Task(int id, String description, Billability billability) {
        this.id.set(id);
        this.description.set(description);
        this.billability = billability;
    }

    public Task(String description, Billability billability) {
        this.description.set(description);
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
