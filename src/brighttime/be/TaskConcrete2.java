package brighttime.be;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A concrete type of Task used in the Overview view.
 *
 * @author annem
 */
public class TaskConcrete2 extends TaskBase {

    private int rate;
    private final IntegerProperty totalDurationSeconds = new SimpleIntegerProperty();
    private final DoubleProperty totalCost = new SimpleDoubleProperty();
    private final StringProperty totalDurationString = new SimpleStringProperty();
    private final StringProperty totalCostString = new SimpleStringProperty();

    public TaskConcrete2(int id, String description, Project project, TaskBase.Billability billability, int totalDurationSeconds, int rate) {
        super(id, description, project, billability);
        this.rate = rate;
        this.totalDurationSeconds.set(totalDurationSeconds);
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTotalDurationSeconds() {
        return totalDurationSeconds.get();
    }

    public void setTotalDurationSeconds(int value) {
        totalDurationSeconds.set(value);
    }

    public IntegerProperty totalDurationSecondsProperty() {
        return totalDurationSeconds;
    }

    public double getTotalCost() {
        return totalCost.get();
    }

    public void setTotalCost(double value) {
        totalCost.set(value);
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    public String getTotalDurationString() {
        return totalDurationString.get();
    }

    public void setTotalDurationString(String value) {
        totalDurationString.set(value);
    }

    public StringProperty totalDurationStringProperty() {
        return totalDurationString;
    }

    public String getTotalCostString() {
        return totalCostString.get();
    }

    public void setTotalCostString(String value) {
        totalCostString.set(value);
    }

    public StringProperty totalCostStringProperty() {
        return totalCostString;
    }

}
