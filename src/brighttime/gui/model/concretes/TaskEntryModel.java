package brighttime.gui.model.concretes;

import brighttime.be.TaskEntry;
import brighttime.bll.BllFacade;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author rado
 */
public class TaskEntryModel implements ITaskEntryModel {

    private BllFacade bllManager;

    private final StringProperty stringDuration = new SimpleStringProperty();
    private final StringProperty entryDescription = new SimpleStringProperty();
    private TaskEntry taskEntry;
    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();

    public TaskEntryModel(BllFacade bllManager) {
        this.bllManager = bllManager;

        updateStartTimeListener();
        updateEndTimeListener();
        durationListener();
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
        return secToFormat(calculateDuration(taskEntry).toSeconds());
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
    public String secToFormat(long sec) {
        return bllManager.secToFormat(sec);

    }

    @Override
    public String getEntryDescription() {
        return entryDescription.get();
    }

    @Override
    public void setEntryDescription(String value) {
        entryDescription.set(value);
    }

    @Override
    public StringProperty entryDescriptionProperty() {
        return entryDescription;
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
    public LocalDateTime convertLTandLDtoLDT(LocalTime time, LocalDate date) {
        return null;
    }

    @Override
    public void updateStartTimeListener() {
        startTime.addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {

            if (newValue.isBefore(taskEntry.getEndTime().toLocalTime())) {
                LocalDateTime startTimeLDT = LocalDateTime.of(getDate(), newValue);
                taskEntry.setStartTime(startTimeLDT);
                bllManager.updateTaskEntryStartTime(taskEntry);
                stringDuration.set(secToFormat(calculateDuration(taskEntry).toSeconds()));
            }

        });

    }

    @Override
    public void updateEndTimeListener() {
        endTime.addListener((observable, oldValue, newValue) -> {
            if (newValue.isAfter(taskEntry.getStartTime().toLocalTime())) {
                LocalDateTime endTimeLDT = LocalDateTime.of(getDate(), newValue);
                taskEntry.setEndTime(endTimeLDT);
                bllManager.updateTaskEntryEndTime(taskEntry);
                stringDuration.set(secToFormat(calculateDuration(taskEntry).toSeconds()));
            }

        });
    }

    @Override
    public void durationListener() {
        stringDuration.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
        });
        
    }
    
    

}
