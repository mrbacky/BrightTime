package brighttime.be;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author annem
 */
public class TaskType2 extends Task {

    private int rate;
    private final IntegerProperty totalDurationSeconds = new SimpleIntegerProperty();
    private final DoubleProperty totalCost = new SimpleDoubleProperty();
    private final StringProperty totalDurationString = new SimpleStringProperty();
    private final StringProperty totalCostString = new SimpleStringProperty();

    public TaskType2(int id, String description, Task.Billability billability, int totalDurationSeconds, int rate) {
        super(id, description, billability);
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
