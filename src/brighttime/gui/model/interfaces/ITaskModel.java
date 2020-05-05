package brighttime.gui.model.interfaces;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import java.time.Duration;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ITaskModel {

    

    void setTask(Task task);

    Task getTask();

    Duration calculateDuration(TaskEntry taskEntry);

    Duration calculateDuration(Task task);

    String secToFormat(long sec);

    long formatToSec(String formatString);

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    public void createTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime);
    
}
