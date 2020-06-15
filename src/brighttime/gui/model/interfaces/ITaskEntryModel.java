package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rados
 */
public interface ITaskEntryModel {

    void initializeTaskEntryModel();

    void setTaskEntryModelDetails();

    void setupStartTimeListener();

    void setupEndTimeListener();

    void updateTaskEntryStartTime() throws ModelException;

    void updateTaskEntryEndTime() throws ModelException;

    void setTaskEntry(TaskEntry taskEntry);

    TaskEntry getTaskEntry();

    LocalDate getDate();

    void setDate(LocalDate value);

    ObjectProperty dateProperty();

    LocalTime getStartTime();

    void setStartTime(LocalTime value);

    ObjectProperty startTimeProperty();

    LocalTime getEndTime();

    void setEndTime(LocalTime value);

    ObjectProperty endTimeProperty();

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    Duration calculateDuration(TaskEntry taskEntry);

    String secToFormat(int sec);

}
