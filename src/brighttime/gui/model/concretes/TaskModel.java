package brighttime.gui.model.concretes;

import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.bll.BllFacade;
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
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author rado - inspiration from Greg
 *
 */
public class TaskModel implements ITaskModel {

    private final BllFacade bllManager;
    private TaskConcrete1 task;
    private LocalDate date;

    private ObjectProperty<List<TaskEntry>> dayEntryList = new SimpleObjectProperty<>();
    private final StringProperty stringDuration = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();
    private final ListProperty<TaskEntry> dayEntryListProp = new SimpleListProperty<>();
    private ObservableList<TaskEntry> obsEntries = FXCollections.observableArrayList();

    @Override
    public ObservableList getObsEntries() {
        return obsEntries;
    }

    @Override
    public ObservableList getDayEntryListProp() {
        return dayEntryListProp.get();
    }

    @Override
    public void setDayEntryListProp(ObservableList value) {
        dayEntryListProp.set(value);
    }

    @Override
    public ListProperty dayEntryListPropProperty() {
        return dayEntryListProp;
    }

    public TaskModel(BllFacade bllManager) throws IOException {
        this.bllManager = bllManager;

    }

    @Override
    public void initializeModel() {
        obsEntries.addAll(setUpDayEntryList());
        dayEntryListProp.set(obsEntries);
        startTime.set(updatedStartTime());
        endTime.set(updatedEndTime());
//
        setupDayEntryListListener();
//        setupEndTimeListener();

    }

    @Override
    public LocalDateTime getEndTime() {
//        if (!getDayEntryList().isEmpty()) {
//            return bllManager.getEndTime(getDayEntryList());
//        }
//        return task.getCreationTime();
        return endTime.get();
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

        return startTime.get();
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

        return dayEntryList.get();
//        List<TaskEntry> dayEntries = task.getTaskEntryList().stream().filter(allEntries
//                -> allEntries.getStartTime().toLocalDate().equals(date)).collect(Collectors.toList());
//        dayEntries.sort(Comparator.comparing(o -> o.getStartTime()));
//        return dayEntries;
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
    public void setTask(TaskConcrete1 task) {
        this.task = task;

    }

    @Override
    public TaskConcrete1 getTask() {
        return task;
    }

    @Override
    public Duration calculateDuration(TaskEntry taskEntry) {
        return bllManager.calculateDuration(taskEntry);
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

//    @Override
//    public LocalDateTime getEndTime() {
//        if (!getDayEntryList().isEmpty()) {
//            return bllManager.getEndTime(task);
//        }
//        return task.getCreationTime();
//    }
//
//    @Override
//    public LocalDateTime getStartTime() {
//        if (!getDayEntryList().isEmpty()) {
//            return bllManager.getStartTime(task);
//        }
//        return task.getCreationTime();
//    }
    @Override
    public void addTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) throws ModelException {
        try {
            TaskEntry newTaskEntry = new TaskEntry(task, task.getDescription(), tempStartTime, tempEndTime);
            TaskEntry taskEntry = bllManager.createTaskEntry(newTaskEntry);
            task.getTaskEntryList().add(taskEntry);
//  call createTaskEntry from DB here
//  TODO: create entryList in task OBJ if the list does not exist
//        if (task.getTaskEntryList().isEmpty()) {
//            List<TaskEntry> entryList = new ArrayList<>();
//            entryList.add(newTaskEntry);
//            task.setTaskEntryList(entryList);
//        } else {
//            task.getTaskEntryList().add(newTaskEntry);
//        }
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
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

    public void taskEntryListListener() {
        task.getTaskEntryList();
    }

    @Override
    public void setupDayEntryListListener() {

        obsEntries.addListener((ListChangeListener.Change<? extends TaskEntry> c) -> {
            System.out.println("change in setupDayEntryListListener");
            startTime.set(updatedStartTime());
            endTime.set(updatedEndTime());
            
            

        });

//        dayEntryList.addListener((ObservableValue<? extends List<TaskEntry>> observable, List<TaskEntry> oldValue, List<TaskEntry> newValue) -> {
//            System.out.println("change in setupDayEntryListListener");
//            
//            dayEntryList.setValue(newValue);
//            startTime.set(updatedStartTime());
//            endTime.set(updatedEndTime());
//
//        });
//        startTime.addListener((ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) -> {
//            
//        });
    }

    @Override
    public void setupEndTimeListener() {
//        endTime.addListener((ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) -> {
//            newValue = getEndTime();
//        });
    }

    @Override
    public LocalDateTime updatedStartTime() {
        // || getDayEntryList()!=null
        if (!obsEntries.isEmpty()) {
            return bllManager.getStartTime(obsEntries);
        }
        return task.getCreationTime();
    }

    @Override
    public LocalDateTime updatedEndTime() {
        if (!obsEntries.isEmpty()) {
            return bllManager.getEndTime(obsEntries);
        }
        return task.getCreationTime();
    }

    @Override
    public List<TaskEntry> setUpDayEntryList() {

        List<TaskEntry> dayEntries = task.getTaskEntryList().stream().filter(allEntries
                -> allEntries.getStartTime().toLocalDate().equals(date)).collect(Collectors.toList());
        dayEntries.sort(Comparator.comparing(o -> o.getStartTime()));
        return dayEntries;
    }

}
