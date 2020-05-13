package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import brighttime.be.TaskType1;
import brighttime.gui.model.ModelException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rados
 */
public interface ITaskModel {

    void setTask(TaskType1 task);

    TaskType1 getTask();

    Duration calculateDuration(TaskEntry taskEntry);

    Duration calculateTaskDuration(List<TaskEntry> entryList);

    String secToFormat(long sec);

    long formatToSec(String formatString);

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    void setStartTime(LocalDateTime value);

    void setEndTime(LocalDateTime value);

    ObjectProperty endTimeProperty();

    ObjectProperty startTimeProperty();

    void addTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) throws ModelException;

    void setDate(LocalDate date);

    LocalDate getDate();

    List getDayEntryList();

    void setDayEntryList(List value);

    ObjectProperty dayEntryListProperty();

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    void setupStartTimeListener();

    void setupEndTimeListener();

}
