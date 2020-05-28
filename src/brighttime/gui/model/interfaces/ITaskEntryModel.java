/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ITaskEntryModel {

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    Duration calculateDuration(TaskEntry taskEntry);

    String secToFormat(long sec);

    public void setTaskEntry(TaskEntry taskEntry);

    TaskEntry getTaskEntry();

    LocalTime getStartTime();

    void setStartTime(LocalTime value);

    ObjectProperty startTimeProperty();

    ObjectProperty dateProperty();

    void setDate(LocalDate value);

    LocalDate getDate();

    LocalTime getEndTime();

    void setEndTime(LocalTime value);

    ObjectProperty endTimeProperty();

    void setupStartTimeListener();

    void setupEndTimeListener();

    void updateTaskEntryStartTime() throws ModelException;

    void updateTaskEntryEndTime() throws ModelException;

    void initializeTaskEntryModel();

    void setTaskEntryModelDetails();

}
