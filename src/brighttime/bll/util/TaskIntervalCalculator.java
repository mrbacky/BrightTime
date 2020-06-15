package brighttime.bll.util;

import brighttime.be.TaskEntry;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author rado
 */
public class TaskIntervalCalculator {

    public LocalDateTime getStartTime(List<TaskEntry> specificEntryList) {
        TaskEntry firstEntry = specificEntryList.get(0);
        LocalDateTime taskStartTime = firstEntry.getStartTime();
        return taskStartTime;
    }

    public LocalDateTime getEndTime(List<TaskEntry> specificEntryList) {
        TaskEntry lastEntry = specificEntryList.get(specificEntryList.size() - 1);
        LocalDateTime taskEndTime = lastEntry.getEndTime();
        return taskEndTime;
    }

}
