package brighttime.bll.util;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.time.Duration;

/**
 *
 * @author rado
 */
public class TaskDurationCalculator {
        
    private EntryDurationCalculator entryDurationCalculator;
    
    public TaskDurationCalculator() {
        entryDurationCalculator = new EntryDurationCalculator();
    }

    public Duration calculateDuration(Task task) {
        Duration totalDuration = Duration.ZERO;
        for (TaskEntry taskEntry : task.getTaskEntryList()) {
            totalDuration = totalDuration.plus(entryDurationCalculator.calculateDuration(taskEntry));
        }
        return totalDuration;
    }

}
