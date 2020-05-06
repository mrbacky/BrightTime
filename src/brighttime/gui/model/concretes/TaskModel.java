package brighttime.gui.model.concretes;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.bll.BllException;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado - inspiration from Greg
 *
 */
public class TaskModel implements ITaskModel {

    private final BllFacade bllManager;
    private Task task;
    private LocalDate date;

    private ObjectProperty<List<TaskEntry>> dayEntryList = new SimpleObjectProperty<>();
    private final StringProperty stringDuration = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();

    public TaskModel(BllFacade bllManager) throws IOException {
        this.bllManager = bllManager;

    }

    @Override
    public LocalDateTime getEndTime() {
        if (!getDayEntryList().isEmpty()) {

            return bllManager.getEndTime(getDayEntryList());
        }
        return task.getCreationTime();
    }

    @Override
    public void setEndTime(LocalDateTime value) {
        endTime.set(value);
    }

    @Override
    public ObjectProperty endTimeProperty() {
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!getDayEntryList().isEmpty()) {

            return bllManager.getStartTime(getDayEntryList());
        }
        return task.getCreationTime();
    }

    @Override
    public void setStartTime(LocalDateTime value) {
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
    public List getDayEntryList() {
        List<TaskEntry> dayEntries = task.getTaskEntryList().stream().filter(allEntries
                -> allEntries.getStartTime().toLocalDate().equals(date)).collect(Collectors.toList());
        

//        dayEntries.sort(Comparator.comparing(e -> e.getStartTime()));
        
        return dayEntries;
    }

    @Override
    public void setDayEntryList(List value) {
        dayEntryList.set(value);
    }

    @Override
    public ObjectProperty dayEntryListProperty() {
        return dayEntryList;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;

    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public Duration calculateTaskDuration(List<TaskEntry> entryList) {
        if (task.getTaskEntryList() != null) {
            return bllManager.calculateTaskDuration(entryList);
        }
        return Duration.ZERO;
    }

    @Override
    public String secToFormat(long sec) {
        return bllManager.secToFormat(sec);
    }

    @Override
    public long formatToSec(String formatString) {
        return bllManager.formatToSec(formatString);
    }

    @Override
    public void createTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) {
        try {
            TaskEntry newTaskEntry = new TaskEntry(task, tempStartTime, tempEndTime);
            task.getTaskEntryList().add(newTaskEntry);
            bllManager.createTaskEntry(newTaskEntry);

            /*    
  call createTaskEntry from DB here
  TODO: create entryList in task OBJ if the list does not exist
        if (task.getTaskEntryList().isEmpty()) {
            List<TaskEntry> entryList = new ArrayList<>();
            entryList.add(newTaskEntry);
            task.setTaskEntryList(entryList);
        } else {
            task.getTaskEntryList().add(newTaskEntry);
        }
             */
        } catch (BllException ex) {
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

}
