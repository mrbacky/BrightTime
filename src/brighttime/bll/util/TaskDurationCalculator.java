package brighttime.bll.util;

import brighttime.be.TaskEntry;
import java.time.Duration;
import java.util.List;

/**
 *
 * @author rado
 */
public class TaskDurationCalculator {

    private final EntryDurationCalculator entryDurationCalculator;

    public TaskDurationCalculator() {
        entryDurationCalculator = new EntryDurationCalculator();
    }

    public Duration calculateTaskDuration(List<TaskEntry> entryList) {

        Duration totalDuration = Duration.ZERO;
        for (TaskEntry entry : entryList) {
            totalDuration = totalDuration.plus(entryDurationCalculator.calculateDuration(entry));
        }

        return totalDuration;
    }

}
