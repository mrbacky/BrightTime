package brighttime.bll.util;

import brighttime.be.TaskBase;
import brighttime.be.TaskEntry;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author rado
 */
public class TaskIntervalCalculator {

    public LocalDateTime getStartTime(List<TaskEntry> specificEntryList) {
        TaskEntry firstTaskEntry = specificEntryList.get(0);
        LocalDateTime taskStartTime = firstTaskEntry.getStartTime();
        return taskStartTime;
    }

    public LocalDateTime getEndTime(List<TaskEntry> specificEntryList) {
        TaskEntry latestTaskEntry = specificEntryList.get(specificEntryList.size() - 1);
        LocalDateTime taskEndTime = latestTaskEntry.getEndTime();
        return taskEndTime;
    }

}
