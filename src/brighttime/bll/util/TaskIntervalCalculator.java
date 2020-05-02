package brighttime.bll.util;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.time.LocalDateTime;

/**
 *
 * @author rado
 */
public class TaskIntervalCalculator {

    public LocalDateTime getStartTime(Task task) {
        TaskEntry firstTaskEntry = task.getTaskEntryList().get(0);
        LocalDateTime taskStartTime = firstTaskEntry.getStartTime();
        return taskStartTime;
    }

    public LocalDateTime getEndTime(Task task) {
        TaskEntry latestTaskEntry = task.getTaskEntryList().get(task.getTaskEntryList().size() - 1);
        LocalDateTime taskEndTime = latestTaskEntry.getEndTime();
        return taskEndTime;
    }

}
