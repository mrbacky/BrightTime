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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
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

    public TaskModel(BllFacade bllManager) throws IOException {
        this.bllManager = bllManager;
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
    public Duration calculateDuration(TaskEntry taskEntry) {
        return bllManager.calculateDuration(taskEntry);
    }

    @Override
    public Duration calculateDuration(Task task) {
        if (task.getTaskEntryList() != null) {
            return bllManager.calculateDuration(task);
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

    public LocalDateTime getEndTime() {
        if (!task.getTaskEntryList().isEmpty()) {
            return bllManager.getEndTime(task);
        }
        return task.getCreationTime();
    }

    public LocalDateTime getStartTime() {
        if (!task.getTaskEntryList().isEmpty()) {
            return bllManager.getStartTime(task);
        }
        return task.getCreationTime();
    }

    @Override
    public void createTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) {
        try {
            TaskEntry newTaskEntry = new TaskEntry(task.getId(), task.getDescription(), tempStartTime, tempEndTime);
            task.getTaskEntryList().add(newTaskEntry);
            bllManager.createTaskEntry(newTaskEntry);
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
            Logger.getLogger(TaskModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
