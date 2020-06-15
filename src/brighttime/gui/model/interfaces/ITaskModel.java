package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.gui.model.ModelException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ITaskModel {

    void initializeTaskModel();

    void setupDayEntryList();

    void setUpDayEntryListListener();

    void setTaskModelDetails();

    void addTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) throws ModelException;

    LocalDate getDate();

    void setDate(LocalDate date);

    void setTask(TaskConcrete1 task);

    TaskConcrete1 getTask();

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime value);

    ObjectProperty startTimeProperty();

    LocalDateTime getEndTime();

    void setEndTime(LocalDateTime value);

    ObjectProperty endTimeProperty();

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    ObservableList<TaskEntry> getDayEntryList();

}
