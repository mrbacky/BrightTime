package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.gui.model.ModelException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.ListProperty;
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

    void setupDayEntryListListener();

    void setupEndTimeListener();

    LocalDateTime updatedStartTime();

    LocalDateTime updatedEndTime();

    List<TaskEntry> setUpDayEntryList();

    public void initializeModel();

    ObservableList getObsEntries();

    ObservableList getDayEntryListProp();

    void setDayEntryListProp(ObservableList value);

    ListProperty dayEntryListPropProperty();

}
