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

    void setTask(TaskConcrete1 task);

    TaskConcrete1 getTask();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    void setStartTime(LocalDateTime value);

    void setEndTime(LocalDateTime value);

    ObjectProperty endTimeProperty();

    ObjectProperty startTimeProperty();

    void addTaskEntry(LocalDateTime tempStartTime, LocalDateTime tempEndTime) throws ModelException;

    void setDate(LocalDate date);

    LocalDate getDate();

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    public void initializeTaskModel();

    ObservableList<TaskEntry> getDayEntryList();

    void setupDayEntryList();

    void setUpDayEntryListListener();

    void setTaskModelDetails();

}
