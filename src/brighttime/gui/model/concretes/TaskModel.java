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
import java.util.List;
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
        return bllManager.calculateDuration(task);
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
        return bllManager.getEndTime(task);
    }

    public LocalDateTime getStartTime() {
        return bllManager.getStartTime(task);

    }

}
