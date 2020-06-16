package brighttime.gui.model.concretes;

import brighttime.be.TaskEntry;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author rado
 */
public class TaskEntryModel implements ITaskEntryModel {

    private BllFacade bllManager;
    private TaskEntry taskEntry;

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();
    private final StringProperty stringDuration = new SimpleStringProperty();

    public TaskEntryModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    @Override
    public void initializeTaskEntryModel() {
        setTaskEntryModelDetails();
        setupStartTimeListener();
        setupEndTimeListener();
    }

    @Override
    public void setTaskEntryModelDetails() {
        setStartTime(taskEntry.getStartTime().toLocalTime());
        setEndTime(taskEntry.getEndTime().toLocalTime());
        setStringDuration(secToFormat((int) calculateDuration(taskEntry).toSeconds()));
    }

    @Override
    public void setupStartTimeListener() {
        startTime.addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            if (newValue.isBefore(taskEntry.getEndTime().toLocalTime())) {
                LocalDateTime startTimeFull = LocalDateTime.of(getDate(), newValue);
                taskEntry.setStartTime(startTimeFull);
                stringDuration.set(secToFormat((int) calculateDuration(taskEntry).toSeconds()));
            } else {
                stringDuration.set("00:00:00");
            }
        });
    }

    @Override
    public void setupEndTimeListener() {
        endTime.addListener((observable, oldValue, newValue) -> {
            if (newValue.isAfter(taskEntry.getStartTime().toLocalTime())) {
                LocalDateTime endTimeFull = LocalDateTime.of(getDate(), newValue);
                taskEntry.setEndTime(endTimeFull);
                stringDuration.set(secToFormat((int) calculateDuration(taskEntry).toSeconds()));
            } else {
                stringDuration.set("00:00:00");
            }
        });
    }

    @Override
    public void updateTaskEntryStartTime() throws ModelException {
        try {
            //  TODO: We don't need to return TASK ENTRY, change to void ------------------------------------------------------------
            bllManager.updateTaskEntryStartTime(taskEntry);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void updateTaskEntryEndTime() throws ModelException {
        try {
            //  TODO: We don't need to return TASK ENTRY, change to void ------------------------------------------------------------
            bllManager.updateTaskEntryEndTime(taskEntry);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void setTaskEntry(TaskEntry taskEntry) {
        this.taskEntry = taskEntry;
    }

    @Override
    public TaskEntry getTaskEntry() {
        return taskEntry;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime.get();
    }

    @Override
    public void setEndTime(LocalTime value) {
        endTime.set(value);
    }

    @Override
    public ObjectProperty endTimeProperty() {
        return endTime;
    }

    @Override
    public LocalDate getDate() {
        return taskEntry.getStartTime().toLocalDate();
    }

    @Override
    public void setDate(LocalDate value) {
        date.set(value);
    }

    @Override
    public ObjectProperty dateProperty() {
        return date;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime.get();
    }

    @Override
    public void setStartTime(LocalTime value) {
        startTime.set(value);
    }

    @Override
    public ObjectProperty startTimeProperty() {
        return startTime;
    }

    @Override
    public String getStringDuration() {
        return stringDuration.get();
    }

    @Override
    public void setStringDuration(String value) {
        stringDuration.set(value);
    }

    @Override
    public StringProperty stringDurationProperty() {
        return stringDuration;
    }

    @Override
    public Duration calculateDuration(TaskEntry taskEntry) {
        return bllManager.calculateDuration(taskEntry);
    }

    @Override
    public String secToFormat(int sec) {
        return bllManager.secToFormat(sec);

    }

}
