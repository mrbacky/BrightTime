package brighttime.bll.util;

import brighttime.be.TaskEntry;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author rado
 */
public class EntryDurationCalculator {

    public Duration calculateDuration(TaskEntry taskEntry) {
        return Duration.between(taskEntry.getStartTime(), taskEntry.getEndTime());

    }

}
