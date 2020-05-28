package brighttime.gui.model.concretes;

import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.bll.BllFacade;
import brighttime.bll.BllException;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * @author rado - inspiration from BelTracker project (Greg, Nedas, David)
 * TaskModel is responsible for storing and manipulating with data of one task
 * displayed on UI
 */
public class TaskModel implements ITaskModel {

    private final BllFacade bllManager;
    private TaskConcrete1 task;
    private LocalDate date;

    private final StringProperty stringDuration = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();

    private ObservableList<TaskEntry> dayEntryList;

    public TaskModel(BllFacade bllManager) throws IOException {
        this.bllManager = bllManager;
        this.dayEntryList = FXCollections.observableArrayList((TaskEntry taskEntry) -> {
            return new Observable[]{
                taskEntry.startTimeProperty(),
                taskEntry.endTimeProperty()
            };
        });

    }

    @Override
    public void initializeTaskModel() {
        setupDayEntryList();
        setUpDayEntryListListener();
        setTaskModelDetails();
    }

    @Override
    public void setupDayEntryList() {
        List<TaskEntry> dayEntries = task.getTaskEntryList().stream().filter(allEntries
                -> allEntries.getStartTime().toLocalDate().equals(date)).collect(Collectors.toList());
        dayEntries.sort(Comparator.comparing(o -> o.getStartTime()));
        dayEntryList.clear();
        dayEntryList.addAll(dayEntries);
    }

    @Override
    public void setUpDayEntryListListener() {
        dayEntryList.addListener((ListChangeListener.Change<? extends TaskEntry> c) -> {
            setTaskModelDetails();
        });
    }

    @Override
    public void setTaskModelDetails() {
        if (!dayEntryList.isEmpty()) {
            startTime.set(bllManager.getStartTime(dayEntryList));
            endTime.set(bllManager.getEndTime(dayEntryList));
            stringDuration.set(bllManager.secToFormat(bllManager.calculateTaskDuration(dayEntryList).toSeconds()));
        } else {
            startTime.set(task.getCreationTime());
            endTime.set(task.getCreationTime());
            stringDuration.set("00:00:00");
        }
    }

    @Override
    public void addTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) throws ModelException {
        try {
            TaskEntry newTaskEntry = new TaskEntry(task, tempStartTime, tempEndTime);
            TaskEntry freshTaskEntry = bllManager.createTaskEntry(newTaskEntry);
            task.getTaskEntryList().add(freshTaskEntry);
            setupDayEntryList();
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

    @Override
    public void setTask(TaskConcrete1 task) {
        this.task = task;

    }

    @Override
    public TaskConcrete1 getTask() {
        return task;
    }

    @Override
    public LocalDateTime getEndTime() {
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
    public ObservableList<TaskEntry> getDayEntryList() {
        return dayEntryList;
    }

}
