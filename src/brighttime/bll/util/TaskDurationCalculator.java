package brighttime.bll.util;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.time.Duration;

/**
 *
 * @author rado
 */
public class TaskDurationCalculator {

    public TaskDurationCalculator() {

    }

    public Duration calculateDuration(Task task) {
        Duration totalDuration = Duration.ZERO;
        for (TaskEntry taskEntry : task.getTaskEntryList()) {
            totalDuration = totalDuration.plus(taskEntry.getDuration());
        }
        return totalDuration;
    }

}
